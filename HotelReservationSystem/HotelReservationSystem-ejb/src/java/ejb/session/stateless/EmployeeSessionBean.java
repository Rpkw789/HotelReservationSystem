/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
    public Employee createNewEmployee(Employee newEmployee) throws EmployeeExistsException {

        if (isUniqueUsername(newEmployee.getUsername())) {
            em.persist(newEmployee);
            em.flush();
            return newEmployee;
        } else {
            throw new EmployeeExistsException("Employee already exists!");
        }

    }

    @Override
    public boolean isUniqueUsername(String username) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Employee e WHERE e.username = :username", Long.class);
        query.setParameter("username", username);

        Long count = query.getSingleResult();
        return count < 0; //return true if no other username exists yet
    }

    @Override
    public Employee getEmployeeByUsername(String username) throws EmployeeNotFoundException {
        Employee e = (Employee) em.createQuery("SELECT e FROM Employee e WHERE e.username = :username")
                .setParameter("username", username)
                .getSingleResult();

        if (e == null) {
            throw new EmployeeNotFoundException("Username does not exists!");
        }
        return e;
    }
    
    @Override
    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException {
        Employee e = em.find(Employee.class, employeeId);
        if (e==null) {
            throw new EmployeeNotFoundException("Employee does not exist");
        } else {
            em.remove(e);
        }
    }
    
    @Override
    public List<Employee> getAllEmployees(){
        return em.createQuery("SELECT e FROM Employee e").getResultList();
    }

}
