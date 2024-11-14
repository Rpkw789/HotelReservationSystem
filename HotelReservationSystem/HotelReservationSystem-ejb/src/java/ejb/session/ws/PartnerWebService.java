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
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
                g.setPartner(null);
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

    @WebMethod(operationName = "getCheckInDate")
    public String getCheckInDate(@WebParam(name = "reservationId") Long reservationId) throws ReservationNotFoundException {
        Reservation r = reservationSessionBeanLocal.getReservationById(reservationId);
        return r.getCheckInDate().toString();
    }

    @WebMethod(operationName = "getCheckOutDate")
    public String getCheckOutDate(@WebParam(name = "reservationId") Long reservationId) throws ReservationNotFoundException {
        Reservation r = reservationSessionBeanLocal.getReservationById(reservationId);
        return r.getCheckOutDate().toString();
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
        g.setPartner(null);

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

    @WebMethod(operationName = "getRoomTypes")
    public List<RoomType> getRoomTypes() {
        Query query = em.createQuery("SELECT rt FROM RoomType rt");
        List<RoomType> list = query.getResultList();
        for (RoomType rt : list) {
            em.detach(rt);
            rt.setNextHigherRoomType(null);
            rt.setReservations(null);
            rt.setRates(null);
            rt.setRooms(null);
        }
        return list;
    }

    @WebMethod(operationName = "getAvailableNumber")
    public Integer getAvailableNumber(@WebParam(name = "checkInDate") String checkIn, @WebParam(name = "checkOutDate") String checkOut, @WebParam(name = "roomTypeId") Long roomTypeId) {
        LocalDate checkInDate = LocalDate.parse(checkIn);
        LocalDate checkOutDate = LocalDate.parse(checkOut);

        List<Pair<RoomType, Integer>> listOfRoomTypesAndNumbers = roomAvailabilitySessionBean.getAvailableRoomTypeAndNumber(checkInDate, checkOutDate);

        for (Pair<RoomType, Integer> pair : listOfRoomTypesAndNumbers) {
            if (roomTypeId == pair.getKey().getRoomTypeId()) {
                return pair.getValue();
            }
        }

        return 0;
    }

    @WebMethod(operationName = "createReservation")
    public Long createReservation(@WebParam(name = "numberOfRooms") int numberOfRooms, @WebParam(name = "fee") double fee, @WebParam(name = "roomType") Long roomTypeId, @WebParam(name = "guestId") Long guestId, @WebParam(name = "checkInDate") String checkIn, @WebParam(name = "checkOutDate") String checkOut, @WebParam(name = "usedRates") List<Long> usedRates) {
        System.out.println("HELPEFLPALDSPDA");
        LocalDate checkInDate = LocalDate.parse(checkIn); // This might fail if the format is incorrect
        LocalDate checkOutDate = LocalDate.parse(checkOut); // Same here
        
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        List<Rate> list = new ArrayList<Rate>();
        for(Long rateId : usedRates) {
            list.add(em.find(Rate.class, rateId));
        }

        // Proceed with creating reservation
        Reservation actualReservation = new Reservation(numberOfRooms, fee, false, checkInDate, checkOutDate, roomType);
        actualReservation.setRates(list);

        Long reservationId = reservationSessionBeanLocal.createReservation(actualReservation, guestId);
        return reservationId;
    }

    @WebMethod(operationName = "createGuestForPartner")
    public Long createGuestForPartner(@WebParam(name = "name") String name, @WebParam(name = "username") String username, @WebParam(name = "password") String password, @WebParam(name = "email") String email, @WebParam(name = "mobileNumber") String mobileNumber, @WebParam(name = "passportNumber") String passportNumber, @WebParam(name = "partnerId") Long partnerId) throws PartnerAccountExistsException {
        Guest guest = new Guest(name, username, password, email, mobileNumber, passportNumber);
        return partnerSessionBeanLocal.createPartnerAccount(guest, partnerId);

    }

    @WebMethod(operationName = "logInPartnerAccount")
    public Long logInPartnerAccount(@WebParam(name = "username") String username, @WebParam(name = "partnerId") Long partnerId) throws InvalidCredentialException {
        try {
            Guest guest = guestSessionBean.getGuestByUsername(username);
            if(guest.getPartner().equals(em.find(Partner.class, partnerId))) {
                return guest.getGuestId();
            } else {
                throw new InvalidCredentialException("Guest is not associated with Partner" + partnerId);
            }
        } catch (GuestNotFoundException ex) {
            throw new InvalidCredentialException(ex.getMessage());
        }

    }

    @WebMethod(operationName = "getRatesUsed")
    public List<Rate> getRatesUsed(@WebParam(name = "checkInDate") String checkIn, @WebParam(name = "checkOutDate") String checkOut, @WebParam(name = "roomTypeId") Long roomTypeId) {
        LocalDate checkInDate = LocalDate.parse(checkIn);
        LocalDate checkOutDate = LocalDate.parse(checkOut);

        List<Rate> ratesUse = roomAvailabilitySessionBean.getRateByRoomType(checkInDate, checkOutDate, roomTypeId);

        for (Rate r : ratesUse) {
            em.detach(r);
            r.setReservations(null);
            r.setRoomType(null);
        }

        return ratesUse;

    }
}
