/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import javax.ejb.Remote;
import util.exception.CheckInException;
import util.exception.CheckOutException;

/**
 *
 * @author ranen
 */
@Remote
public interface CheckInOutSessionBeanRemote {

    public void checkOut(Long reservationId) throws CheckOutException;

    public void checkIn(Long reservationId) throws CheckInException;
}
