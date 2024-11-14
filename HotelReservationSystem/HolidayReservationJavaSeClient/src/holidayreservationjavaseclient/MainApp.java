/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package holidayreservationjavaseclient;

import java.util.Scanner;
import ws.partner.Partner;
import ws.partner.PartnerNotFoundException_Exception;
import ws.partner.PartnerWebService_Service;

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
    
    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("**SOAP Holiday.com client**");
        System.out.println("1. Login");
        System.out.println("2. Exit");
        System.out.println("");

        int response = sc.nextInt();

        if (response == 1) {
            doLogin();
            mainMenu();
        } else if (response == 2) {
            return;
        } else {
            System.out.println("Invalid input, please try again");
        }
    }
    
    private void doLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username");
        String username = sc.nextLine().trim();
        System.out.println("Enter password");
        String password = sc.nextLine().trim();
        try {
            p = service.getPartnerWebServicePort().getPartnerByUsername(username);
            if(p.getPassword().equals(password)){
                System.out.println("Login successful!");
            } else {
                //throw new InvalidCredentialException("Wrong password!");
            }
        } catch (PartnerNotFoundException_Exception e) {
            System.out.println("Partner not found!");
        }
        
    }

    
    private void mainMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("**You are currently logged in**");
        System.out.println("1. Partner reserve room");
        System.out.println("2. View reservation details");
        System.out.println("3. View all reservations");
        System.out.println("4. Exit");
        System.out.println("");
        
        int response = sc.nextInt();
        if (response == 1) {
            
        } else if (response == 2) {
            
        } else if (response == 3) {
            //List<Reservation> reservations = service.getPartnerWebServicePort().retrieveAllReservations(partner);
        }
        
    }
}
