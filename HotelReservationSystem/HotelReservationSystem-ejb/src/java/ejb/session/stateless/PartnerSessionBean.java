/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.exception.PartnerExistsException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author taniafoo
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Partner createNewPartner(Partner newPartner) throws PartnerExistsException{
        if (isUniqueUsername(newPartner.getUsername())){
            em.persist(newPartner);
            em.flush();
            return newPartner;
        } else {
            throw new PartnerExistsException("Partner already exists!");
        }
    }
    
    public Partner getPartnerByUsername(String username) throws PartnerNotFoundException {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.username = :username");
        query.setParameter("username", username);
        
        try {
            Partner partner = (Partner) query.getSingleResult();
            return partner;
        } catch (NoResultException ex) {
            throw new PartnerNotFoundException("Partner with username " + username + " not found");
        }
    }
    
    @Override
    public boolean isUniqueUsername(String username) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(p) FROM Partner p WHERE p.username = :username", Long.class);
        query.setParameter("username", username);

        Long count = query.getSingleResult();
        return count < 0;
    }
    
    @Override
    public List<Partner> getAllPartners() {
        return em.createQuery("SELECT p FROM Partner p").getResultList();
    }
}
