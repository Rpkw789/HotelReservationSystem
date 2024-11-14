/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationseclient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import ws.partner.Guest;
import ws.partner.InvalidCredentialException_Exception;
import ws.partner.Partner;
import ws.partner.PartnerAccountExistsException_Exception;
import ws.partner.PartnerRoleEnum;
import ws.partner.PartnerWebService_Service;
import ws.partner.Rate;
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
        while (true) {
            System.out.println("**SOAP Holiday.com client**");
            System.out.println("Select to continue:");
            System.out.println("1. Login");
            System.out.println("2. Exit");

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
        while (true) {
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
                System.out.println("2. View Specific Reservation Details");
                System.out.println("3. View all reservations under " + p.getOrganisationName());
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

    }

    private void doSearchRoom() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*Search*");
        System.out.print("Enter Check-In Date (YYYY-MM-DD) > ");
        String checkInDate = scanner.nextLine().trim();
        System.out.print("Enter Check-Out Date (YYYY-MM-DD) > ");
        String checkOutDate = scanner.nextLine().trim();
        

        List<RoomType> roomTypes = service.getPartnerWebServicePort().getRoomTypes();
        System.out.println("Available Room Types and Quantity");
        for (int i = 0; i < roomTypes.size(); i++) {
            RoomType roomType = roomTypes.get(i);
            List<Rate> rates = service.getPartnerWebServicePort().getRatesUsed(checkInDate, checkOutDate, roomType.getRoomTypeId());
            double cost = 0;
            for (Rate rate : rates) {
                cost += rate.getRatePerNight();
            }
            int quantity = service.getPartnerWebServicePort().getAvailableNumber(checkInDate, checkOutDate, roomType.getRoomTypeId());
            System.out.println((i + 1) + ": Room Type = " + roomType.getName() + ", Quantity = " + quantity + ", Cost Per Room = " + cost);
        }
    }

    private void doSearchAndReserve() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*Walk In Search & Reserve Room*");
        System.out.print("Enter Check-In Date (YYYY-MM-DD) > ");
        String checkInDate = scanner.nextLine().trim();
        System.out.print("Enter Check-Out Date (YYYY-MM-DD) > ");
        String checkOutDate = scanner.nextLine().trim();

        List<RoomType> roomTypes = service.getPartnerWebServicePort().getRoomTypes();
        System.out.println("Available Room Types and Quantity");
        for (int i = 0; i < roomTypes.size(); i++) {
            RoomType roomType = roomTypes.get(i);
            List<Rate> rates = service.getPartnerWebServicePort().getRatesUsed(checkInDate, checkOutDate, roomType.getRoomTypeId());
            double cost = 0;
            for (Rate rate : rates) {
                cost += rate.getRatePerNight();
            }
            int quantity = service.getPartnerWebServicePort().getAvailableNumber(checkInDate, checkOutDate, roomType.getRoomTypeId());
            System.out.println((i + 1) + ": Room Type = " + roomType.getName() + ", Quantity = " + quantity + ", Cost Per Room = " + cost);
        }
        int exit = roomTypes.size() + 1;
        System.out.println(exit + ": Exit");
        System.out.println("Choose Room Type");

        int response = scanner.nextInt();
        scanner.nextLine();
        if (response == exit) {
            return;
        }
        RoomType chosenRoomType = roomTypes.get(response - 1);
        List<Rate> rates = service.getPartnerWebServicePort().getRatesUsed(checkInDate, checkOutDate, chosenRoomType.getRoomTypeId());
        List<Long> ratesString = new ArrayList<Long>();
        for (Rate rate : rates) {
            ratesString.add(rate.getRateId());
        }
        double cost = 0;
        for (Rate rate : rates) {
            cost += rate.getRatePerNight();
        }
        int quantity = service.getPartnerWebServicePort().getAvailableNumber(checkInDate, checkOutDate, chosenRoomType.getRoomTypeId());
        int numberOfRooms = 0;

        while (true) {
            System.out.print("Number of Rooms > ");
            int response1 = scanner.nextInt();
            scanner.nextLine();
            if (response1 == 0) {
                return;
            }
            if (response1 <= quantity) {
                numberOfRooms = response1;
                break;
            }
            System.out.println("Insufficient Rooms Available");
        }

        System.out.println("Choose an Option:");
        System.out.println("1: Log In Guest Account");
        System.out.println("2: Register Guest Account");
        int response3 = scanner.nextInt();
        scanner.nextLine();
        if (response3 == 2) {
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
            String mobileNumber = scanner.nextLine().trim();
            System.out.println("Please enter your passport number");
            System.out.print("> ");
            String passportNumber = scanner.nextLine().trim();
            try {
                Long guestId = service.getPartnerWebServicePort().createGuestForPartner(name, username, password, email, mobileNumber, passportNumber, p.getPartnerId());
                Long reservationId = service.getPartnerWebServicePort().createReservation(numberOfRooms, cost, chosenRoomType.getRoomTypeId(), guestId, checkInDate, checkOutDate, ratesString);
                System.out.println("Reservation made with reservation id " + reservationId);
                return;
            } catch (PartnerAccountExistsException_Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                return;
            }
        } else {
            while (true) {
                System.out.print("Guest Username > ");
                String username = scanner.nextLine().trim();

                try {
                    Long guestId = service.getPartnerWebServicePort().logInPartnerAccount(username, p.getPartnerId());
                    Long reservationId = service.getPartnerWebServicePort().createReservation(numberOfRooms, cost, chosenRoomType.getRoomTypeId(), guestId, checkInDate, checkOutDate, ratesString);
                    System.out.println("Reservation made with reservation id " + reservationId);
                    return;
                } catch (InvalidCredentialException_Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                    System.out.println("Input Again");
                }
            }
        }

    }

    private void doViewDetails() {
        System.out.println("**View partner reservation details**");
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose one from the following reservation to view its details. Please only type in the reservation's number");

        doViewAllReservations();
        System.out.println("Press 'E' if you would like to exit");
        System.out.print(">");
        String response = sc.nextLine().trim();

        while (!response.equals("E")) {
            Long id = Long.parseLong(response);
            try {
                Reservation r = service.getPartnerWebServicePort().getReservationById(id);
                LocalDate checkInDate = LocalDate.parse(service.getPartnerWebServicePort().getCheckInDate(r.getReservationId()));
                LocalDate checkOutDate = LocalDate.parse(service.getPartnerWebServicePort().getCheckOutDate(r.getReservationId()));
                System.out.println("Reservation fee: " + r.getFee());
                System.out.println("Check-in Date: " + checkInDate);
                System.out.println("Check-out date: " + checkOutDate);
                System.out.println("Checked in: " + r.isCheckedIn());
                System.out.println("Number of rooms: " + r.getNumberOfRooms());
                System.out.println("Reservation fee: " + r.getFee());
                System.out.println("Guest name: " + r.getGuest().getName());
                System.out.println("Room type: " + r.getRoomType().getName());
            } catch (ReservationNotFoundException_Exception ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println("Press 'E' if you would like to exit");
            System.out.print(">");
            response = sc.nextLine().trim();
        }
        return;
    }

    private void doViewAllReservations() {
        System.out.println("**View all reservations**");
        List<Reservation> reservations = service.getPartnerWebServicePort().retrieveAllReservations(p);
        for (Reservation r : reservations) {
            try {
                LocalDate checkInDate = LocalDate.parse(service.getPartnerWebServicePort().getCheckInDate(r.getReservationId()));
                LocalDate checkOutDate = LocalDate.parse(service.getPartnerWebServicePort().getCheckOutDate(r.getReservationId()));
                System.out.println("Reservation " + r.getReservationId() + " with check-in date of " + checkInDate + " and check out date of " + checkOutDate);
            } catch (ReservationNotFoundException_Exception ex) {

            }

        }
    }
}
