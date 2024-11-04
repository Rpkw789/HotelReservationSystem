/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import java.util.List;
import javax.ejb.Remote;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author ranen
 */
@Remote
public interface ReservationSessionBeanRemote {

    public List<Reservation> getReservationByPassportNumber(String passportNumber) throws ReservationNotFoundException;

    public Reservation createReservation(Reservation reservation, Long guestId);
    
    public Reservation getReservationById(Long reservationId) throws ReservationNotFoundException;
}
