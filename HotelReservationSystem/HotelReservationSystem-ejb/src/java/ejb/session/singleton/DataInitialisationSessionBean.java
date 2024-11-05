/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Employee;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.EmployeeRoleEnum;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author ranen
 */
@Singleton
@Startup
@LocalBean
public class DataInitialisationSessionBean {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        try {
            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = 'username'");
            Employee employee = (Employee) query.getSingleResult();
        } catch (NoResultException ex) {
            dataInitialisation();
        }
    }

    private void dataInitialisation() {
        Employee employee = new Employee("username", "password", "employee");
        em.persist(employee);
        employee.getEmployeeRoles().add(EmployeeRoleEnum.SYSTEM_ADMINISTRATOR);
        employee.getEmployeeRoles().add(EmployeeRoleEnum.GUEST_RELATION_OFFICER);
        employee.getEmployeeRoles().add(EmployeeRoleEnum.OPERATION_MANAGER);
        employee.getEmployeeRoles().add(EmployeeRoleEnum.SALES_MANAGER);
    }
}
