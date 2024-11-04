/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomTypeExistsException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author taniafoo
 */
@Remote
public interface RoomTypeSessionBeanRemote {
    public boolean isUniqueName(String name);
    public RoomType createRoomType(RoomType newRoomType) throws RoomTypeExistsException;
    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException;
    public RoomType updateRoomType(RoomType updatedRoomType) throws RoomTypeNotFoundException;
    public List<RoomType> getAllRoomType();
}
