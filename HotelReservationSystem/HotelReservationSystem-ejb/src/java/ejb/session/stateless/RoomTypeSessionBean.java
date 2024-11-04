/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    public RoomType createRoomType(RoomType newRoomType) throws RoomTypeExistsException{
        
        if (isUniqueName(newRoomType.getName())) {
            em.persist(newRoomType);
            em.flush();
        
            return newRoomType;
        }
        else {
            throw new RoomTypeExistsException("Room type already exists!");
        }
    }
    
    @Override
    public boolean isUniqueName(String name) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(r) FROM RoomType r WHERE r.name = :name", Long.class);
        query.setParameter("name", name);

        Long count = query.getSingleResult();
        return count < 0;
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
        return em.createQuery("SELECT r FROM RoomType r").getResultList();
    }
}
