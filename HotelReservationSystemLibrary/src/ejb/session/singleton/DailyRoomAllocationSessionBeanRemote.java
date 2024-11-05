/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.singleton;

import java.util.Date;
import javax.ejb.Remote;

/**
 *
 * @author ranen
 */
@Remote
public interface DailyRoomAllocationSessionBeanRemote {
    public void allocateReservedRoom(Date date);
}
