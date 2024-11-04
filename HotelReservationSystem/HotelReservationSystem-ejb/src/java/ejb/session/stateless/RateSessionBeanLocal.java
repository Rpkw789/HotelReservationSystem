/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Rate;
import java.util.List;
import javax.ejb.Local;
import util.exception.RateExistsException;
import util.exception.RateNotFoundException;

/**
 *
 * @author taniafoo
 */
@Local
public interface RateSessionBeanLocal {

    public Rate createNewRate(Rate newRate) throws RateExistsException;

    public boolean isUniqueRateName(String name);

    public Rate updateRate(Rate updatedRate) throws RateNotFoundException;

    public void deleteRate(Long rateId) throws RateNotFoundException;

    public List<Rate> getAllRates();
    
}
