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
import java.util.regex.Pattern;
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
            System.out.println("3: Exit");

            int response = sc.nextInt();

            if (response == 1) {
                try {
                    doLogin();
                    guestModule = new GuestModule(roomTypeSessionBean, roomSessionBean, reservationSessionBean, guestSessionBean, guest, roomAvailabilitySessionBean);
                    guestModule.mainMenu();
                } catch (InvalidCredentialException ex) {
                    System.out.println(ex.getMessage());
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
            } else {
                System.out.println("Login successful!");
            }
        } catch (GuestNotFoundException ex) {
            throw new InvalidCredentialException("You do not have a guest account with us, please register instead");
        }
    }

    private void doRegister() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.println("*Register*");
        System.out.println("Please enter your name > ");
        String name = scanner.nextLine().trim();
        
        String username = "";
        String userPattern ="^[A-Za-z0-9]*$";
        while (username.equals("")){
            System.out.print("Please enter your username > ");
            username=scanner.nextLine().trim();
            if (username.length()<5){
                System.out.println("Error: username is too short, it must have a min of 5 characters!");
                username="";
            }
            if (username.length()>20){
                System.out.println("Error: username is too long, it must have a max of 20 characters!");
                username="";
            }
            if(!Pattern.matches(userPattern, username)){
                System.out.println("Error: Username invalid");
                username="";
            }
        }
        
        String password="";
        while (password.equals("")){
            System.out.print("Please enter your password > ");
            password=scanner.nextLine().trim();
            if (password.length()<5){
                System.out.println("Error: password is too short, it must have a min of 5 characters!");
                password="";
            } 
            if (password.length()>20){
                System.out.println("Error: password is too long, it must have a max of 20 characters!");
                password="";
            }
        }
        
        System.out.print("Please enter your email > ");
        String email = scanner.nextLine().trim();
        
        String number;
        String phonePattern = "\\d+"; 
        System.out.print("Please enter your phone number > ");
        while (true){
            number=scanner.nextLine().trim();
            if (Pattern.matches(phonePattern, number)){
                break;
            } else {
                System.out.println("Invalid phone number. Please try again.");
                System.out.print("> ");
            }
        }
        
        String passportNum;
        String passportPattern = "^[A-Z0-9]{9}$";
        System.out.print("Please enter your passport number > ");
        while (true){
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
            guest = g;
        } catch (GuestExistsException ex) {
            System.out.println("You have an existing account with us, please log in instead");
        }
    }
}
