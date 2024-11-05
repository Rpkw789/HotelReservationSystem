/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.RoomType;
import javax.ejb.Stateless;

/**
 *
 * @author ranen
 */
@Stateless
public class RoomAllocationSessionBean implements RoomAllocationSessionBeanRemote, RoomAllocationSessionBeanLocal {

    public void allocateWalkInRoom(RoomType roomType, Reservation reservation) {
        
    }
}
