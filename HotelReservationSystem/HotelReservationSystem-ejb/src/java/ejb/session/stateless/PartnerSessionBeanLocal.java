/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Partner;
import entity.Reservation;
import java.util.List;
import javax.ejb.Local;
import util.exception.GuestNotFoundException;
import util.exception.PartnerAccountExistsException;
import util.exception.PartnerExistsException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author taniafoo
 */
@Local
public interface PartnerSessionBeanLocal {

    public Long createNewPartner(Partner newPartner) throws PartnerExistsException;
    public boolean isUniqueUsername(String username);
    public List<Partner> getAllPartners();

    public Partner getPartnerByUsername(String username) throws PartnerNotFoundException;

    public Guest createPartnerAccount(Guest g) throws PartnerAccountExistsException;

    public boolean isUniqueGuestUsername(String username);

    public Guest getPartnerAccountByUsername(String username) throws GuestNotFoundException;

    public List<Reservation> retrieveAllReservations(Partner p);
    
}
