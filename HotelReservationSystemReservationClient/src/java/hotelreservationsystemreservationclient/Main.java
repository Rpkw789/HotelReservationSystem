/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author ranen
 */
public class Main {

    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBean;

    @EJB
    private static RoomSessionBeanRemote roomSessionBean;

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBean;

    @EJB
    private static GuestSessionBeanRemote guestSessionBean;
    
    
    
    public static void main(String[] args) {
        MainApp mainapp = new MainApp(roomTypeSessionBean, roomSessionBean, reservationSessionBean, guestSessionBean);
        mainapp.runApp();
    }
    
}
