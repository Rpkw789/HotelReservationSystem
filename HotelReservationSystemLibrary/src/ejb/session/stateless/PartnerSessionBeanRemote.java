/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Remote;
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
}
