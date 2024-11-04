/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import javax.ejb.Local;
import util.exception.CheckInException;
import util.exception.CheckOutException;

/**
 *
 * @author ranen
 */
@Local
public interface CheckInOutSessionBeanLocal {

    public void checkOut(Long reservationId) throws CheckOutException;

    public void checkIn(Long reservationId) throws CheckInException;
    
}
