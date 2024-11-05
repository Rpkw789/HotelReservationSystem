/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.stateless.CheckInOutSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author ranen
 */
public class Main {

    @EJB
    private static CheckInOutSessionBeanRemote checkInOutSessionBean;
    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBean;
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBean;
    @EJB
    private static PartnerSessionBeanRemote partnerSessionBean;
    @EJB
    private static GuestSessionBeanRemote guestSessionBean;
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBean;

    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(checkInOutSessionBean, roomTypeSessionBean, reservationSessionBean, partnerSessionBean, guestSessionBean, employeeSessionBean);
        mainApp.run();
    }
    
}
