/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.DateTimeParseException;
import entity.Guest;
import entity.Reservation;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
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

    public GuestModule(RoomTypeSessionBeanRemote roomTypeSessionBean, RoomSessionBeanRemote roomSessionBean, ReservationSessionBeanRemote reservationSessionBean, GuestSessionBeanRemote guestSessionBean, Guest guest) {
        this.roomTypeSessionBean = roomTypeSessionBean;
        this.roomSessionBean = roomSessionBean;
        this.reservationSessionBean = reservationSessionBean;
        this.guestSessionBean = guestSessionBean;
        this.guest = guest;
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
        Scanner sc = new Scanner(System.in);
        System.out.println("");
        System.out.println("*Reserve a room*");
        System.out.println("Enter a check-in date (yyyy-MM-dd):");
        String dateInput = sc.nextLine().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Parse the string to a LocalDate object
        LocalDate checkInDate = LocalDate.parse(dateInput, formatter);

        System.out.println("Enter a check-in date (yyyy-MM-dd):");
        dateInput = sc.nextLine().trim();
        // Parse the string to a LocalDate object
        LocalDate checkOutDate = LocalDate.parse(dateInput, formatter);
        
        

        
        doViewAllReservations();
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
