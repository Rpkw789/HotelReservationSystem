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
import entity.Rate;
import entity.Reservation;
import entity.RoomType;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javafx.util.Pair;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author taniafoo
 */
public class GuestModule {

    private RoomTypeSessionBeanRemote roomTypeSessionBean;
    private RoomSessionBeanRemote roomSessionBean;
    private ReservationSessionBeanRemote reservationSessionBean;
    private GuestSessionBeanRemote guestSessionBean;
    private Guest guest;
    private RoomAvailabilitySessionBeanRemote roomAvailabilitySessionBean;

    public GuestModule(RoomTypeSessionBeanRemote roomTypeSessionBean, RoomSessionBeanRemote roomSessionBean, ReservationSessionBeanRemote reservationSessionBean, GuestSessionBeanRemote guestSessionBean, Guest guest, RoomAvailabilitySessionBeanRemote roomAvailabilitySessionBean) {
        this.roomTypeSessionBean = roomTypeSessionBean;
        this.roomSessionBean = roomSessionBean;
        this.reservationSessionBean = reservationSessionBean;
        this.guestSessionBean = guestSessionBean;
        this.guest = guest;
        this.roomAvailabilitySessionBean = roomAvailabilitySessionBean;
    }

    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("");
            System.out.println("***Guest Module***");
            System.out.println("You are logged in as " + guest.getUsername());
            System.out.println("");
            System.out.println("1: Search and reserve hotel room");
            System.out.println("2: View my reservation details");
            System.out.println("3: View all my reservations");
            System.out.println("4: Exit");
            int response = scanner.nextInt();

            if (response == 1) {
                doSearch();
            } else if (response == 2) {
                doViewReservationDetails();
            } else if (response == 3) {
                System.out.println("");
                System.out.println("**View all reservations**");
                doViewAllReservations();
            } else if (response == 4) {
                return;
            } else {
                System.out.println();
                System.out.println("Error: Enter input again");
                System.out.println();
            }
        }
    }

    private void doSearch() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
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
                if (!endDate.isAfter(startDate)){
                    System.out.println("Error: Check-out date must be after the check-in date");
                    endDate =null;
                }
            } catch (DateTimeParseException ex) {
                System.out.println("Error: Invalid date");
            }
        }

        List<Pair<RoomType, Integer>> roomTypesAndNumber = roomAvailabilitySessionBean.getAvailableRoomTypeAndNumber(startDate, endDate);
        System.out.println("Available Room Types and Quantity");
        for (int i = 0; i < roomTypesAndNumber.size(); i++) {
            Pair<RoomType, Integer> pair = roomTypesAndNumber.get(i);
            List<Rate> rates = roomAvailabilitySessionBean.getRateByRoomType(startDate, endDate, pair.getKey().getRoomTypeId());

            double cost = 0;
            for (Rate rate : rates) {
                cost += rate.getRatePerNight();
            }

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
        Pair<RoomType, Integer> pair = roomTypesAndNumber.get(response - 1);
        List<Rate> rates = roomAvailabilitySessionBean.getRateByRoomType(startDate, endDate, pair.getKey().getRoomTypeId());

        double cost = 0;
        for (Rate rate : rates) {
            cost += rate.getRatePerNight();
        }

        Set<Rate> uniqueRates = new HashSet<Rate>();
        rates.stream().forEach(r -> uniqueRates.add(r));

        System.out.print("Number of Rooms > ");
        int numberOfRooms = scanner.nextInt();
        scanner.nextLine();

        Reservation reservation = new Reservation(numberOfRooms, cost * numberOfRooms, false, startDate, endDate, pair.getKey());
        reservation.setRates(rates);

        Long reservationId = reservationSessionBean.createReservation(reservation, guest.getGuestId());

        System.out.println("Reservation made with reservation id " + reservationId);

    }

    private void doViewReservationDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.println("");
        System.out.println("**View rerservation details**");
        System.out.println("Choose which reservation to view. Please only enter the reservation number");
        doViewAllReservations();
        System.out.println("");
        System.out.println("Press 'E' if you would like to exit");
        System.out.print("> ");
        String response = "";

        while (!response.equals("E")) {
            response = sc.nextLine().trim();
            if (response.equals("E")){
                break;
            } 
            
            try {
                Long id = Long.parseLong(response);
                Reservation r = reservationSessionBean.getReservationById(id);
                System.out.println("Here are the details of your reservation:");
                System.out.println("Fee:" + r.getFee());
                System.out.println("Check in date: " + r.getCheckInDate());
                System.out.println("Check out date: " + r.getCheckOutDate());
                System.out.println("Number of rooms: " + r.getNumberOfRooms());
                System.out.println("Guest name: " + r.getGuest().getName());
                System.out.println("Room type: " + r.getRoomType().getName());
                System.out.print("> ");
            } catch(NumberFormatException ex){
                System.out.println("Invalid input, please enter a valid reservation number or 'E' to exit");
            }
            catch (ReservationNotFoundException ex) {
                System.out.println("No reservation found");
            }
        }

    }

    private void doViewAllReservations() {
        System.out.println("");

        try {
            String passportNum = guest.getPassportNumber();
            List<Reservation> reservations = reservationSessionBean.getReservationByPassportNumber(passportNum);
            for (Reservation r : reservations) {
                System.out.println("> Reservation " + r.getReservationId() + " with check in date of " + r.getCheckInDate() + " and check out date of " + r.getCheckOutDate());
            }
        } catch (ReservationNotFoundException ex) {
            System.out.println("No Reservation found!");
        }

    }
}
