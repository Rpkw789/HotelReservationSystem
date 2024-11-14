/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationseclient;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
        System.out.println("");
        System.out.println("*Log-in*");

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
            System.out.println("");
            System.out.println("***You are logged in with " + p.getRole() + " rights!***");
            System.out.println("Select to continue:");
            if (p.getRole() == PartnerRoleEnum.PARTNER_EMPLOYEE) {
                System.out.println("1. Search room");
                System.out.println("2. Exit");

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
                    System.out.println("");
                    System.out.println("**View all reservations**");
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

        System.out.println("");
        System.out.println("*Search Room*");
        String checkInDate = "";
        LocalDate checkIn = null;
        while (checkIn == null) {
            System.out.print("Enter Check-In Date (YYYY-MM-DD) > ");
            checkInDate = scanner.nextLine().trim();
            try {
                checkIn = LocalDate.parse(checkInDate);
            } catch (DateTimeParseException ex) {
                System.out.println("Error:Invalid date");
            }
        }

        String checkOutDate = "";
        LocalDate checkout = null;
        while (checkout == null || !checkout.isAfter(checkIn)) {
            System.out.print("Enter Check-Out Date (YYYY-MM-DD) > ");
            checkOutDate = scanner.nextLine().trim();
            try {
                checkout = LocalDate.parse(checkOutDate);
                if (!checkout.isAfter(checkIn)) {
                    System.out.println("Error: Check-out date must be after check in date");
                    checkout = null;
                }
            } catch (DateTimeParseException ex) {
                System.out.println("Error:Invalid date");
            }
        }

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
        System.out.println("");
        System.out.println("*Walk In Search & Reserve Room*");
        String checkInDate = "";
        LocalDate checkIn = null;
        while (checkIn == null) {
            System.out.print("Enter Check-In Date (YYYY-MM-DD) > ");
            checkInDate = scanner.nextLine().trim();
            try {
                checkIn = LocalDate.parse(checkInDate);
            } catch (DateTimeParseException ex) {
                System.out.println("Error: Invalid date");
            }
        }

        String checkOutDate = "";
        LocalDate checkout = null;
        while (checkout == null || !checkout.isAfter(checkIn)) {
            System.out.print("Enter Check-Out Date (YYYY-MM-DD) > ");
            checkOutDate = scanner.nextLine().trim();
            try {
                checkout = LocalDate.parse(checkOutDate);
                if (!checkout.isAfter(checkIn)) {
                    System.out.println("Error: Check-out date must be after check in date");
                    checkout = null;
                }
            } catch (DateTimeParseException ex) {
                System.out.println("Error: Invalid date");
            }
        }

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
            ///NAME///
            System.out.println("");
            System.out.println("*Register*");
            System.out.print("Please enter your name > ");
            String name = scanner.nextLine().trim();

            ///USERNAME///
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

            ///PASSWORD///
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

            ///EMAIL///
            System.out.print("Please enter your email > ");
            String email = scanner.nextLine().trim();

            ///PHONE NUMBER///
            String number;
            String phonePattern = "\\d+";
            System.out.print("Please enter your phone number > ");
            while (true) {
                number = scanner.nextLine().trim();
                if (Pattern.matches(phonePattern, number)) {
                    break;
                } else {
                    System.out.println("Invalid phone number. Please try again.");
                    System.out.print("> ");
                }
            }

            ///PASSPORT///
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

            try {
                Long guestId = service.getPartnerWebServicePort().createGuestForPartner(name, username, password, email, number, passportNum, p.getPartnerId());
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
        System.out.println("");
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
        System.out.println("");
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
