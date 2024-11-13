/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.RoomType;
import java.time.LocalDate;
import javax.ejb.Remote;

/**
 *
 * @author ranen
 */
@Remote
public interface RoomAllocationSessionBeanRemote {
    public void allocateWalkInRoom(RoomType roomType, Reservation reservation);
    
        public void allocateDailyReservation(LocalDate currentDate);
}
