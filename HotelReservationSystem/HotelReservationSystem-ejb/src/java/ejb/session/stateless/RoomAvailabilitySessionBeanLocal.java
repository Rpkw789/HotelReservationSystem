/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Rate;
import entity.RoomType;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Local;

/**
 *
 * @author ranen
 */
@Local
public interface RoomAvailabilitySessionBeanLocal {

    public List<Pair<RoomType, Integer>> getAvailableRoomTypeAndNumber(LocalDate checkInDate, LocalDate checkOutDate);

    public double getCostWalkIn(LocalDate checkInDate, LocalDate checkOutDate, Long roomTypeId);

    public List<Rate> getRateByRoomTypeWalkIn(Long roomTypeId);
    
}
