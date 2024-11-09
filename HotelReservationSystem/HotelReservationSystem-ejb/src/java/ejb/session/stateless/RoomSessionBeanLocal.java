/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomExistsException;
import util.exception.RoomNotFoundException;

/**
 *
 * @author taniafoo
 */
@Local
public interface RoomSessionBeanLocal {

    public Long createNewRoom(Room newRoom, Long roomTypeId) throws RoomExistsException;

    public boolean isUniqueRoomNumber(String roomNumber);

    public Room updateRoom(Room updatedRoom) throws RoomNotFoundException;

    public void deleteRoom(Long roomId) throws RoomNotFoundException;

    public List<Room> getAllRooms();

    public Room getRoomByRoomNumber(String roomNumber) throws RoomNotFoundException;
    
}
