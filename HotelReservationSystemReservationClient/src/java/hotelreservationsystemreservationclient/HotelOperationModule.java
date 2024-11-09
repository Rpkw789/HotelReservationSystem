/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.singleton.DailyRoomAllocationSessionBeanRemote;
import ejb.session.stateless.CheckInOutSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomAllocationSessionBeanRemote;
import ejb.session.stateless.RoomAvailabilitySessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import entity.Room;
import entity.RoomType;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeRoleEnum;
import util.enumeration.RoomAvailabilityStatusEnum;
import util.enumeration.RoomOperationalStatusEnum;
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

    private Employee employee;

    private List<InputNumberRolePair> inputNumberRolePairList;

    public HotelOperationModule(CheckInOutSessionBeanRemote checkInOutSessionBean, RoomTypeSessionBeanRemote roomTypeSessionBean, ReservationSessionBeanRemote reservationSessionBean, GuestSessionBeanRemote guestSessionBean, Employee employee, DailyRoomAllocationSessionBeanRemote dailyRoomAllocationSessionBean, RoomAllocationSessionBeanRemote roomAllocationSessionBean, RoomAvailabilitySessionBeanRemote roomAvailabilitySessionBean, RoomSessionBeanRemote roomSessionBean) {
        this.checkInOutSessionBean = checkInOutSessionBean;
        this.roomTypeSessionBean = roomTypeSessionBean;
        this.reservationSessionBean = reservationSessionBean;
        this.guestSessionBean = guestSessionBean;
        this.employee = employee;
        this.dailyRoomAllocationSessionBean = dailyRoomAllocationSessionBean;
        this.roomAllocationSessionBean = roomAllocationSessionBean;
        this.roomAvailabilitySessionBean = roomAvailabilitySessionBean;
        this.roomSessionBean = roomSessionBean;

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
        }
        for (InputNumberRolePair pair : inputNumberRolePairList) {
            if (pair.getNumber() == response) {
                EmployeeRoleEnum role = pair.getRole();

                if (role == EmployeeRoleEnum.OPERATION_MANAGER) {
                    generalOperationMenu();
                } else if (role == EmployeeRoleEnum.SALES_MANAGER) {
                    salesOperationMenu();
                } else if (role == EmployeeRoleEnum.GUEST_RELATION_OFFICER) {
                    guestOperationMenu();
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
        System.out.println("");
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
            System.out.println("7: Exit");

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
                        if (roomType.getRooms().isEmpty()) {
                            System.out.println(roomType.getRooms());
                            roomTypeSessionBean.deleteRoomType(roomType.getRoomTypeId());
                        } else {
                            roomType.setEnabled(false);
                            roomTypeSessionBean.updateRoomType(roomType);
                        }

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
                            room.setOperationalStatus(RoomOperationalStatusEnum.ENABLED);
                        } else if (availability == 2) {
                            room.setOperationalStatus(RoomOperationalStatusEnum.DISABLED);
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
                    // TO-DO : Disable feature
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
        System.out.println("doViewRoomAllocationExceptionReport");
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
        System.out.println("doCreateNewRoomRate");
    }

    private void doUpdateRoomRate() {
        System.out.println("doUpdateRoomRate");
    }

    private void doDeletRoomRate() {
        System.out.println("doDeleteRoomRate");
    }

    private void doViewAllRoomRates() {
        System.out.println("doViewAlRoomRates");
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
        System.out.println("doWalkInSearchReserveRoom");
    }

    private void doCheckInGuest() {
        System.out.println("doCheckInGuest");
    }

    private void doCheckOutGuest() {
        System.out.println("doCheckOutGuest");
    }

    ///////////ALLOCATE ROOM TO CURRENT DAY RESERVATION/////////////
    private void doAllocateRoomToCurrentDayReservation() {
        System.out.println("TO-DO: doAllocateRoomToCurrentDayReservation");
    }
}
