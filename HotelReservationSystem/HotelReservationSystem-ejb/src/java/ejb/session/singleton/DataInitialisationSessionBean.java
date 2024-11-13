/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Employee;
import entity.Guest;
import entity.Partner;
import entity.Rate;
import entity.Reservation;
import entity.RoomType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import util.enumeration.PartnerRoleEnum;
import util.enumeration.RateTypeEnum;
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
        System.out.println("DataInitialisationSessionBean postConstruct called.");

        try {
            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = 'username'");
            Employee employee = (Employee) query.getSingleResult();
            System.out.println("Employee data found, skipping initialization.");
        } catch (NoResultException ex) {
            System.out.println("Initializing employee data.");
            employeeDataInitialisation();
        }

        try {
            Query query = em.createQuery("SELECT p FROM Partner p WHERE p.username = 'username'");
            Partner partner = (Partner) query.getSingleResult();
            System.out.println("Partner data found, skipping initialization.");
        } catch (NoResultException ex) {
            System.out.println("Initializing partner data.");
            partnerDataInitialisation();
        }
    }

    private void employeeDataInitialisation() {
        System.out.println("Creating initial employee data.");
        Employee employee = new Employee("username", "password", "employee");
        employee.getEmployeeRoles().add(EmployeeRoleEnum.SYSTEM_ADMINISTRATOR);
        employee.getEmployeeRoles().add(EmployeeRoleEnum.GUEST_RELATION_OFFICER);
        employee.getEmployeeRoles().add(EmployeeRoleEnum.OPERATION_MANAGER);
        employee.getEmployeeRoles().add(EmployeeRoleEnum.SALES_MANAGER);
        em.persist(employee);
    }

    private void partnerDataInitialisation() {
        System.out.println("Creating initial partner data.");
        Partner partner = new Partner("username", "password", "Holiday", "holiday@gmail.com", "62248440", PartnerRoleEnum.PARTNER_RESERVATION_MANAGER);
        em.persist(partner);
    }
}
