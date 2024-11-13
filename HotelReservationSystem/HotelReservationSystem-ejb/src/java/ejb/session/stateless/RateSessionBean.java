/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Rate;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.enumeration.OperationalStatusEnum;
import util.enumeration.RateTypeEnum;
import util.exception.RateExistsException;
import util.exception.RateNotFoundException;

/**
 *
 * @author taniafoo
 */
@Stateless
public class RateSessionBean implements RateSessionBeanRemote, RateSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewRate(Rate newRate) throws RateExistsException {
        if (isUniqueRateName(newRate.getName())) {
            newRate.setRoomType(em.find(RoomType.class, newRate.getRoomType().getRoomTypeId()));
            em.persist(newRate);
            em.flush();
            
            RoomType roomType = newRate.getRoomType();
            roomType.getRates().add(newRate);
            return newRate.getRateId();
        } else {
            throw new RateExistsException("Rate with same name exsts!");
        }
    }

    @Override
    public boolean isUniqueRateName(String name) {
        Query query = em.createQuery("SELECT r FROM Rate r WHERE r.name = :name");
        query.setParameter("name", name);
        
        try {
            query.getSingleResult();
            return false;
        } catch (NoResultException ex) {
            return true;
        }
    }

    @Override
    public void updateRate(Rate updatedRate) throws RateNotFoundException {
        Rate rate = em.find(Rate.class, updatedRate.getRateId());
        if (rate == null) {
            throw new RateNotFoundException("Rate does not exist!");
        } else {
            rate.getRoomType().getRates().remove(rate);
            RoomType roomType = em.find(RoomType.class, updatedRate.getRoomType().getRoomTypeId());
            updatedRate.setRoomType(roomType);
            em.merge(updatedRate);
            roomType.getRates().add(updatedRate);
        }
    }

    @Override
    public void deleteRate(Long rateId) throws RateNotFoundException {
        Rate rate = em.find(Rate.class, rateId);
        if (rate == null) {
            throw new RateNotFoundException("Rate does not exist!");
        }
        if(rate.getReservations().isEmpty()) {
            em.remove(rate);
        } else {
            rate.setOperationalStatus(OperationalStatusEnum.DISABLED);
        }
         

    }
    
    @Override
    public Rate getRateByName(String name) throws RateNotFoundException {
        Query query = em.createQuery("SELECT r FROM Rate r WHERE r.name = :name");
        query.setParameter("name", name);
        
        try {
            Rate rate = (Rate)query.getSingleResult();
            return rate;
        } catch (NoResultException ex) {
            throw new RateNotFoundException("Rate does not Exist");
        }
    }

    @Override
    public List<Rate> getAllRates() {
        return em.createQuery("SELECT r FROM Rate r").getResultList();
    }
}
