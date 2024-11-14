/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.RoomAllocationSessionBeanLocal;
import java.time.LocalDate;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author ranen
 */
@Singleton
@Startup
public class DailyRoomAllocationSessionBean implements DailyRoomAllocationSessionBeanRemote, DailyRoomAllocationSessionBeanLocal {

    @EJB
    private RoomAllocationSessionBeanLocal roomAllocationSessionBean;

    @Schedule(hour = "2", minute = "0", second = "0", persistent = false)
    public void allocateReservedRoom() {
        System.out.println("HI");
        roomAllocationSessionBean.allocateDailyReservation(LocalDate.now());
    }
}
