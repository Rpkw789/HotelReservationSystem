/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Rate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RateExistsException;
import util.exception.RateNotFoundException;

/**
 *
 * @author taniafoo
 */
@Remote
public interface RateSessionBeanRemote {
    public Rate createNewRate(Rate newRate) throws RateExistsException;
    public boolean isUniqueRateName(String name);
    public Rate updateRate(Rate updatedRate) throws RateNotFoundException;
    public void deleteRate(Long rateId) throws RateNotFoundException;
    public List<Rate> getAllRates();
}
