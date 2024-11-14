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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.enumeration.OperationalStatusEnum;
import util.enumeration.RoomAvailabilityStatusEnum;
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
    public Long createNewRoom(Room newRoom, Long roomTypeId) throws RoomExistsException {
        if (isUniqueRoomNumber(newRoom.getRoomNumber())) {

            RoomType roomType = em.find(RoomType.class, roomTypeId);
            newRoom.setRoomType(roomType);
            em.persist(newRoom);
            em.flush();
            roomType.getRooms().add(newRoom);

            return newRoom.getRoomId();
        } else {
            throw new RoomExistsException("Room with same room number already exists!");
        }
    }

    @Override
    public boolean isUniqueRoomNumber(String roomNumber) {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :roomNumber");
        query.setParameter("roomNumber", roomNumber);

        try {
            query.getSingleResult();
            return false;
        } catch (NoResultException ex) {
            return true;
        }
    }

    @Override
    public Room updateRoom(Room updatedRoom) throws RoomNotFoundException {
        Room room = em.find(Room.class, updatedRoom.getRoomId());
        
        if (room == null) {
            throw new RoomNotFoundException("Room does not exist!");
        } else {
            room.getRoomType().getRooms().remove(room);
            RoomType roomType = em.find(RoomType.class, updatedRoom.getRoomType().getRoomTypeId());
            updatedRoom.setRoomType(roomType);
            em.merge(updatedRoom);
            
            return updatedRoom;
        }
    }

    @Override
    public void deleteRoom(Long roomId) throws RoomNotFoundException {
        Room room = em.find(Room.class, roomId);

        if (room == null) {
            throw new RoomNotFoundException("Room does not exist!");
        } else {
            room.getRoomType().getRooms().remove(room);
            if(room.getAvailabilityStatus().equals(RoomAvailabilityStatusEnum.NOT_AVAILABLE)) {
                room.setOperationalStatus(OperationalStatusEnum.DISABLED);
                return;
            }
            em.remove(room);
        }
    }

    @Override
    public List<Room> getAllRooms() {
        return em.createQuery("SELECT r FROM Room r").getResultList();
    }

    @Override
    public Room getRoomByRoomNumber(String roomNumber) throws RoomNotFoundException {
        try {
            Room room = (Room) em.createQuery("SELECT r FROM Room r WHERE r.roomNumber = :roomNumber")
                    .setParameter("roomNumber", roomNumber)
                    .getSingleResult();
            return room;
        } catch (NoResultException ex) {
            throw new RoomNotFoundException("Room does not exist!");
        }

    }
}
