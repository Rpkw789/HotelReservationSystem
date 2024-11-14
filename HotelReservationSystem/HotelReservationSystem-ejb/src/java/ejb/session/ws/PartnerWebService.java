/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.GuestSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomAvailabilitySessionBeanLocal;
import entity.Guest;
import entity.Partner;
import entity.Rate;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.GuestNotFoundException;
import util.exception.InvalidCredentialException;
import util.exception.PartnerExistsException;
import util.exception.PartnerAccountExistsException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author taniafoo
 */
@WebService(serviceName = "PartnerWebService")
@Stateless()
public class PartnerWebService {

    @EJB
    private GuestSessionBeanLocal guestSessionBean;

    @EJB
    private RoomAvailabilitySessionBeanLocal roomAvailabilitySessionBean;

    @EJB(name = "ReservationSessionBeanLocal")
    private ReservationSessionBeanLocal reservationSessionBeanLocal;

    @EJB(name = "PartnerSessionBeanLocal")
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    /**
     * This is a sample web service operation
     *
     * @throws util.exception.PartnerNotFoundException
     */
    @WebMethod(operationName = "doLogin") //login
    public Partner doLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password)
            throws InvalidCredentialException {

        try {
            Partner p = partnerSessionBeanLocal.getPartnerByUsername(username);
            if (!p.getPassword().equals(password)) {
                throw new InvalidCredentialException("Password is wrong");
            }
            em.detach(p);
            List<Guest> guests = p.getGuests();
            for (Guest g : guests) {
                em.detach(g);
                g.setReservations(null);
            }
            return p;
        } catch (PartnerNotFoundException ex) {
            throw new InvalidCredentialException("Partner does not exist");
        }
    }

    @WebMethod(operationName = "retrieveAllReservations") //retrieve all reservation tied to partner
    public List<Reservation> retrieveAllReservations(@WebParam(name = "partner") Partner partner) {
        System.out.println("HERE");
        List<Reservation> reservations = partnerSessionBeanLocal.retrieveAllReservations(partner);
        System.out.println("RESDER: " + reservations);

        for (Reservation r : reservations) {
            em.detach(r);
            r.setGivenRooms(null);
            r.setRoomType(null);
            r.setRates(null);
            r.setGuest(null);
        }
        return reservations;
    }

    @WebMethod(operationName = "getReservationById") //retrieve particular reservation from the id
    public Reservation getReservationById(@WebParam(name = "reservationId") Long reservationId) throws ReservationNotFoundException {
        Reservation r = reservationSessionBeanLocal.getReservationById(reservationId);

        em.detach(r);
        r.setGivenRooms(null);

        r.setRates(null);

        RoomType rt = r.getRoomType();
        em.detach(rt);
        rt.setReservations(null);
        rt.setRooms(null);
        rt.setNextHigherRoomType(null);
        rt.setRates(null);

        Guest g = r.getGuest();
        em.detach(g);
        g.setReservations(null);

        return r;
    }

    @WebMethod(operationName = "getReservationByGuest") //retrieve particular reservation from the id
    public List<Reservation> getReservationByGuest(@WebParam(name = "guestId") Long guestId) throws ReservationNotFoundException {

        Guest g = em.find(Guest.class, guestId);
        List<Reservation> reservations = reservationSessionBeanLocal.getReservationByPassportNumber(g.getPassportNumber());
        for (Reservation r : reservations) {
            em.detach(r);
            r.setGivenRooms(null);
            r.setRoomType(null);
            r.setRates(null);
            r.setGuest(null);
        }
        return reservations;
        
    }

    @WebMethod(operationName="getAvailableRoomTypeAndNumber")
    public List<Pair<RoomType, Integer>> getAvailableRoomTypeAndNumber(@WebParam(name="checkInDate")LocalDate checkInDate, @WebParam(name ="checkOutDate")LocalDate checkOutDate) {
        List<Pair<RoomType, Integer>> listOfRoomTypesAndNumbers = roomAvailabilitySessionBean.getAvailableRoomTypeAndNumber(checkInDate, checkOutDate);
        
        for (Pair<RoomType, Integer> pair : listOfRoomTypesAndNumbers) {
            RoomType rt = pair.getKey();
            em.detach(rt);
            rt.setNextHigherRoomType(null);
            rt.setRates(null);
            rt.setRooms(null);
            rt.setReservations(null);
        }
        
        return listOfRoomTypesAndNumbers;
    }

    @WebMethod(operationName="createReservation")
    public Long createReservation(@WebParam(name = "reservation")Reservation reservation, @WebParam(name="guestId")Long guestId) {
        Long reservationId = reservationSessionBeanLocal.createReservation(reservation, guestId);
        return reservationId;
    }


    @WebMethod(operationName="createGuestForPartner")
    public Long createGuestForPartner(Guest guest, Long partnerId) throws PartnerAccountExistsException {
        return partnerSessionBeanLocal.createPartnerAccount(guest, partnerId); 

    }

    @WebMethod(operationName = "logInPartnerAccount")
    public Long logInPartnerAccount(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidCredentialException {
        return partnerSessionBeanLocal.logInPartnerAccount(username, password);
    }
    
    @WebMethod(operationName = "getRatesUsed")
    public List<Rate> getRatesUsed(@WebParam(name="startDate")LocalDate startDate, @WebParam(name="endDate")LocalDate endDate, @WebParam(name="roomTypeId")Long roomTypeId) {
        List<Rate> ratesUse = roomAvailabilitySessionBean.getRateByRoomType(startDate, endDate, roomTypeId);
        
        for (Rate r : ratesUse){
            em.detach(r);
            r.setReservations(null);
            r.setRoomType(null);
        }
        
        return ratesUse;

    }
}
