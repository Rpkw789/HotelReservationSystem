/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.singleton.DailyRoomAllocationSessionBeanRemote;
import ejb.session.stateless.CheckInOutSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomAllocationSessionBeanRemote;
import ejb.session.stateless.RoomAvailabilitySessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;
import util.enumeration.EmployeeRoleEnum;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidCredentialException;

/**
 *
 * @author ranen
 */
public class MainApp {

    private CheckInOutSessionBeanRemote checkInOutSessionBean;
    private RoomTypeSessionBeanRemote roomTypeSessionBean;
    private ReservationSessionBeanRemote reservationSessionBean;
    private PartnerSessionBeanRemote partnerSessionBean;
    private GuestSessionBeanRemote guestSessionBean;
    private EmployeeSessionBeanRemote employeeSessionBean;
    private RoomAvailabilitySessionBeanRemote roomAvailabilitySessionBean;
    private DailyRoomAllocationSessionBeanRemote dailyRoomAllocationSessionBean;
    private RoomAllocationSessionBeanRemote roomAllocationSessionBean;
    private RoomSessionBeanRemote roomSessionBean;

    private Employee employee;
   
    private HotelOperationModule hotelOperationModule;
    private SystemAdministrationModule systemAdministrationModule;

    public MainApp(CheckInOutSessionBeanRemote checkInOutSessionBean, RoomTypeSessionBeanRemote roomTypeSessionBean, ReservationSessionBeanRemote reservationSessionBean, PartnerSessionBeanRemote partnerSessionBean, GuestSessionBeanRemote guestSessionBean, EmployeeSessionBeanRemote employeeSessionBean, DailyRoomAllocationSessionBeanRemote dailyRoomAllocationSessionBean, RoomAllocationSessionBeanRemote roomAllocationSessionBean, RoomAvailabilitySessionBeanRemote roomAvailabilitySessionBean, RoomSessionBeanRemote roomSessionBean) {
        this.checkInOutSessionBean = checkInOutSessionBean;
        this.roomTypeSessionBean = roomTypeSessionBean;
        this.reservationSessionBean = reservationSessionBean;
        this.partnerSessionBean = partnerSessionBean;
        this.guestSessionBean = guestSessionBean;
        this.employeeSessionBean = employeeSessionBean;
        this.dailyRoomAllocationSessionBean = dailyRoomAllocationSessionBean;
        this.roomAllocationSessionBean = roomAllocationSessionBean;
        this.roomAvailabilitySessionBean = roomAvailabilitySessionBean;
        this.roomSessionBean = roomSessionBean;
    }

    public void run() {

        while (true) {
            Scanner scanner = new Scanner(System.in);

            System.out.println();
            System.out.println("***Welcome To Hotel Reservation System - Reservation Client***");
            System.out.println("1: Log-in");
            System.out.println("2: Exit");

            int response = scanner.nextInt();
            if (response == 1) {
                try {
                    doLogIn();
                    hotelOperationModule = new HotelOperationModule(checkInOutSessionBean, roomTypeSessionBean, reservationSessionBean, guestSessionBean, employee, dailyRoomAllocationSessionBean, roomAllocationSessionBean, roomAvailabilitySessionBean, roomSessionBean);
                    systemAdministrationModule = new SystemAdministrationModule(employeeSessionBean, partnerSessionBean, employee);
                    mainMenu();
                } catch (InvalidCredentialException ex) {
                    System.out.println("");
                    System.out.println("Error: " + ex.getMessage());
                    System.out.println("");
                }

            } else if (response == 2) {
                return;
            } else {
                System.out.println();
                System.out.println("Error: Enter input again");
                System.out.println();
            }
        }

    }

    public void doLogIn() throws InvalidCredentialException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.println("*Log-In*");
        System.out.print("Username > ");
        String username = scanner.nextLine().trim();
        System.out.print("Password > ");
        String password = scanner.nextLine().trim();

        try {
            Employee logEmployee = employeeSessionBean.getEmployeeByUsername(username);
            if (!logEmployee.getPassword().equals(password)) {
                throw new InvalidCredentialException("Wrong password");
            }
            employee = logEmployee;
        } catch (EmployeeNotFoundException ex) {
            throw new InvalidCredentialException(ex.getMessage());
        }
    }

    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("");
            System.out.println("***Hotel Reservation Client Main Menu***");
            System.out.println("You are logged in as " + employee.getName() + " with " + getRights() + " Rights");
            System.out.println("");
            System.out.println("1: Hotel Operation");
            System.out.println("2: System Administration");
            System.out.println("3: Exit");

            int response = scanner.nextInt();
            if (response == 1) {
                hotelOperationModule.mainMenu();
            } else if (response == 2) {
                systemAdministrationModule.mainMenu();
            } else if (response == 3) {
                return;
            } else {
                System.out.println();
                System.out.println("Error: Enter input again");
                System.out.println();
            }
        }

    }

    private String getRights() {
        String rights = "";
        List<EmployeeRoleEnum> listOfRoles = employee.getEmployeeRoles();
        for (int i = 0; i < listOfRoles.size(); i++) {
            rights += listOfRoles.get(i);
            if (i != listOfRoles.size() - 1) {
                rights += ", ";
            }
        }
        return rights;
    }
}
