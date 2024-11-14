/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Partner;
import entity.Reservation;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.exception.GuestNotFoundException;
import util.exception.InvalidCredentialException;
import util.exception.PartnerAccountExistsException;
import util.exception.PartnerExistsException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author taniafoo
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @EJB
    private GuestSessionBeanLocal guestSessionBean;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    

    @Override
    public Long createNewPartner(Partner newPartner) throws PartnerExistsException{
        if (isUniqueUsername(newPartner.getUsername())){
            em.persist(newPartner);
            em.flush();
            return newPartner.getPartnerId();
        } else {
            throw new PartnerExistsException("Partner already exists!");
        }
    }
    
    @Override
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
    public Long createPartnerAccount(Guest g, Long partnerId) throws PartnerAccountExistsException {
        if (isUniqueGuestUsername(g.getUsername())){
            em.persist(g);
            em.flush();
            Partner p = em.find(Partner.class, partnerId);
            p.getGuests().add(g);
            return g.getGuestId();
        } else {
            throw new PartnerAccountExistsException("Account with same username already exists!");     
        }
    }
    
    @Override
    public Long logInPartnerAccount(String username, String password) throws InvalidCredentialException{
       try {
           Guest g = guestSessionBean.getGuestByUsername(username);
           if (!g.getPassword().equals(password)) {
               throw new InvalidCredentialException("Password is not correct!");
           }
           return g.getGuestId();
       } catch (GuestNotFoundException ex) {
           throw new InvalidCredentialException("Username is not correct!");
       }
    }
    
    @Override
    public boolean isUniqueGuestUsername(String username) {
        Query query = em.createQuery("SELECT g FROM Guest g WHERE g.username = :username");
        query.setParameter("username", username);
        try {
            query.getSingleResult();
            return false;
        } catch (NoResultException ex) {
            return true;
        }
    }
    
    @Override
    public Guest getPartnerAccountByUsername(String username) throws GuestNotFoundException {
        Query query = em.createQuery("SELECT g FROM Guest g WHERE g.username= :username");
        query.setParameter("username", username);
        
        Guest g = (Guest) query.getSingleResult();
        
        if (g==null) {
            throw new GuestNotFoundException("Account does not exist");
        } else {
            return g;
        }
    }
    
    @Override
    public List<Reservation> retrieveAllReservations(Partner p) {
        Partner partner = em.find(Partner.class, p.getPartnerId());
        List<Guest> guests = partner.getGuests();
        List<Reservation> allReservations = new ArrayList<Reservation>();
        
        for (Guest g : guests){
            List<Reservation> guestReservations = g.getReservations();
            
            for (Reservation r : guestReservations) {
                allReservations.add(r);
            }
        }
        
        return allReservations;
    }
    
    @Override
    public boolean isUniqueUsername(String username) {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.username = :username");
        query.setParameter("username", username);
        try {
            query.getSingleResult();
            return false;
        } catch (NoResultException ex) {
            return true;
        }
    }
    
    @Override
    public List<Partner> getAllPartners() {
        return em.createQuery("SELECT p FROM Partner p").getResultList();
    }
}
