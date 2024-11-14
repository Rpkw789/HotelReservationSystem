/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Partner;
import entity.Reservation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.exception.GuestExistsException;
import util.exception.GuestNotFoundException;

/**
 *
 * @author ranen
 */
@Stateless
public class GuestSessionBean implements GuestSessionBeanRemote, GuestSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Guest getGuestByUsername(String username) throws GuestNotFoundException {
        Query query = em.createQuery("SELECT g FROM Guest g WHERE g.username = :username");
        query.setParameter("username", username);

        try {
            Guest guest = (Guest) query.getSingleResult();
            return guest;
        } catch (NoResultException ex) {
            throw new GuestNotFoundException("Guest with username '" + username + "' not found");
        }
    }

    @Override
    public Guest createNewGuest(Guest guest) throws GuestExistsException {
        if(isUniqueUsername(guest.getUsername())) {
            em.persist(guest);
            em.flush();
            return guest;
        } else {
            throw new GuestExistsException("Guest already exists!");
        }
    }
    
    public Guest createNewGuestThroughPartner(Guest guest, Long partnerId) throws GuestExistsException {
        if(isUniqueUsername(guest.getUsername())) {
            em.persist(guest);
            em.flush();
            Partner partner = em.find(Partner.class, partnerId);
            partner.getGuests().add(guest);
            return guest;
        } else {
            throw new GuestExistsException("Guest already exists!");
        }
    }
    
    @Override
    public boolean isUniqueUsername(String username) {
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
    public Guest getGuestByPassportNumber(String passportNumber) throws GuestNotFoundException {
        Query query = em.createQuery("SELECT g FROM Guest g WHERE g.passportNumber = :passportNumber");
        query.setParameter("passportNumber", passportNumber);

        try {
            Guest guest = (Guest) query.getSingleResult();
            guest.getReservations().size();
            for (Reservation reservation : guest.getReservations()) {
                reservation.getGivenRooms().size();
            }
            return guest;
        } catch (NonUniqueResultException ex) {
            throw new GuestNotFoundException("Guest with passportNumber '" + passportNumber + "' not found");
        } catch (NoResultException ex) {
            throw new GuestNotFoundException("Multiple Guests with passportNumber '" + passportNumber + "' found");
        }
    }


}
