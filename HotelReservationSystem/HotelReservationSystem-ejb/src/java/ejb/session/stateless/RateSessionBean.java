/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Rate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
    public Rate createNewRate(Rate newRate) throws RateExistsException{
        if (isUniqueRateName(newRate.getName())){
            em.persist(newRate);
            em.flush();
            return newRate;
        } else {
            throw new RateExistsException("Rate with same name exsts!");
        }
    }
    
    @Override
    public boolean isUniqueRateName(String name) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(r) FROM Rate r WHERE r.name = :name", Long.class);
        query.setParameter("name", name);

        Long count = query.getSingleResult();
        return count < 0;
    }
    
    @Override
    public Rate updateRate(Rate updatedRate) throws RateNotFoundException{
        Rate rate = em.find(Rate.class, updatedRate.getRateId());
        if (rate == null ){
            throw new RateNotFoundException("Rate does not exist!");
        } else {
            em.merge(updatedRate);
            return updatedRate;
        }
    }
    
    @Override
    public void deleteRate(Long rateId) throws RateNotFoundException{
        Rate rate = em.find(Rate.class, rateId);
        if (rate == null ){
            throw new RateNotFoundException("Rate does not exist!");
        } else {
            em.remove(rate);
        }
        
    }
    
    @Override
    public List<Rate> getAllRates() {
        return em.createQuery("SELECT r FROM Rate r").getResultList();
    }
}
