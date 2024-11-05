/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package holidayreservationsystem;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomAvailabilitySessionBeanRemote;
import entity.Employee;
import entity.Partner;
import java.util.Scanner;
import util.exception.InvalidCredentialException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author ranen
 */
public class MainApp {

    private GuestSessionBeanRemote guestSessionBean;
    private ReservationSessionBeanRemote reservationSessionBean;
    private PartnerSessionBeanRemote partnerSessionBean;
    private RoomAvailabilitySessionBeanRemote roomAvailabilitySessionBean;
    
    private Partner partner;
    
    MainApp(GuestSessionBeanRemote guestSessionBean, ReservationSessionBeanRemote reservationSessionBean, PartnerSessionBeanRemote partnerSessionBean, RoomAvailabilitySessionBeanRemote roomAvailabilitySessionBean) {
        this.guestSessionBean = guestSessionBean;
        this.reservationSessionBean = reservationSessionBean;
        this.partnerSessionBean = partnerSessionBean;
        this.roomAvailabilitySessionBean = roomAvailabilitySessionBean;
    }
    
    public void run() {
        while (true) {
            Scanner scanner = new Scanner(System.in);

            System.out.println();
            System.out.println("***Welcome To Holiday Reservation System***");
            System.out.println("1: Log-in");
            System.out.println("2: Exit");

            int response = scanner.nextInt();
            if (response == 1) {
                try {
                    doLogIn();
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
    
    private void doLogIn() throws InvalidCredentialException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.println("*Log-In*");
        System.out.print("Username > ");
        String username = scanner.nextLine().trim();
        System.out.print("Password > ");
        String password = scanner.nextLine().trim();

        try {
            Partner logInPartner = partnerSessionBean.getPartnerByUsername(username);
            if (!logInPartner.getPassword().equals(password)) {
                throw new InvalidCredentialException("Wrong password");
            }
            partner = logInPartner;
        } catch (PartnerNotFoundException ex) {
            throw new InvalidCredentialException(ex.getMessage());
        }
    }

    private void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("");
            System.out.println("***Holiday Reservation System***");
            System.out.println("You are logged in as " + partner.getOrganisationName());
            System.out.println("");
            System.out.println("1: Search & Reserve Room");
            System.out.println("2: View Specific Reservation");
            System.out.println("3: View All Reservations");
            System.out.println("4: Exit");

            int response = scanner.nextInt();
            if (response == 1) {
                doSearchAndReserveRoom();
            } else if (response == 2) {
                doViewSpecificReservation();
            } else if (response == 3) {
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
    
    private void doSearchAndReserveRoom() {
        
    }
    
    private void doViewSpecificReservation() {
        
    }
    
    private void doViewAllReservations() {
        
    }
    
}
