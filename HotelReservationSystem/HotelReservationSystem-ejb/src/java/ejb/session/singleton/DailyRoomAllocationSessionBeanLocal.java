/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.singleton;

import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author ranen
 */
@Local
public interface DailyRoomAllocationSessionBeanLocal {

    public void allocateReservedRoom(Date date);
    
}
