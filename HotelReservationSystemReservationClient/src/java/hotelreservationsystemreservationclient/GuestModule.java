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
import entity.DateTimeParseException;
import entity.Guest;
import entity.Rate;
import entity.Reservation;
import entity.RoomType;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                doViewAllReservations();
            } else {
                System.out.println();
                System.out.println("Error: Enter input again");
                System.out.println();
            }
        }
    }

    private void doSearch() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*Walk In Search & Reserve Room*");
        System.out.print("Enter Check-In Date (YYYY-MM-DD) > ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("Enter Check-Out Date (YYYY-MM-DD) > ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine().trim());

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
        System.out.println("*View rerservation details*");
        System.out.println("Choose which reservation to view:");
        doViewAllReservations();
        System.out.print("> ");
        Long id = sc.nextLong();

        try {
            Reservation r = reservationSessionBean.getReservationById(id);
            System.out.println("Here are the details of your reservation:");
            System.out.println("Fee:" + r.getFee());
            System.out.println("Check in date:" + r.getCheckInDate());
            System.out.println("Check out date:" + r.getCheckOutDate());
            System.out.println("Number of rooms:" + r.getNumberOfRooms());
        } catch (ReservationNotFoundException ex) {
            System.out.println("No reservation found");
        }

    }

    private void doViewAllReservations() {
        System.out.println("");
        System.out.println("*View all reservations*");

        try {
            String passportNum = guest.getPassportNumber();
            List<Reservation> reservations = reservationSessionBean.getReservationByPassportNumber(passportNum);
            for (Reservation r : reservations) {
                System.out.println("> " + r.getReservationId());
            }
        } catch (ReservationNotFoundException ex) {
            System.out.println("No Reservation found!");
        }

    }
}
