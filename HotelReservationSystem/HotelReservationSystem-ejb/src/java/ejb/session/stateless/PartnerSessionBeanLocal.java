/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Local;
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
    
}
