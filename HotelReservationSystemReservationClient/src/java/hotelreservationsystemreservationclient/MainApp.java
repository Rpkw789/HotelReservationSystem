/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomAvailabilitySessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Guest;
import java.util.Scanner;
import util.exception.GuestExistsException;
import util.exception.GuestNotFoundException;
import util.exception.InvalidCredentialException;

/**
 *
 * @author taniafoo
 */
public class MainApp {

    private RoomTypeSessionBeanRemote roomTypeSessionBean;
    private RoomSessionBeanRemote roomSessionBean;
    private ReservationSessionBeanRemote reservationSessionBean;
    private GuestSessionBeanRemote guestSessionBean;
    private RoomAvailabilitySessionBeanRemote roomAvailabilitySessionBean;

    private Guest guest;
    private GuestModule guestModule;

    public MainApp(RoomTypeSessionBeanRemote roomTypeSessionBean, RoomSessionBeanRemote roomSessionBean, ReservationSessionBeanRemote reservationSessionBean, GuestSessionBeanRemote guestSessionBean, RoomAvailabilitySessionBeanRemote roomAvailabilitySessionBean) {
        this.roomTypeSessionBean = roomTypeSessionBean;
        this.roomSessionBean = roomSessionBean;
        this.reservationSessionBean = reservationSessionBean;
        this.guestSessionBean = guestSessionBean;
        this.roomAvailabilitySessionBean = roomAvailabilitySessionBean;
    }

    public void runApp() {
        while (true) {
            Scanner sc = new Scanner(System.in);

            System.out.println("***Welcome To Hotel Reservation System - Reservation Client***");
            System.out.println("1: Log-in");
            System.out.println("2: Register");
            System.out.println("3: Exist");

            int response = sc.nextInt();

            if (response == 1) {
                try {
                    doLogin();
                    guestModule = new GuestModule(roomTypeSessionBean, roomSessionBean, reservationSessionBean, guestSessionBean, guest, roomAvailabilitySessionBean);
                    guestModule.mainMenu();
                } catch (InvalidCredentialException ex) {
                    ex.getMessage();
                }
            } else if (response == 2) {
                doRegister();
            } else if (response ==3) {
                return;
            } 
            else {
                 System.out.println();
                System.out.println("Error: Enter input again");
                System.out.println();
            }
        }
    }

    private void doLogin() throws InvalidCredentialException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.println("*Log-In*");
        System.out.print("Username > ");
        String username = scanner.nextLine().trim();
        System.out.print("Password > ");
        String password = scanner.nextLine().trim();

        try {
            guest = guestSessionBean.getGuestByUsername(username);
            if (!guest.getPassword().equals(password)) {
                throw new InvalidCredentialException("Wrong password!");
            }
        } catch (GuestNotFoundException ex) {
            System.out.println("You do not have a guest account with us, please register instead");
        }
    }

    private void doRegister() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.println("*Register*");
        System.out.println("Please enter your name");
        System.out.print("> ");
        String name = scanner.nextLine().trim();
        System.out.println("Please enter your username");
        System.out.print("> ");
        String username = scanner.nextLine().trim();
        System.out.println("Please enter your password");
        System.out.print("> ");
        String password = scanner.nextLine().trim();
        System.out.println("Please enter your email");
        System.out.print("> ");
        String email = scanner.nextLine().trim();
        System.out.println("Please enter your phone number");
        System.out.print("> ");
        String number = scanner.nextLine().trim();
        System.out.println("Please enter your passport number");
        System.out.print("> ");
        String passportNum = scanner.nextLine().trim();

        Guest newGuest = new Guest(name, username, password, email, number, passportNum);
        try {
            Guest g = guestSessionBean.createNewGuest(newGuest);
            System.out.println("Your account has been created successfully!");
            guest = g;
        } catch (GuestExistsException ex) {
            System.out.println("You have an existing account with us, please log in instead");
        }
    }
}
