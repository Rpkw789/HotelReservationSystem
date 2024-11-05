/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeExistsException;

/**
 *
 * @author taniafoo
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewEmployee(Employee newEmployee) throws EmployeeExistsException {

        if (isUniqueUsername(newEmployee.getUsername())) {
            em.persist(newEmployee);
            em.flush();
            return newEmployee.getEmployeeId();
        } else {
            throw new EmployeeExistsException("Employee already exists!");
        }

    }

    @Override
    public boolean isUniqueUsername(String username) {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username");
        query.setParameter("username", username);
        try {
            query.getSingleResult();
            return false;
        } catch (NoResultException ex) {
            return true;
        }
    }

    @Override
    public Employee getEmployeeByUsername(String username) throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username");
        query.setParameter("username", username);
        try {
            Employee employee = (Employee) query.getSingleResult();
            return employee;
        } catch (NoResultException ex) {
            throw new EmployeeNotFoundException("Employee with username '" + username + "' not found");
        }
    }

    @Override
    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException {
        Employee e = em.find(Employee.class, employeeId);
        if (e == null) {
            throw new EmployeeNotFoundException("Employee does not exist");
        } else {
            em.remove(e);
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        return em.createQuery("SELECT e FROM Employee e").getResultList();
    }

}
