/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.RateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Employee;
import entity.Guest;
import entity.Partner;
import entity.Rate;
import entity.Reservation;
import entity.Room;
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
import util.exception.RateExistsException;
import util.exception.RoomExistsException;
import util.exception.RoomTypeExistsException;

/**
 *
 * @author ranen
 */
@Singleton
@Startup
@LocalBean
public class DataInitialisationSessionBean {

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private RateSessionBeanLocal rateSessionBean;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        Employee employee = em.find(Employee.class, 1L);
        if (employee == null) {
            DataInitialisation();
        }
    }

    private void DataInitialisation() {
        employeeDataInitialisation();
        roomTypeDataInitialisation();
        roomDataInitialisation();
        rateDataInitialisation();
    }

    private void employeeDataInitialisation() {
        Employee e1 = new Employee("sysadmin", "password", "employee1");
        e1.getEmployeeRoles().add(EmployeeRoleEnum.SYSTEM_ADMINISTRATOR);
        em.persist(e1);

        Employee e2 = new Employee("opmanager", "password", "employee2");
        e2.getEmployeeRoles().add(EmployeeRoleEnum.OPERATION_MANAGER);
        em.persist(e2);

        Employee e3 = new Employee("salesmanager", "password", "employee3");
        e3.getEmployeeRoles().add(EmployeeRoleEnum.SALES_MANAGER);
        em.persist(e3);

        Employee e4 = new Employee("guestrelo", "password", "employee4");
        e4.getEmployeeRoles().add(EmployeeRoleEnum.GUEST_RELATION_OFFICER);
        em.persist(e4);
    }

    private void roomTypeDataInitialisation() {
        try {
            RoomType grand = new RoomType("Grand Suite", "Description", "100sqm", "Queen Size Bed", 2);
            roomTypeSessionBean.createRoomType(grand);

            RoomType junior = new RoomType("Junior Suite", "Description", "100sqm", "Queen Size Bed", 2);
            junior.setNextHigherRoomType(grand);
            roomTypeSessionBean.createRoomType(junior);

            RoomType family = new RoomType("Family Room", "Description", "100sqm", "Queen Size Bed", 2);
            family.setNextHigherRoomType(junior);
            roomTypeSessionBean.createRoomType(family);

            RoomType premier = new RoomType("Premier Room", "Description", "100sqm", "Queen Size Bed", 2);
            premier.setNextHigherRoomType(family);
            roomTypeSessionBean.createRoomType(premier);

            RoomType deluxe = new RoomType("Deluxe Room", "Description", "100sqm", "Queen Size Bed", 2);
            deluxe.setNextHigherRoomType(premier);
            roomTypeSessionBean.createRoomType(deluxe);
        } catch (RoomTypeExistsException ex) {
        }
    }

    private void rateDataInitialisation() {
        RoomType deluxe = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Deluxe Room'").getSingleResult();
        RoomType premier = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Premier Room'").getSingleResult();
        RoomType family = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Family Room'").getSingleResult();
        RoomType junior = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Junior Suite'").getSingleResult();
        RoomType grand = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Grand Suite'").getSingleResult();

        try {
            Rate r1 = new Rate("Deluxe Room Published", 100, RateTypeEnum.PUBLISHED, deluxe);
            rateSessionBean.createNewRate(r1);
            Rate r2 = new Rate("Deluxe Room Normal", 50, RateTypeEnum.NORMAL, deluxe);
            rateSessionBean.createNewRate(r2);
            Rate r3 = new Rate("Premier Room Published", 200, RateTypeEnum.PUBLISHED, premier);
            rateSessionBean.createNewRate(r3);
            Rate r4 = new Rate("Premier Room Normal", 100, RateTypeEnum.NORMAL, premier);
            rateSessionBean.createNewRate(r4);
            Rate r5 = new Rate("Family Room Published", 300, RateTypeEnum.PUBLISHED, family);
            rateSessionBean.createNewRate(r5);
            Rate r6 = new Rate("Family Room Normal", 150, RateTypeEnum.NORMAL, family);
            rateSessionBean.createNewRate(r6);
            Rate r7 = new Rate("Junior Suite Published", 400, RateTypeEnum.PUBLISHED, junior);
            rateSessionBean.createNewRate(r7);
            Rate r8 = new Rate("Junior Suite Normal", 200, RateTypeEnum.NORMAL, junior);
            rateSessionBean.createNewRate(r8);
            Rate r9 = new Rate("Grand Suite Published", 500, RateTypeEnum.PUBLISHED, grand);
            rateSessionBean.createNewRate(r9);
            Rate r10 = new Rate("Grand Suite Published", 250, RateTypeEnum.NORMAL, grand);
            rateSessionBean.createNewRate(r10);
        } catch (RateExistsException ex) {
        }
    }

    private void roomDataInitialisation() {
        RoomType deluxe = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Deluxe Room'").getSingleResult();
        RoomType premier = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Premier Room'").getSingleResult();
        RoomType family = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Family Room'").getSingleResult();
        RoomType junior = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Junior Suite'").getSingleResult();
        RoomType grand = (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Grand Suite'").getSingleResult();

        try {
            Room r1 = new Room("0101");
            roomSessionBean.createNewRoom(r1, deluxe.getRoomTypeId());
            Room r2 = new Room("0201");
            roomSessionBean.createNewRoom(r2, deluxe.getRoomTypeId());
            Room r3 = new Room("0301");
            roomSessionBean.createNewRoom(r3, deluxe.getRoomTypeId());
            Room r4 = new Room("0401");
            roomSessionBean.createNewRoom(r4, deluxe.getRoomTypeId());
            Room r5 = new Room("0501");
            roomSessionBean.createNewRoom(r5, deluxe.getRoomTypeId());

            Room a1 = new Room("0102");
            roomSessionBean.createNewRoom(a1, premier.getRoomTypeId());
            Room a2 = new Room("0202");
            roomSessionBean.createNewRoom(a2, premier.getRoomTypeId());
            Room a3 = new Room("0302");
            roomSessionBean.createNewRoom(a3, premier.getRoomTypeId());
            Room a4 = new Room("0402");
            roomSessionBean.createNewRoom(a4, premier.getRoomTypeId());
            Room a5 = new Room("0502");
            roomSessionBean.createNewRoom(a5, premier.getRoomTypeId());

            Room b1 = new Room("0103");
            roomSessionBean.createNewRoom(b1, family.getRoomTypeId());
            Room b2 = new Room("0203");
            roomSessionBean.createNewRoom(b2, family.getRoomTypeId());
            Room b3 = new Room("0303");
            roomSessionBean.createNewRoom(b3, family.getRoomTypeId());
            Room b4 = new Room("0403");
            roomSessionBean.createNewRoom(b4, family.getRoomTypeId());
            Room b5 = new Room("0503");
            roomSessionBean.createNewRoom(b5, family.getRoomTypeId());

            Room c1 = new Room("0104");
            roomSessionBean.createNewRoom(c1, junior.getRoomTypeId());
            Room c2 = new Room("0204");
            roomSessionBean.createNewRoom(c2, junior.getRoomTypeId());
            Room c3 = new Room("0304");
            roomSessionBean.createNewRoom(c3, junior.getRoomTypeId());
            Room c4 = new Room("0404");
            roomSessionBean.createNewRoom(c4, junior.getRoomTypeId());
            Room c5 = new Room("0504");
            roomSessionBean.createNewRoom(c5, junior.getRoomTypeId());

            Room d1 = new Room("0105");
            roomSessionBean.createNewRoom(d1, grand.getRoomTypeId());
            Room d2 = new Room("0205");
            roomSessionBean.createNewRoom(d2, grand.getRoomTypeId());
            Room d3 = new Room("0305");
            roomSessionBean.createNewRoom(d3, grand.getRoomTypeId());
            Room d4 = new Room("0405");
            roomSessionBean.createNewRoom(d4, grand.getRoomTypeId());
            Room d5 = new Room("0505");
            roomSessionBean.createNewRoom(d5, grand.getRoomTypeId());
        } catch (RoomExistsException ex) {

        }

    }
}
