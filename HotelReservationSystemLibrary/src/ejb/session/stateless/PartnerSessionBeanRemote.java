/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Partner;
import entity.Reservation;
import java.util.List;
import javax.ejb.Remote;
import util.exception.GuestNotFoundException;
import util.exception.PartnerAccountExistsException;
import util.exception.PartnerExistsException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author taniafoo
 */
@Remote
public interface PartnerSessionBeanRemote {

    public Long createNewPartner(Partner newPartner) throws PartnerExistsException;

    public boolean isUniqueUsername(String username);

    public List<Partner> getAllPartners();

    public Partner getPartnerByUsername(String username) throws PartnerNotFoundException;
    public Guest createPartnerAccount(Guest g) throws PartnerAccountExistsException;
    public boolean isUniqueGuestUsername(String username);
    public Guest getPartnerAccountByUsername(String username) throws GuestNotFoundException;
    public List<Reservation> retrieveAllReservations(Partner p);
}
