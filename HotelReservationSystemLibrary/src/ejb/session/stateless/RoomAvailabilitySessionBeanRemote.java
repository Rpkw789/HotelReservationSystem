/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Remote;

/**
 *
 * @author ranen
 */
@Remote
public interface RoomAvailabilitySessionBeanRemote {
    public List<Pair<RoomType, Integer>> getAvailableRoomTypeAndPrice(Date checkInDate, Date checkOutDate);
}
