/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.RoomType;
import java.time.LocalDate;
import javax.ejb.Local;

/**
 *
 * @author ranen
 */
@Local
public interface RoomAllocationSessionBeanLocal {

    public void allocateWalkInRoom(RoomType roomType, Reservation reservation);

    public void allocateDailyReservation(LocalDate currentDate);
    
}
