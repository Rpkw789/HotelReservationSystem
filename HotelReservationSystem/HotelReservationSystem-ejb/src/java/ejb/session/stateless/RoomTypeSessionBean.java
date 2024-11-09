/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.exception.RoomTypeExistsException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author taniafoo
 */
@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createRoomType(RoomType newRoomType) throws RoomTypeExistsException{
        
        if (isUniqueName(newRoomType.getName())) {
            em.persist(newRoomType);
            em.flush();
        
            return newRoomType.getRoomTypeId();
        }
        else {
            throw new RoomTypeExistsException("Room type already exists!");
        }
    }
    
    @Override
    public boolean isUniqueName(String name) {
        Query query = em.createQuery("SELECT r FROM RoomType r WHERE r.name = :name");
        query.setParameter("name", name);
        
        try {
            query.getSingleResult();
            return false;
        } catch(NoResultException ex) {
            return true;
        }
    }

    @Override
    public RoomType updateRoomType(RoomType updatedRoomType) throws RoomTypeNotFoundException {
        RoomType old = em.find(RoomType.class, updatedRoomType.getRoomTypeId());
        
        if (old==null){
            throw new RoomTypeNotFoundException("Room type does not exist!");
        } else {
            em.merge(updatedRoomType);
            return updatedRoomType;
        }
    }
    
    @Override
    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException{
        RoomType roomtype = em.find(RoomType.class, roomTypeId);
        
        if (roomtype == null) {
            throw new RoomTypeNotFoundException("Room type does not exist!");
        } else {
            em.remove(roomtype);
        }
    }
    
    @Override
    public List<RoomType> getAllRoomType() {
        List<RoomType> roomTypes = em.createQuery("SELECT r FROM RoomType r").getResultList();
        for (RoomType rt : roomTypes) {
            rt.getRooms().size();
        }
        return roomTypes;
    }
}
