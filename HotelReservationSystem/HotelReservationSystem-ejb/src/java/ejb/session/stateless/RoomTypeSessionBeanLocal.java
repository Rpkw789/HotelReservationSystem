/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomTypeExistsException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author taniafoo
 */
@Local
public interface RoomTypeSessionBeanLocal {

    public boolean isUniqueName(String name);

    public RoomType createRoomType(RoomType newRoomType) throws RoomTypeExistsException;

    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException;

    public RoomType updateRoomType(RoomType updatedRoomType) throws RoomTypeNotFoundException;

    public List<RoomType> getAllRoomType();
    
}
