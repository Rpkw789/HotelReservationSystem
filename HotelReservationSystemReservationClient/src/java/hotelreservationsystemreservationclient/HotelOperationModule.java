/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.stateless.CheckInOutSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeRoleEnum;

/**
 *
 * @author ranen
 */
public class HotelOperationModule {

    private CheckInOutSessionBeanRemote checkInOutSessionBean;
    private RoomTypeSessionBeanRemote roomTypeSessionBean;
    private ReservationSessionBeanRemote reservationSessionBean;
    private GuestSessionBeanRemote guestSessionBean;

    private Employee employee;

    private List<InputNumberRolePair> inputNumberRolePairList;

    public HotelOperationModule(CheckInOutSessionBeanRemote checkInOutSessionBean, RoomTypeSessionBeanRemote roomTypeSessionBean, ReservationSessionBeanRemote reservationSessionBean, GuestSessionBeanRemote guestSessionBean, Employee employee) {
        this.checkInOutSessionBean = checkInOutSessionBean;
        this.roomTypeSessionBean = roomTypeSessionBean;
        this.reservationSessionBean = reservationSessionBean;
        this.guestSessionBean = guestSessionBean;
        this.employee = employee;

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
        System.out.println("doCreateNewRoomType");
    }
    
    private void doUpdateRoomType() {
        System.out.println("doUpdateRoomType");
    }
    
    private void doDeleteRoomType() {
        System.out.println("doDeleteRoomType");
    }
    
    private void doViewAllRoomTypes() {
        System.out.println("doViewAllRoomTypes");
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
