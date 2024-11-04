/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import util.exception.RoomExistsException;
import util.exception.RoomNotFoundException;

/**
 *
 * @author taniafoo
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Room createNewRoom(Room newRoom, Long roomTypeId) throws RoomExistsException{
        if (isUniqueRoomNumber(newRoom.getRoomNumber())){
            em.persist(newRoom);
            em.flush();
            
            RoomType roomtype = em.find(RoomType.class, roomTypeId);
            newRoom.setRoomType(roomtype);
            
            return newRoom;
        } else {
            throw new RoomExistsException("Room with same room number already exists!");
        }
    }
    
    @Override
    public boolean isUniqueRoomNumber(String roomNumber) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(r) FROM Room r WHERE r.roomNumber = :roomNumber", Long.class);
        query.setParameter("roomNumber", roomNumber);

        Long count = query.getSingleResult();
        return count < 0;
    }
    
    @Override
    public Room updateRoom(Room updatedRoom) throws RoomNotFoundException {
        Room room = em.find(Room.class, updatedRoom.getRoomId());
        
        if (room == null) {
            throw new RoomNotFoundException("Room does not exist!");
        } else {
            em.merge(updatedRoom);
            return updatedRoom;
        }
    }
    
    @Override
    public void deleteRoom(Long roomId) throws RoomNotFoundException{
        Room room = em.find(Room.class, roomId);
        
        if (room == null) {
            throw new RoomNotFoundException("Room does not exist!");
        } else {
            em.remove(room);
        }
    }
    
    @Override
    public List<Room> getAllRooms() {
        return em.createQuery("SELECT r FROM Room r").getResultList();
    }
    
    @Override
    public Room getRoomByRoomNumber(String roomNumber) throws RoomNotFoundException{
        Room room = (Room) em.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :roomNumber")
                .setParameter("roomNumber", roomNumber)
                .getSingleResult();
        
        if (room==null){
            throw new RoomNotFoundException("Room does not exist!");
        } else {
            return room;
        }
    }
}
