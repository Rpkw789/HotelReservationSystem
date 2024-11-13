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

        Query query = em.createQuery("SELECT r FROM Reservation r");
        List<Reservation> list = query.getResultList();
        if (list.isEmpty()) {
            System.out.println("Initializing reservation data.");
            
        } else {
            System.out.println("Reservation data found, skipping initialization.");
        }

        /*if (em.find(Guest.class, 1L) == null) {
            System.out.println("Initializing guest data.");
            guestInitialiseData();
        } else {
            System.out.println("Guest data found, skipping initialization.");
        }*/
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
        Partner partner = new Partner("username", "password", "Holiday", "holiday@gmail.com", "62248440");
        em.persist(partner);
    }

    /*private void reservationDataInitialisation() {
        System.out.println("Creating initial reservation data.");

        // Ensure a Rate is fetched or created
        Rate rate = createOrFetchRate();  // Implement this method to either create or fetch a Rate

        // Create reservation objects and set the rate
        Reservation reservation1 = new Reservation(1, 20, false, LocalDate.parse("2024-06-12"), LocalDate.parse("2024-06-15"));
        reservation1.setRate(rate);
        Reservation reservation2 = new Reservation(1, 20, false, LocalDate.parse("2024-06-10"), LocalDate.parse("2024-06-13"));
        reservation2.setRate(rate);
        Reservation reservation3 = new Reservation(1, 20, false, LocalDate.parse("2024-06-14"), LocalDate.parse("2024-06-17"));
        reservation3.setRate(rate);

        em.persist(reservation1);
        em.persist(reservation2);
        em.persist(reservation3);
    }*/

    

    private void guestInitialiseData() {
        System.out.println("Creating initial guest data.");
        Guest guest = new Guest("tania", "tania", "password", "taniakyfoo@gmail.com", "84221998", "A1B2C3D4E");
        em.persist(guest);

        Reservation reservation1 = new Reservation(3, 40.0, false, LocalDate.parse("2024-06-12"), LocalDate.parse("2024-06-15"));
        reservation1.setGuest(guest);
        em.persist(reservation1);

        Reservation reservation2 = new Reservation(2, 10.0, false, LocalDate.parse("2024-06-10"), LocalDate.parse("2024-06-13"));
        reservation2.setGuest(guest);
        em.persist(reservation2);

        guest.getReservations().add(reservation1);
        guest.getReservations().add(reservation2);

        em.flush();
    }
}
