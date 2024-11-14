/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.RoomAllocationExceptionRecord;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ranen
 */
@Local
public interface RoomAllocationSessionBeanLocal {

    public void allocateWalkInRoom(RoomType roomType, Reservation reservation);

    public void allocateDailyReservation(LocalDate currentDate);

    public List<RoomAllocationExceptionRecord> getRoomAllocationExceptionRecord(LocalDate date);
    
}
