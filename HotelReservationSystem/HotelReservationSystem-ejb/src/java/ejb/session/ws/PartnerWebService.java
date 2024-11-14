/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import entity.Guest;
import entity.Partner;
import entity.Rate;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InvalidCredentialException;
import util.exception.PartnerExistsException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author taniafoo
 */
@WebService(serviceName = "PartnerWebService")
@Stateless()
public class PartnerWebService {

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
    public void doLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password)
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
        }catch (PartnerNotFoundException ex) {
             System.out.println("Account does not exist");
        }
    }

    @WebMethod(operationName = "retrieveAllReservations") //retrieve all reservation tied to partner
    public List<Reservation> retrieveAllReservations(@WebParam(name = "partner") Partner partner) {
        List<Reservation> reservations = partnerSessionBeanLocal.retrieveAllReservations(partner);

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

        List<Rate> rates = r.getRates();
        for (Rate rate : rates) {
            em.detach(rate);
            rate.setRoomType(null);
        }

        RoomType rt = r.getRoomType();
        em.detach(rt);
        rt.setReservations(null);
        rt.setRooms(null);
        ///TODO: rt.setHigherRoomType(null);

        Guest g = r.getGuest();
        em.detach(g);
        g.setReservations(null);

        return r;
    }
}
