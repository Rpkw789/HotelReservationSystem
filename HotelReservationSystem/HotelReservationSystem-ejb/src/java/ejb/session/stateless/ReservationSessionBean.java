/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Reservation;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.GuestNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author ranen
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private GuestSessionBeanLocal guestSessionBean;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<Reservation> getReservationByPassportNumber(String passportNumber) throws ReservationNotFoundException {
        try {
            Guest guest = guestSessionBean.getGuestByPassportNumber(passportNumber);
            return guest.getReservations();
        } catch (GuestNotFoundException ex) {
            throw new ReservationNotFoundException(ex.getMessage());
        }

    }

    @Override
    public Long createReservation(Reservation reservation, Long guestId) {
        Guest guest = em.find(Guest.class, guestId);
        reservation.setGuest(guest);
        em.persist(reservation);
        em.flush();
        
        reservation.getRoomType().getReservations().add(reservation);

        guest.getReservations().add(reservation);
        reservation.getRoomType().getReservations().add(reservation);
        return reservation.getReservationId();
    }

    public Reservation getReservationById(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation with Id " + reservationId + " not found");
        }
        return reservation;
    }

}
