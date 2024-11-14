/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationseclient;

import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;
import ws.partner.InvalidCredentialException_Exception;
import ws.partner.Partner;
import ws.partner.PartnerRoleEnum;
import ws.partner.PartnerWebService_Service;
import ws.partner.Reservation;
import ws.partner.ReservationNotFoundException_Exception;
import ws.partner.RoomType;

/**
 *
 * @author taniafoo
 */
public class MainApp {

    PartnerWebService_Service service;
    Partner p;

    public MainApp(PartnerWebService_Service service) {
        this.service = service;
    }

    public void runApp() {
        Scanner sc = new Scanner(System.in);
        System.out.println("**SOAP Holiday.com client**");
        System.out.println("Select to continue:");
        System.out.println("1. Login");
        System.out.println("2. exit");

        int response = sc.nextInt();

        if (response == 1) {
            try {
                doLogin();
                mainMenu();
            } catch (InvalidCredentialException_Exception ex) {
                System.out.println(ex.getMessage());
            }

        } else if (response == 2) {
            return;
        } else {
            System.out.println("Invalid input, please try again");
        }
    }

    private void doLogin() throws InvalidCredentialException_Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter username > ");

        String username = sc.nextLine().trim();
        System.out.print("Enter password > ");

        String password = sc.nextLine().trim();

        p = service.getPartnerWebServicePort().doLogin(username, password);
        System.out.println("Login successful!");

    }

    private void mainMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("You are logged in with " + p.getRole() + " rights!");
        System.out.println("Select to continue:");
        if (p.getRole() == PartnerRoleEnum.PARTNER_EMPLOYEE) {
            System.out.println("1. Search room");
            System.out.println("2. exit");

            int response = sc.nextInt();

            if (response == 1) {
                doSearchRoom();
            } else if (response == 2) {
                return;
            } else {
                System.out.println("Invalid input, please try again");
            }
        } else {
            System.out.println("1. Search and reserve room");
            System.out.println("2. View reservation details for customer");
            System.out.println("3. View all reservations under Holiday.com");
            System.out.println("4. Exit");
            int response = sc.nextInt();

            if (response == 1) {
                doSearchAndReserve();
            } else if (response == 2) {
                doViewDetails();
            } else if (response == 3) {
                doViewAllReservations();
            } else if (response == 4) {
                return;
            } else {
                System.out.println("Invalid input, please try again");
            }
        }

    }

    private void doSearchRoom() {
        System.out.println("**Search room**");
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter check-in date (YYYY-MM-DD");
        String checkInDate = sc.nextLine().trim();
        System.out.println("Please enter check-out date (YYYY-MM-DD");
        String checkOutDate = sc.nextLine().trim();

        //List<Pair<RoomType, Integer>> listOfRoomTypesAndNumbers = service.getPartnerWebServicePort().getAvailableRoomTypeAndNumber(checkInDate, checkOutDate);
    }

    private void doSearchAndReserve() {
        System.out.println("**Reserve room**");
        Scanner sc = new Scanner(System.in);

        System.out.print("Please enter check-in date (YYYY-MM-DD >");
        String checkInDate = sc.nextLine().trim();
        System.out.print("Please enter check-out date (YYYY-MM-DD 0 >");
        String checkOutDate = sc.nextLine().trim();

        System.out.print("Please enter username of guest >");
        String username = sc.nextLine().trim();

        System.out.print("Please enter password of guest >");
        String password = sc.nextLine().trim();

        //Long partnerId = service.getPartnerWebServicePort().logInPartnerAccount(username, password);
    }

    private void doViewDetails() {
        System.out.println("**View partner reservation details**");
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose one from the following reservation to view its details. Please only type in the reservation's number");

        doViewAllReservations();
        System.out.print(">");
        System.out.println("Press 'E' if you would like to exit");
        String response = sc.nextLine().trim();

        while (!response.equals("E")) {
            Long id = Long.parseLong(response);
            try {
                Reservation r = service.getPartnerWebServicePort().getReservationById(id);
                System.out.println("Reservation fee: " + r.getFee());
                System.out.println("Check-in Date: " + r.getCheckInDate());
                System.out.println("Check-out date: " + r.getCheckOutDate());
                System.out.println("Checked in: " + r.isCheckedIn());
                System.out.println("Number of rooms: " + r.getNumberOfRooms());
                System.out.println("Reservation fee: " + r.getFee());
                System.out.println("Guest name: " + r.getGuest().getName());
                System.out.println("Room type: " + r.getRoomType());
            } catch (ReservationNotFoundException_Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return;
    }

    private void doViewAllReservations() {
        System.out.println("**View all reservations**");
        List<Reservation> reservations = service.getPartnerWebServicePort().retrieveAllReservations(p);
        for (Reservation r : reservations) {
            System.out.println("Reservation " + r.getReservationId() + " with check-in date of " + r.getCheckInDate() + " and check out date of " + r.getCheckOutDate());
        }
    }
}
