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
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import entity.RoomType;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeRoleEnum;
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

    private Employee employee;

    private List<InputNumberRolePair> inputNumberRolePairList;

    public HotelOperationModule(CheckInOutSessionBeanRemote checkInOutSessionBean, RoomTypeSessionBeanRemote roomTypeSessionBean, ReservationSessionBeanRemote reservationSessionBean, GuestSessionBeanRemote guestSessionBean, Employee employee, DailyRoomAllocationSessionBeanRemote dailyRoomAllocationSessionBean, RoomAllocationSessionBeanRemote roomAllocationSessionBean, RoomAvailabilitySessionBeanRemote roomAvailabilitySessionBean) {
        this.checkInOutSessionBean = checkInOutSessionBean;
        this.roomTypeSessionBean = roomTypeSessionBean;
        this.reservationSessionBean = reservationSessionBean;
        this.guestSessionBean = guestSessionBean;
        this.employee = employee;
        this.dailyRoomAllocationSessionBean = dailyRoomAllocationSessionBean;
        this.roomAllocationSessionBean = roomAllocationSessionBean;
        this.roomAvailabilitySessionBean = roomAvailabilitySessionBean;

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
            System.out.println("Choose an attribute to ");
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
            while (true) {
                System.out.println("Remove this Room Type? (y/n)");
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
        System.out.println("doCreateNewRoom");
    }

    private void doUpdateRoom() {
        System.out.println("doUpdateRoom");
    }

    private void doDeleteRoom() {
        System.out.println("doDeleteRoom");
    }

    private void doViewAllRooms() {
        System.out.println("doViewAllRooms");
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
