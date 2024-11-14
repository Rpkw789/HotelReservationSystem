/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.singleton.DailyRoomAllocationSessionBeanRemote;
import ejb.session.stateless.CheckInOutSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.RateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomAllocationSessionBeanRemote;
import ejb.session.stateless.RoomAvailabilitySessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import entity.Guest;
import entity.Rate;
import entity.Reservation;
import entity.Room;
import entity.RoomAllocationExceptionRecord;
import entity.RoomType;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import javafx.util.Pair;
import util.enumeration.EmployeeRoleEnum;
import util.enumeration.RateTypeEnum;
import util.enumeration.RoomAvailabilityStatusEnum;
import util.enumeration.OperationalStatusEnum;
import util.exception.CheckInException;
import util.exception.CheckOutException;
import util.exception.GuestExistsException;
import util.exception.GuestNotFoundException;
import util.exception.RateExistsException;
import util.exception.RateNotFoundException;
import util.exception.RoomExistsException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeExistsException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author ranen
 */
public class HotelOperationModule {

    private CheckInOutSessionBeanRemote checkInOutSessionBean;
    private RoomTypeSessionBeanRemote roomTypeSessionBean;
    private ReservationSessionBeanRemote reservationSessionBean;
    private GuestSessionBeanRemote guestSessionBean;
    private RoomAvailabilitySessionBeanRemote roomAvailabilitySessionBean;
    private DailyRoomAllocationSessionBeanRemote dailyRoomAllocationSessionBean;
    private RoomAllocationSessionBeanRemote roomAllocationSessionBean;
    private RoomSessionBeanRemote roomSessionBean;
    private RateSessionBeanRemote rateSessionBean;

    private Employee employee;

    private List<InputNumberRolePair> inputNumberRolePairList;

    public HotelOperationModule(CheckInOutSessionBeanRemote checkInOutSessionBean, RoomTypeSessionBeanRemote roomTypeSessionBean, ReservationSessionBeanRemote reservationSessionBean, GuestSessionBeanRemote guestSessionBean, Employee employee, DailyRoomAllocationSessionBeanRemote dailyRoomAllocationSessionBean, RoomAllocationSessionBeanRemote roomAllocationSessionBean, RoomAvailabilitySessionBeanRemote roomAvailabilitySessionBean, RoomSessionBeanRemote roomSessionBean, RateSessionBeanRemote rateSessionBean) {
        this.checkInOutSessionBean = checkInOutSessionBean;
        this.roomTypeSessionBean = roomTypeSessionBean;
        this.reservationSessionBean = reservationSessionBean;
        this.guestSessionBean = guestSessionBean;
        this.employee = employee;
        this.dailyRoomAllocationSessionBean = dailyRoomAllocationSessionBean;
        this.roomAllocationSessionBean = roomAllocationSessionBean;
        this.roomAvailabilitySessionBean = roomAvailabilitySessionBean;
        this.roomSessionBean = roomSessionBean;
        this.rateSessionBean = rateSessionBean;

        this.inputNumberRolePairList = new ArrayList<InputNumberRolePair>();
        for (EmployeeRoleEnum role : employee.getEmployeeRoles()) {
            inputNumberRolePairList.add(new InputNumberRolePair(role));
        }
    }

    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("");
            System.out.println("***Hotel Operation Module***");

            int dailyAllocationNumber = printAvailableOperations();
            int exit = dailyAllocationNumber + 1;

