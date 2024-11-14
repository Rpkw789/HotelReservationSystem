/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Rate;
import entity.Reservation;
import entity.Room;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.enumeration.RoomAvailabilityStatusEnum;
import util.exception.CheckInException;
import util.exception.CheckOutException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author ranen
 */
@Stateless
public class CheckInOutSessionBean implements CheckInOutSessionBeanRemote, CheckInOutSessionBeanLocal {

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    public void checkIn(Long reservationId) throws CheckInException {
        try {
            Reservation reservation = reservationSessionBean.getReservationById(reservationId);
            if (reservation.isCheckedIn()) {
                throw new CheckInException("Reservation has been checked in");
            }
            reservation.setCheckedIn(true);
            for(Room room : reservation.getGivenRooms()) {
                room.setAvailabilityStatus(RoomAvailabilityStatusEnum.NOT_AVAILABLE);
            }
        } catch (ReservationNotFoundException ex) {
            throw new CheckInException(ex.getMessage());
        }
    }
    
    public void checkOut(Long reservationId) throws CheckOutException {
        try {
            Reservation reservation = reservationSessionBean.getReservationById(reservationId);
            if (!reservation.isCheckedIn()) {
                throw new CheckOutException("Reservation has been checked out");
            }
            for(Room room : reservation.getGivenRooms()) {
                room.setReservation(null);
                room.setAvailabilityStatus(RoomAvailabilityStatusEnum.AVAILABLE);
            }
            for(Rate rate : reservation.getRates()) {
                rate.getReservations().remove(reservation);
            }
            reservation.getGuest().getReservations().remove(reservation);
        } catch (ReservationNotFoundException ex) {
            throw new CheckOutException(ex.getMessage());
        }
    }
}
