/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import util.exception.PartnerExistsException;

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
