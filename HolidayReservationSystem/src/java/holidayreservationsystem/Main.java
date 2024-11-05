/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package holidayreservationsystem;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomAvailabilitySessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author ranen
 */
public class Main {

    @EJB
    private static GuestSessionBeanRemote guestSessionBean;
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBean;
    @EJB
    private static PartnerSessionBeanRemote partnerSessionBean;
    @EJB
    private static RoomAvailabilitySessionBeanRemote roomAvailabilitySessionBean;

    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(guestSessionBean, reservationSessionBean, partnerSessionBean, roomAvailabilitySessionBean);
        mainApp.run();
    }
    
}
