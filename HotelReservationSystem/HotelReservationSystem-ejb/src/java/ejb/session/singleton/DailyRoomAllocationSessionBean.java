/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import java.util.Date;
import javax.ejb.Singleton;

/**
 *
 * @author ranen
 */
@Singleton
public class DailyRoomAllocationSessionBean implements DailyRoomAllocationSessionBeanRemote, DailyRoomAllocationSessionBeanLocal {

    public void allocateReservedRoom(Date date) {
        
    }
}