            int response = scanner.nextInt();
            if (response == exit) {
                return;
            }
            processResponse(response, dailyAllocationNumber);
        }
    }

    private void processResponse(int response, int dailyAllocationNumber) {
        if (response == dailyAllocationNumber) {
            doAllocateRoomToCurrentDayReservation();
            return;
        }
        for (InputNumberRolePair pair : inputNumberRolePairList) {
            if (pair.getNumber() == response) {
                EmployeeRoleEnum role = pair.getRole();

                if (role == EmployeeRoleEnum.OPERATION_MANAGER) {
                    generalOperationMenu();
                    return;
                } else if (role == EmployeeRoleEnum.SALES_MANAGER) {
                    salesOperationMenu();
                    return;
                } else if (role == EmployeeRoleEnum.GUEST_RELATION_OFFICER) {
                    guestOperationMenu();
                    return;
                }
            }
        }
        System.out.println("");
        System.out.println("Error: Enter input again");
    }

    private int printAvailableOperations() {
        int counter = 1;
        InputNumberRolePair operation = retrieveByRole(EmployeeRoleEnum.OPERATION_MANAGER);
        InputNumberRolePair sales = retrieveByRole(EmployeeRoleEnum.SALES_MANAGER);
        InputNumberRolePair guest = retrieveByRole(EmployeeRoleEnum.GUEST_RELATION_OFFICER);
        if (operation != null) {
            System.out.println(counter + ": General Operation");
            operation.setNumber(counter);
            counter++;
        }
        if (sales != null) {
            System.out.println(counter + ": Sales Operation");
            sales.setNumber(counter);
            counter++;
        }
        if (guest != null) {
            System.out.println(counter + ": Guest Operation");
            guest.setNumber(counter);
            counter++;
        }
        System.out.println(counter + ": Allocate Room to current day reservation");
        System.out.println((counter + 1) + ": Exit");
        return counter;
    }

    private InputNumberRolePair retrieveByRole(EmployeeRoleEnum role) {
        for (InputNumberRolePair pair : inputNumberRolePairList) {
            if (pair.getRole() == role) {
                return pair;
            }
        }
        return null;
    }

    ///////////GENERAL OPERATION////////////////////
    private void generalOperationMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("");
            System.out.println("***General Operation Menu***");
            System.out.println("*Room Type*");
            System.out.println("1: Create New Room Type");
            System.out.println("2: Update Room Type");
            System.out.println("3: Delete Room Type");
            System.out.println("4: View All Room Types");
            System.out.println("*Room*");
            System.out.println("5: Create New Room");
            System.out.println("6: Update Room");
            System.out.println("7: Delete Room");
            System.out.println("8: View All Rooms");
            System.out.println("*Others*");
            System.out.println("9: View Room Allocation Exception Report");
            System.out.println("10: Exit");

            int response = scanner.nextInt();
            switch (response) {
                case 1:
                    doCreateNewRoomType();
                    break;
                case 2:
                    doUpdateRoomType();
                    break;
                case 3:
                    doDeleteRoomType();
                    break;
                case 4:
                    doViewAllRoomTypes();
                    break;
                case 5:
                    doCreateNewRoom();
                    break;
                case 6:
                    doUpdateRoom();
                    break;
                case 7:
                    doDeleteRoom();
                    break;
                case 8:
                    doViewAllRooms();
                    break;
                case 9:
                    doViewRoomAllocationExceptionReport();
                    break;
                case 10:
                    return;
                default:
                    System.out.println("");
                    System.out.println("Error: Enter input again");
                    break;
            }
        }

    }

    private void doCreateNewRoomType() {
        Scanner scanner = new Scanner(System.in);
        List<RoomType> roomTypes = roomTypeSessionBean.getAllRoomType();
        List<RoomType> filteredRoomTypes = new ArrayList<RoomType>();
        roomTypes.stream().filter(roomType -> roomType.isEnabled()).forEach(x -> filteredRoomTypes.add(x));
        System.out.println("*Create New Room Type*");
        String name = "";
        while (true) {
            System.out.print("Name > ");
            name = scanner.nextLine().trim();

            if (roomTypeSessionBean.isUniqueName(name)) {
                break;
            } else {
                System.out.println("Error: Name taken. Enter another name");
            }
        }

        System.out.print("Description > ");
        String description = scanner.nextLine().trim();
        System.out.print("Size > ");
        String size = scanner.nextLine().trim();
        System.out.print("Bed > ");
        String bed = scanner.nextLine().trim();
        System.out.print("Capacity > ");
        int capacity = scanner.nextInt();
        scanner.nextLine();
        int count = 1;
        RoomType roomType = new RoomType(name, description, size, bed, capacity);

        while (!filteredRoomTypes.isEmpty()) {
            System.out.println("Choose Next Higher Room Type");
            for (int i = 0; i < filteredRoomTypes.size(); i++) {
                System.out.println((i + 1) + ": " + filteredRoomTypes.get(i).getName());
            }
            System.out.println((filteredRoomTypes.size() + 1) + ": NIL");
            int response = scanner.nextInt();
            scanner.nextLine();
            if (response == filteredRoomTypes.size() + 1) {
                break;
            }

            RoomType nextHigherRoomType = filteredRoomTypes.get(response - 1);
            roomType.setNextHigherRoomType(nextHigherRoomType);
            break;
        }

        System.out.println("Type 'q' if you have no more amenities");
        List<String> amenities = roomType.getAmenities();
        while (true) {
            System.out.print("Amenity #" + count + " > ");
            String amentity = scanner.nextLine().trim();
            if (amentity.equals("q")) {
                break;
            }
            amenities.add(amentity);
            count++;
        }
        try {
            Long roomTypeId = roomTypeSessionBean.createRoomType(roomType);
            System.out.println("Room Type created with Room Type ID: " + roomTypeId);
        } catch (RoomTypeExistsException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void doUpdateRoomType() {
        Scanner scanner = new Scanner(System.in);
        List<RoomType> roomTypes = roomTypeSessionBean.getAllRoomType();

        System.out.println("");
        System.out.println("*Update Room Type*");
        int size = roomTypes.size();
        int exit = size + 1;

        for (int i = 0; i < size; i++) {
            System.out.println((i + 1) + ": " + roomTypes.get(i).getName());
        }
        System.out.println((size + 1) + ": Exit");
        System.out.println("Select a Room Type to update");

        int response = 0;
        while (true) {
            response = scanner.nextInt();

            if (response == exit) {
                return;
            }

            if (response <= 0 || response > exit) {
                System.out.println("");
                System.out.println("Error: Enter input again");
                continue;
            }

            break;
        }

        RoomType roomType = roomTypes.get(response - 1);
        while (true) {
            System.out.println("");
            System.out.println("Choose an attribute to update");
            System.out.println("RoomType - " + roomType.getRoomTypeId());
            System.out.println("1: Name: " + roomType.getName());
            System.out.println("2: Description: " + roomType.getDescription());
            System.out.println("3: Size: " + roomType.getSize());
            System.out.println("4: Bed: " + roomType.getBed());
            System.out.println("5: Capacity: " + roomType.getCapacity());
            String amenities = roomType.getAmenities().toString();
            System.out.println("6: Amenities: " + amenities.substring(1, amenities.length() - 1));
            if (roomType.getNextHigherRoomType() == null) {
                System.out.println("7: Next Higher Room Type: None");
            } else {
                System.out.println("7: Next Higher Room Type: " + roomType.getNextHigherRoomType().getName());
            }

            System.out.println("8: Exit");

            int response1 = scanner.nextInt();
            scanner.nextLine();

            if (response1 == 1) {
                System.out.print("New Name > ");
                roomType.setName(scanner.nextLine().trim());
            } else if (response1 == 2) {
                System.out.print("New Description > ");
                roomType.setDescription(scanner.nextLine().trim());
            } else if (response1 == 3) {
                System.out.print("New Size > ");
                roomType.setSize(scanner.nextLine().trim());
            } else if (response1 == 4) {
                System.out.print("New Bed > ");
                roomType.setBed(scanner.nextLine().trim());
            } else if (response1 == 5) {
                System.out.print("New Capacity > ");
                roomType.setCapacity(scanner.nextInt());
                scanner.nextLine();
            } else if (response1 == 6) {
                System.out.println("Type 'q' if you have no more amenities");
                List<String> newAmenities = new ArrayList<String>();
                int count = 1;
                while (true) {
                    System.out.print("Amenity #" + count + " > ");
                    String amentity = scanner.nextLine().trim();
                    if (amentity.equals("q")) {
                        break;
                    }
                    newAmenities.add(amentity);
                    count++;
                }
                roomType.setAmenities(newAmenities);
            } else if (response1 == 7) {

                System.out.println("Choose Next Higher Room Type");
                for (int i = 0; i < roomTypes.size(); i++) {
                    System.out.println((i + 1) + ": " + roomTypes.get(i).getName());
                }
                System.out.println((roomTypes.size() + 1) + ": NIL");
                int response2 = scanner.nextInt();
                scanner.nextLine();
                if (response2 == roomTypes.size() + 1) {
                    break;
                }

                RoomType nextHigherRoomType = roomTypes.get(response2 - 1);
                if (roomType.equals(nextHigherRoomType)) {
                    System.out.println("Not Allowed to Choose Itself");
                    continue;
                }
                roomType.setNextHigherRoomType(nextHigherRoomType);

            } else if (response1 == 8) {
                break;
            } else {
                System.out.println("");
                System.out.println("Error: Enter input again");
            }
        }

        try {
            roomTypeSessionBean.updateRoomType(roomType);
            System.out.println("Room Type updated");
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }

    private void doDeleteRoomType() {
        Scanner scanner = new Scanner(System.in);
        List<RoomType> roomTypes = roomTypeSessionBean.getAllRoomType();

        while (true) {
            System.out.println("");
            System.out.println("*Delete Room Type*");
            int size = roomTypes.size();
            int exit = size + 1;

            for (int i = 0; i < size; i++) {
                System.out.println((i + 1) + ": " + roomTypes.get(i).getName());
            }
            System.out.println((size + 1) + ": Exit");
            System.out.println("Select a Room Type to delete");
            int response = scanner.nextInt();
            scanner.nextLine();

            if (response == exit) {
                break;
            }

            if (response <= 0 || response > exit) {
                System.out.println("");
                System.out.println("Error: Enter input again");
                continue;
            }

            RoomType roomType = roomTypes.get(response - 1);
            System.out.println("");
            System.out.println("RoomType - " + roomType.getRoomTypeId());
            System.out.println("Name: " + roomType.getName());
            System.out.println("Description: " + roomType.getDescription());
            System.out.println("Size: " + roomType.getSize());
            System.out.println("Bed: " + roomType.getBed());
            System.out.println("Capacity: " + roomType.getCapacity());
            String amenities = roomType.getAmenities().toString();
            System.out.println("Amenities: " + amenities.substring(1, amenities.length() - 1));
            System.out.println("Status: " + (roomType.isEnabled() ? "Enabled" : "Disabled"));
            while (true) {
                System.out.println("Remove/Disabled this Room Type? (y/n)");
                String reply = scanner.nextLine().trim().toLowerCase();
                if (reply.equals("y")) {
                    try {
                        roomTypeSessionBean.deleteRoomType(roomType.getRoomTypeId());
                    } catch (RoomTypeNotFoundException ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                    return;
                } else if (reply.equals("n")) {
                    break;
                } else {
                    System.out.println("Error: Enter input again");
                }
            }
        }
    }

    private void doViewAllRoomTypes() {
        Scanner scanner = new Scanner(System.in);
        List<RoomType> roomTypes = roomTypeSessionBean.getAllRoomType();
        if (roomTypes.isEmpty()) {
            System.out.println("No Room Types in the System");
            return;
        }
        while (true) {
            System.out.println("");
            System.out.println("*View All Room Types*");
            int size = roomTypes.size();
            int exit = size + 1;

            for (int i = 0; i < size; i++) {
                System.out.println((i + 1) + ": " + roomTypes.get(i).getName());
            }
            System.out.println((size + 1) + ": Exit");
            System.out.println("Select a Room Type to view / Exit");
            int response = scanner.nextInt();

            if (response == exit) {
                break;
            }

            if (response <= 0 || response > exit) {
                System.out.println("");
                System.out.println("Error: Enter input again");
                continue;
            }

            RoomType roomType = roomTypes.get(response - 1);
            System.out.println("RoomType - " + roomType.getRoomTypeId());
            if (roomType.getNextHigherRoomType() != null) {
                System.out.println("Next Higher Room Type: " + roomType.getNextHigherRoomType().getName());
            }
            System.out.println("Name: " + roomType.getName());
            System.out.println("Description: " + roomType.getDescription());
            System.out.println("Size: " + roomType.getSize());
            System.out.println("Bed: " + roomType.getBed());
            System.out.println("Capacity: " + roomType.getCapacity());
            String amenities = roomType.getAmenities().toString();
            System.out.println("Amenities: " + amenities.substring(1, amenities.length() - 1));
        }
    }

    private void doCreateNewRoom() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.println("*Create New Room*");
        String roomNumber = "";
        List<RoomType> roomTypes = roomTypeSessionBean.getAllRoomType();
        List<RoomType> filteredRoomTypes = new ArrayList<RoomType>();
        roomTypes.stream().filter(roomType -> roomType.isEnabled()).forEach(x -> filteredRoomTypes.add(x));
        if (filteredRoomTypes.isEmpty()) {
            System.out.println("No Room Type available. Create Room Type first");
            return;
        }
        while (true) {
            System.out.print("Room Number > ");
            roomNumber = scanner.nextLine().trim();

            if (roomSessionBean.isUniqueRoomNumber(roomNumber)) {
                break;
            } else {
                System.out.println("Error: Room Number taken. Enter another name");
            }
        }
        System.out.println("Choose Room Type");
        for (int i = 0; i < filteredRoomTypes.size(); i++) {
            System.out.println((i + 1) + ": " + filteredRoomTypes.get(i).getName());
        }
        int response = scanner.nextInt();
        scanner.nextLine();

        Long chosenRoomTypeId = filteredRoomTypes.get(response - 1).getRoomTypeId();
        Room room = new Room(roomNumber);

        try {
            Long roomId = roomSessionBean.createNewRoom(room, chosenRoomTypeId);
            System.out.println("Room created with Room Type ID: " + roomId);
        } catch (RoomExistsException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void doUpdateRoom() {
        Scanner scanner = new Scanner(System.in);
        List<RoomType> roomTypes = roomTypeSessionBean.getAllRoomType();
        List<RoomType> filteredRoomTypes = new ArrayList<RoomType>();
        roomTypes.stream().filter(roomType -> roomType.isEnabled()).forEach(x -> filteredRoomTypes.add(x));
        while (true) {
            System.out.println("*Update Room*");
            System.out.println("To Exit, type 'q'");
            System.out.print("Room Number > ");
            String roomNumber = scanner.nextLine().trim();
            if (roomNumber.equals("q")) {
                return;
            }

            try {
                Room room = roomSessionBean.getRoomByRoomNumber(roomNumber);
                while (true) {
                    System.out.println("Choose an attribute to update");
                    System.out.println("1: Room Number: " + room.getRoomNumber());
                    System.out.println("2: Room Type: " + room.getRoomType().getName());
                    System.out.println("3: Room Availability Status: " + room.getAvailabilityStatus());
                    System.out.println("4: Room Operational Status: " + room.getOperationalStatus());
                    System.out.println("5: Done");
                    System.out.println("6: Exit");
                    int response = scanner.nextInt();
                    scanner.nextLine();

                    if (response == 1) {
                        System.out.print("New Room Number > ");
                        room.setRoomNumber(scanner.nextLine().trim());
                    } else if (response == 2) {
                        System.out.println("Select Room Type");
                        for (int i = 0; i < roomTypes.size(); i++) {
                            System.out.println((i + 1) + ": " + filteredRoomTypes.get(i).getName());
                        }
                        int newRoomType = scanner.nextInt() - 1;
                        scanner.nextLine();
                        room.setRoomType(filteredRoomTypes.get(newRoomType));
                    } else if (response == 3) {
                        System.out.println("Select Room Availability Status");
                        System.out.println("1: Available");
                        System.out.println("2: Not Available");
                        int availability = scanner.nextInt();
                        scanner.nextLine();
                        if (availability == 1) {
                            room.setAvailabilityStatus(RoomAvailabilityStatusEnum.AVAILABLE);
                        } else if (availability == 2) {
                            room.setAvailabilityStatus(RoomAvailabilityStatusEnum.NOT_AVAILABLE);
                        }
                    } else if (response == 4) {
                        System.out.println("Select Room Availability Status");
                        System.out.println("1: Enabled");
                        System.out.println("2: Disabled");
                        int availability = scanner.nextInt();
                        scanner.nextLine();
                        if (availability == 1) {
                            room.setOperationalStatus(OperationalStatusEnum.ENABLED);
                        } else if (availability == 2) {
                            room.setOperationalStatus(OperationalStatusEnum.DISABLED);
                        }
                    } else if (response == 5) {
                        try {
                            roomSessionBean.updateRoom(room);
                            System.out.println("Room updated");
                            return;
                        } catch (RoomNotFoundException ex) {
                            System.out.println("Error: " + ex.getMessage());
                            break;
                        }
                    } else {
                        break;
                    }
                }

            } catch (RoomNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
                System.out.println("Input Room Number Again");
                continue;
            }

        }
    }

    private void doDeleteRoom() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("*Delete Room*");
            System.out.println("To Exit, type 'q'");
            System.out.print("Room Number > ");
            String roomNumber = scanner.nextLine().trim();
            if (roomNumber.equals("q")) {
                return;
            }

            try {
                Room room = roomSessionBean.getRoomByRoomNumber(roomNumber);
                System.out.println("Room Number: " + room.getRoomNumber());
                System.out.println("Room Type: " + room.getRoomType().getName());
                System.out.println("Room Availability Status: " + room.getAvailabilityStatus());
                System.out.println("Room Operational Status: " + room.getOperationalStatus());

                System.out.print("Remove / Disable this Room? (y/n) > ");
                String response = scanner.nextLine().trim();
                if (response.equals("y")) {
                    roomSessionBean.deleteRoom(room.getRoomId());
                    System.out.println("Room " + room.getRoomNumber() + " deleted");
                } else {
                    continue;
                }
            } catch (RoomNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
                System.out.println("Input Room Number Again");
                continue;
            }

        }
    }

    private void doViewAllRooms() {
        Scanner scanner = new Scanner(System.in);
        List<Room> rooms = roomSessionBean.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No Rooms in the System");
            return;
        }
        while (true) {
            System.out.println("");
            System.out.println("*View All Room*");
            int size = rooms.size();
            int exit = size + 1;

            for (int i = 0; i < size; i++) {
                System.out.println((i + 1) + ": " + rooms.get(i).getRoomNumber());
            }
            System.out.println((size + 1) + ": Exit");
            System.out.println("Select a Room to view / Exit");
            int response = scanner.nextInt();

            if (response == exit) {
                break;
            }

            if (response <= 0 || response > exit) {
                System.out.println("");
                System.out.println("Error: Enter input again");
                continue;
            }

            Room room = rooms.get(response - 1);
            System.out.println("Room Number: " + room.getRoomNumber());
            System.out.println("Room Type: " + room.getRoomType().getName());
            System.out.println("Room Availability Status: " + room.getAvailabilityStatus());
            System.out.println("Room Operational Status: " + room.getOperationalStatus());
        }
    }

    private void doViewRoomAllocationExceptionReport() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*View Room Allocation Exception Report*");
        System.out.print("Enter Date > ");
        LocalDate date = LocalDate.parse(scanner.nextLine().trim());

        List<RoomAllocationExceptionRecord> records = roomAllocationSessionBean.getRoomAllocationExceptionRecord(date);
        for (RoomAllocationExceptionRecord record : records) {
            System.out.println("Record " + record.getRoomAllocationExceptionId());
            System.out.println("    Reservation Id: " + record.getAffectedReservation().getReservationId());
            System.out.println("    Description: " + record.getDescription());
        }
    }

    ///////////SALES OPERATION//////////////////////
    private void salesOperationMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("");
            System.out.println("***Sales Operation Menu***");
            System.out.println("1: Create New Room Rate");
            System.out.println("2: Update Room Rate");
            System.out.println("3: Delete Room Rate");
            System.out.println("4: View All Room Rates");
            System.out.println("5: Exit");

            int response = scanner.nextInt();
            scanner.nextLine();
            switch (response) {
                case 1:
                    doCreateNewRoomRate();
                    break;
                case 2:
                    doUpdateRoomRate();
                    break;
                case 3:
                    doDeletRoomRate();
                    break;
                case 4:
                    doViewAllRoomRates();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("");
                    System.out.println("Error: Enter input again");
                    break;
            }
        }
    }

    private void doCreateNewRoomRate() {
        Scanner scanner = new Scanner(System.in);
        List<RoomType> roomTypes = roomTypeSessionBean.getAllRoomType();
        List<RoomType> filteredRoomTypes = new ArrayList<RoomType>();
        roomTypes.stream().filter(roomType -> roomType.isEnabled()).forEach(x -> filteredRoomTypes.add(x));

        System.out.println("*Create New Room Rate*");
        if (filteredRoomTypes.isEmpty()) {
            System.out.println("No Room Type found. Create Room Type first");
            return;
        }
        System.out.print("Name > ");
        String name = scanner.nextLine().trim();
        System.out.println("Select Room Type");
        for (int i = 0; i < roomTypes.size(); i++) {
            System.out.println((i + 1) + ": " + filteredRoomTypes.get(i).getName());
        }
        RoomType roomType = filteredRoomTypes.get(scanner.nextInt() - 1);
        scanner.nextLine();
        System.out.println("Select Rate Type");
        RateTypeEnum[] rateTypes = RateTypeEnum.values();
        for (int i = 0; i < rateTypes.length; i++) {
            System.out.println((i + 1) + ": " + rateTypes[i].toString());
        }
        RateTypeEnum rateType = rateTypes[scanner.nextInt() - 1];
        scanner.nextLine();
        System.out.print("Rate Per Night > ");
        double ratePerNight = scanner.nextDouble();
        scanner.nextLine();
        Rate rate = new Rate(name, ratePerNight, rateType, roomType);
        if (rateType.equals(RateTypeEnum.PEAK) || rateType.equals(RateTypeEnum.PROMOTION)) {
            while (true) {
                System.out.print("Start Period (YYYY-MM-DD) > ");
                String startDate = scanner.nextLine().trim();
                try {
                    LocalDate localDate = LocalDate.parse(startDate);
                    rate.setValidityStart(localDate);
                    break;
                } catch (DateTimeParseException ex) {
                    System.out.println("Error: Invalid Date");
                }
            }
            while (true) {
                System.out.print("End Period (YYYY-MM-DD) > ");
                String endDate = scanner.nextLine().trim();
                try {
                    LocalDate localDate = LocalDate.parse(endDate);
                    rate.setValidityEnd(localDate);
                    break;
                } catch (DateTimeParseException ex) {
                    System.out.println("Error: Invalid Date");
                }
            }
        } else {
            rate.setValidityStart(LocalDate.of(1900, 1, 1));
            rate.setValidityEnd(LocalDate.of(2200, 1, 1));
        }

        try {
            Long rateId = rateSessionBean.createNewRate(rate);
            System.out.println("Rate created with Rate Type ID: " + rateId);
        } catch (RateExistsException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }

    private void doUpdateRoomRate() {
        Scanner scanner = new Scanner(System.in);
        List<RoomType> roomTypes = roomTypeSessionBean.getAllRoomType();
        List<RoomType> filteredRoomTypes = new ArrayList<RoomType>();
        roomTypes.stream().filter(roomType -> roomType.isEnabled()).forEach(x -> filteredRoomTypes.add(x));

        while (true) {
            System.out.println("Update Room Rate");
            System.out.println("To Exit: Type 'q'");
            System.out.print("Room Rate Name > ");
            String rateName = scanner.nextLine().trim();
            if (rateName.equals("q")) {
                return;
            }

            try {
                Rate rate = rateSessionBean.getRateByName(rateName);

                while (true) {
                    System.out.println("Choose an attribute to update");
                    System.out.println("1: Name: " + rate.getName());
                    System.out.println("2: Room Type: " + rate.getRoomType().getName());
                    System.out.println("3: Rate Type: " + rate.getRateType());
                    System.out.println("4: Rate Per Night: " + rate.getRatePerNight());
                    if (rate.getRateType().equals(RateTypeEnum.PEAK) || rate.getRateType().equals(RateTypeEnum.PROMOTION)) {
                        System.out.println("5: Start Period: " + rate.getValidityStart());
                        System.out.println("6: End Period: " + rate.getValidityEnd());
                        System.out.println("7: Done");
                        System.out.println("8: Exit");
                    } else {
                        System.out.println("5: Done");
                        System.out.println("6: Exit");
                    }
                    int response = scanner.nextInt();
                    scanner.nextLine();
                    if (response == 1) {
                        System.out.print("Name > ");
                        rate.setName(scanner.nextLine().trim());
                    } else if (response == 2) {
                        System.out.println("Select Room Type");
                        for (int i = 0; i < roomTypes.size(); i++) {
                            System.out.println((i + 1) + ": " + filteredRoomTypes.get(i).getName());
                        }
                        RoomType roomType = filteredRoomTypes.get(scanner.nextInt() - 1);
                        scanner.nextLine();
                        rate.setRoomType(roomType);
                    } else if (response == 3) {
                        System.out.print("Select Rate Type");
                        RateTypeEnum[] rateTypes = RateTypeEnum.values();
                        for (int i = 0; i < rateTypes.length; i++) {
                            System.out.println((i + 1) + ": " + rateTypes[i].toString());
                        }
                        RateTypeEnum rateType = rateTypes[scanner.nextInt() - 1];
                        scanner.nextLine();
                        rate.setRateType(rateType);
                    } else if (response == 4) {
                        System.out.print("Rate Per Night > ");
                        rate.setRatePerNight(scanner.nextDouble());
                        scanner.nextLine();
                    } else if (response == 5 && (rate.getRateType().equals(RateTypeEnum.PEAK) || rate.getRateType().equals(RateTypeEnum.PROMOTION))) {
                        LocalDate start = null;
                        while (start == null || !start.isBefore(rate.getValidityEnd())) {
                            System.out.print("Start Period (YYYY-MM-DD) > ");
                            String startDate = scanner.nextLine().trim();
                            try {
                                start = LocalDate.parse(startDate);
                                if (!start.isBefore(rate.getValidityEnd())) {
                                    System.out.println("Error: Start date must be before end date");
                                    start = null;
                                } else {
                                    rate.setValidityStart(start);
                                    break;
                                }

                            } catch (DateTimeParseException ex) {
                                System.out.println("Error: Invalid Date");
                            }
                        }
                    } else if (response == 6 && (rate.getRateType().equals(RateTypeEnum.PEAK) || rate.getRateType().equals(RateTypeEnum.PROMOTION))) {
                        LocalDate end = null;
                        while (end == null || !end.isAfter(rate.getValidityStart())) {
                            System.out.print("End Period (YYYY-MM-DD) > ");
                            String endDate = scanner.nextLine().trim();
                            try {
                                end = LocalDate.parse(endDate);
                                if (!end.isAfter(rate.getValidityStart())) {
                                    System.out.println("Error: End date must be after start date");
                                    end = null;
                                } else {
                                    rate.setValidityEnd(end);
                                    break;
                                }

                            } catch (DateTimeParseException ex) {
                                System.out.println("Error: Invalid Date");
                            }
                        }
                    } else if (response == 5 || response == 7) {
                        try {
                            RateTypeEnum rateType = rate.getRateType();
                            if (rateType.equals(RateTypeEnum.NORMAL) || rateType.equals(RateTypeEnum.PUBLISHED)) {
                                rate.setValidityStart(LocalDate.of(1900, 1, 1));
                                rate.setValidityEnd(LocalDate.of(2200, 1, 1));
                            }
                            rateSessionBean.updateRate(rate);
                            System.out.println("Room Rate Updated");
                            return;
                        } catch (RateNotFoundException ex) {
                            System.out.println("Error: " + ex.getMessage());
                        }
                    } else {
                        return;
                    }
                }

            } catch (RateNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
                System.out.println("Input Room Rate name again");
                continue;
            }
        }
    }

    private void doDeletRoomRate() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Delete Room Rate");
            System.out.println("To Exit: Type 'q'");
            System.out.print("Room Rate Name > ");
            String rateName = scanner.nextLine().trim();
            if (rateName.equals("q")) {
                return;
            }

            try {
                Rate rate = rateSessionBean.getRateByName(rateName);
                System.out.println("Name: " + rate.getName());
                System.out.println("Room Type: " + rate.getRoomType().getName());
                System.out.println("Rate Type: " + rate.getRateType());
                System.out.println("Rate Per Night: " + rate.getRatePerNight());
                if (rate.getValidityStart() != null) {
                    System.out.println("Start Period: " + rate.getValidityStart());
                    System.out.println("End Period: " + rate.getValidityEnd());
                }
                System.out.print("Remove / Disable this Room? (y/n) > ");
                String response = scanner.nextLine().trim();
                if (response.equals("y")) {
                    try {
                        rateSessionBean.deleteRate(rate.getRateId());
                        System.out.println("Room " + rate.getName() + " Removed / Disabled");
                        return;
                    } catch (RateNotFoundException ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                } else {
                    continue;
                }
            } catch (RateNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
                System.out.println("Input Room Rate Name again");
                continue;
            }
        }

    }

    private void doViewAllRoomRates() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*View All Room Rates*");
        List<Rate> rates = rateSessionBean.getAllRates();

        if (rates.isEmpty()) {
            System.out.println("No Room Rates in the System");
            return;
        }

        int size = rates.size();
        int exit = size + 1;

        while (true) {
            for (int i = 0; i < size; i++) {
                System.out.println((i + 1) + ": " + rates.get(i).getName());
            }
            System.out.println((size + 1) + ": Exit");
            System.out.println("Select a Room to view / Exit");
            int response = scanner.nextInt();
            scanner.nextLine();

            if (response == exit) {
                break;
            }

            if (response <= 0 || response > exit) {
                System.out.println("");
                System.out.println("Error: Enter input again");
                continue;
            }

            Rate rate = rates.get(response - 1);
            System.out.println("Name: " + rate.getName());
            System.out.println("Room Type: " + rate.getRoomType().getName());
            System.out.println("Rate Type: " + rate.getRateType());
            System.out.println("Rate Per Night: " + rate.getRatePerNight());
            RateTypeEnum rateType = rate.getRateType();
            if (!rateType.equals(RateTypeEnum.NORMAL) && !rateType.equals(RateTypeEnum.PUBLISHED)) {
                System.out.println("Start Period: " + rate.getValidityStart());
                System.out.println("End Period: " + rate.getValidityEnd());
            }
        }

    }

    ///////////GUEST OPERATION//////////////////////
    private void guestOperationMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("");
            System.out.println("***Guest Operation Menu***");
            System.out.println("1: Walk-in Search & Reserve Room");
            System.out.println("2: Check-in Guest");
            System.out.println("3: Check-out Guest");
            System.out.println("4: Exit");

            int response = scanner.nextInt();
            switch (response) {
                case 1:
                    doWalkInSearchReserveRoom();
                    break;
                case 2:
                    doCheckInGuest();
                    break;
                case 3:
                    doCheckOutGuest();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("");
                    System.out.println("Error: Enter input again");
                    break;
            }
        }
    }

    private void doWalkInSearchReserveRoom() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*Walk In Search & Reserve Room*");
        LocalDate startDate = null;
        String date = "";

        while (startDate == null) {
            System.out.print("Enter Check-In Date (YYYY-MM-DD) >");
            date = scanner.nextLine().trim();
            try {
                startDate = LocalDate.parse(date);
            } catch (DateTimeParseException ex) {
                System.out.println("Error: Invalid date. Please enter check-in date again");
            }
        }

        LocalDate endDate = null;
        while (endDate == null || !endDate.isAfter(startDate)) {
            System.out.print("Enter Check-Out Date (YYYY-MM-DD) > ");
            date = scanner.nextLine().trim();
            try {
                endDate = LocalDate.parse(date);
                if (!endDate.isAfter(startDate)) {
                    System.out.println("Error: Check-out date must be after the check-in date");
                    endDate = null;
                }
            } catch (DateTimeParseException ex) {
                System.out.println("Error: Invalid date");
            }
        }

        List<Pair<RoomType, Integer>> roomTypesAndNumber = roomAvailabilitySessionBean.getAvailableRoomTypeAndNumber(startDate, endDate);
        System.out.println("Available Room Types and Quantity");
        for (int i = 0; i < roomTypesAndNumber.size(); i++) {
            Pair<RoomType, Integer> pair = roomTypesAndNumber.get(i);
            double cost = roomAvailabilitySessionBean.getCostWalkIn(startDate, endDate, pair.getKey().getRoomTypeId());
            System.out.println((i + 1) + ": Room Type = " + pair.getKey().getName() + ", Quantity = " + pair.getValue() + ", Cost Per Room = " + cost);
        }
        int exit = roomTypesAndNumber.size() + 1;
        System.out.println(exit + ": Exit");
        System.out.println("Choose Room Type");

        int response = scanner.nextInt();
        scanner.nextLine();
        if (response == exit) {
            return;
        }
        Pair<RoomType, Integer> chosenPair = roomTypesAndNumber.get(response - 1);
        double cost = roomAvailabilitySessionBean.getCostWalkIn(startDate, endDate, chosenPair.getKey().getRoomTypeId());

        int numberOfRooms = 0;
        while (true) {
            System.out.print("Number of Rooms > ");
            numberOfRooms = scanner.nextInt();
            scanner.nextLine();
            if(numberOfRooms > chosenPair.getValue()) {
                System.out.println("Insufficient Rooms. Input Again");
            } else {
                break;
            }
        }

        Reservation reservation = new Reservation(numberOfRooms, cost * numberOfRooms, false, startDate, endDate, chosenPair.getKey());
        List<Rate> usedRates = roomAvailabilitySessionBean.getRateByRoomTypeWalkIn(chosenPair.getKey().getRoomTypeId());
        reservation.setRates(usedRates);

        System.out.println("Choose an Option:");
        System.out.println("1: Register Guest");
        System.out.println("2: Log In Guest");
        int response1 = scanner.nextInt();
        scanner.nextLine();

        if (response1 == 1) {
            try {
                Guest guest = guestSessionBean.getGuestByUsername(doRegister());
                Long reservationId = reservationSessionBean.createReservation(reservation, guest.getGuestId());
                System.out.println("Reservation made with reservation id " + reservationId);
                return;
            } catch (GuestNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
                System.out.println("Input Again");
            }
        } else {
            while (true) {
                System.out.print("Guest Username > ");
                String username = scanner.nextLine().trim();

                try {
                    Guest guest = guestSessionBean.getGuestByUsername(username);
                    Long reservationId = reservationSessionBean.createReservation(reservation, guest.getGuestId());
                    System.out.println("Reservation made with reservation id " + reservationId);
                    return;
                } catch (GuestNotFoundException ex) {
                    System.out.println("Error: " + ex.getMessage());
                    System.out.println("Input Again");
                }
            }
        }

    }

    private String doRegister() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.println("*Register*");
        System.out.println("Please enter your name > ");
        String name = scanner.nextLine().trim();

        String username = "";
        String userPattern = "^[A-Za-z0-9]*$";
        while (username.equals("")) {
            System.out.print("Please enter your username > ");
            username = scanner.nextLine().trim();
            if (username.length() < 5) {
                System.out.println("Error: username is too short, it must have a min of 5 characters!");
                username = "";
            }
            if (username.length() > 20) {
                System.out.println("Error: username is too long, it must have a max of 20 characters!");
                username = "";
            }
            if (!Pattern.matches(userPattern, username)) {
                System.out.println("Error: Username invalid");
                username = "";
            }
        }

        String password = "";
        while (password.equals("")) {
            System.out.print("Please enter your password > ");
            password = scanner.nextLine().trim();
            if (password.length() < 5) {
                System.out.println("Error: password is too short, it must have a min of 5 characters!");
                password = "";
            }
            if (password.length() > 20) {
                System.out.println("Error: password is too long, it must have a max of 20 characters!");
                password = "";
            }
        }

        System.out.print("Please enter your email >");
        String email = scanner.nextLine().trim();

        String number;
        String phonePattern = "\\d+";
        System.out.print("Please enter your phone number >");
        while (true) {
            number = scanner.nextLine().trim();
            if (Pattern.matches(phonePattern, number)) {
                break;
            } else {
                System.out.println("Invalid phone number. Please try again.");
                System.out.print("> ");
            }
        }

        String passportNum;
        String passportPattern = "^[A-Z0-9]{9}$";
        System.out.print("Please enter your passport number > ");
        while (true) {
            passportNum = scanner.nextLine().trim();
            if (Pattern.matches(passportPattern, passportNum)) {
                break;
            } else {
                System.out.println("Invalid passport number. Please try again.");
                System.out.print("> ");
            }
        }

        Guest newGuest = new Guest(name, username, password, email, number, passportNum);
        try {
            Guest g = guestSessionBean.createNewGuest(newGuest);
            System.out.println("Your account has been created successfully!");
        } catch (GuestExistsException ex) {
            System.out.println("You have an existing account with us, please log in instead");
        }

        return username;
    }

    private void doCheckInGuest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*Check In Guest*");
        System.out.print("Guest's Passport Number > ");
        String passportNumber = scanner.nextLine().trim();

        try {
            Guest guest = guestSessionBean.getGuestByPassportNumber(passportNumber);
            LocalDate currentDate = LocalDate.now();

            List<Reservation> reservations = guest.getReservations();
            List<Reservation> currentDayReservations = new ArrayList<Reservation>();
            reservations.stream().filter(r -> r.overlaps(currentDate)).forEach(r -> currentDayReservations.add(r));

            for (int i = 0; i < currentDayReservations.size(); i++) {
                Reservation reservation = currentDayReservations.get(i);
                System.out.println((i + 1) + ": Reservation Id: " + reservation.getReservationId() + " , Checked In: " + reservation.isCheckedIn() + " , Number Of Rooms: " + reservation.getNumberOfRooms() + " , Period of Stay: " + reservation.getCheckInDate() + " to " + reservation.getCheckOutDate());
            }
            int exit = currentDayReservations.size() + 1;
            System.out.println(exit + ": Exit");

            int response = scanner.nextInt();
            scanner.nextLine();

            if (response == exit) {
                return;
            }

            Reservation reservation = currentDayReservations.get(response - 1);
            try {
                checkInOutSessionBean.checkIn(reservation.getReservationId());
                System.out.println("Rooms Allocated:");
                List<Room> rooms = reservation.getGivenRooms();

                for (Room room : rooms) {
                    System.out.println("    " + room.getRoomNumber());
                }
            } catch (CheckInException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        } catch (GuestNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }

    private void doCheckOutGuest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*Check Out Guest*");
        System.out.print("Guest's Passport Number > ");
        String passportNumber = scanner.nextLine().trim();

        try {
            Guest guest = guestSessionBean.getGuestByPassportNumber(passportNumber);

            List<Reservation> reservations = guest.getReservations();
            List<Reservation> checkedInReservations = new ArrayList<Reservation>();
            reservations.stream().filter(r -> r.isCheckedIn()).forEach(r -> checkedInReservations.add(r));

            for (int i = 0; i < checkedInReservations.size(); i++) {
                Reservation reservation = checkedInReservations.get(i);
                System.out.println((i + 1) + ": Reservation Id: " + reservation.getReservationId() + " , Checked In: " + reservation.isCheckedIn() + " , Number Of Rooms: " + reservation.getNumberOfRooms() + " , Period of Stay: " + reservation.getCheckInDate() + " to " + reservation.getCheckOutDate());
            }
            int exit = checkedInReservations.size() + 1;
            System.out.println(exit + ": Exit");

            int response = scanner.nextInt();
            scanner.nextLine();

            if (response == exit) {
                return;
            }

            Reservation reservation = checkedInReservations.get(response - 1);
            try {
                checkInOutSessionBean.checkOut(reservation.getReservationId());
                System.out.println("Guest has checked Out");
            } catch (CheckOutException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        } catch (GuestNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    ///////////ALLOCATE ROOM TO CURRENT DAY RESERVATION/////////////
    private void doAllocateRoomToCurrentDayReservation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*Allocate Room To Current Date Reservation*");
        String date = "";
        LocalDate currentDate = null;

        while (currentDate == null) {
            System.out.print("Current Date > ");
            date = scanner.nextLine().trim();
            try {
                currentDate = LocalDate.parse(date);
            } catch (DateTimeParseException ex) {
                System.out.println("Error: Invalid date. Please enter check-in date again");
            }
        }

        roomAllocationSessionBean.allocateDailyReservation(currentDate);
        System.out.println("Allocated Rooms for " + date);
    }
}
