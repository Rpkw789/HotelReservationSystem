/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ranen
 */
@Stateless
public class RoomAvailabilitySessionBean implements RoomAvailabilitySessionBeanRemote, RoomAvailabilitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    
    public List<Pair<RoomType,Integer>> getAvailableRoomTypeAndPrice(Date checkInDate, Date checkOutDate) {
        return null;
    }

    
}
