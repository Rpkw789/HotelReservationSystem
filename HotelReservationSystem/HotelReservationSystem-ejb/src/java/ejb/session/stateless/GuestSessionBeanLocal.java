/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import javax.ejb.Local;
import util.exception.GuestExistsException;
import util.exception.GuestNotFoundException;

/**
 *
 * @author ranen
 */
@Local
public interface GuestSessionBeanLocal {

    public Guest getGuestByPassportNumber(String passportNumber) throws GuestNotFoundException;

    public boolean isUniqueUsername(String username);

    public Guest createNewGuest(Guest guest) throws GuestExistsException;

    public Guest getGuestByUsername(String username) throws GuestNotFoundException;

    public Guest createNewGuestThroughPartner(Guest guest, Long partnerId) throws GuestExistsException;
    
}
