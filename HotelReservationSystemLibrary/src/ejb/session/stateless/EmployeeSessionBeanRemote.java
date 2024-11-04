/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Remote;
import util.exception.EmployeeExistsException;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author taniafoo
 */
@Remote
public interface EmployeeSessionBeanRemote {
    public Employee createNewEmployee(Employee newEmployee) throws EmployeeExistsException;
    public Employee getEmployeeByUsername(String username) throws EmployeeNotFoundException;
    public boolean isUniqueUsername(String username);
    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException;
    public List<Employee> getAllEmployees();
}
