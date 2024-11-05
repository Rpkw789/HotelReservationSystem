/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.EmployeeRoleEnum;

/**
 *
 * @author ranen
 */
public class SystemAdministrationModule {
    private EmployeeSessionBeanRemote employeeSessionBean;
    private PartnerSessionBeanRemote partnerSessionBean;
    
    private Employee employee;

    public SystemAdministrationModule(EmployeeSessionBeanRemote employeeSessionBean, PartnerSessionBeanRemote partnerSessionBean, Employee employee) {
        this.employeeSessionBean = employeeSessionBean;
        this.partnerSessionBean = partnerSessionBean;
        this.employee = employee;
    }
    
    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("");
            System.out.println("***System Administration Module***");
            
            if (!employee.getEmployeeRoles().contains(EmployeeRoleEnum.SYSTEM_ADMINISTRATOR)) {
                System.out.println(EmployeeRoleEnum.SYSTEM_ADMINISTRATOR + " needed for this operation.");
                return;
            }
            
            System.out.println("1: Create New Employee");
            System.out.println("2: View All Employees");
            System.out.println("3: Create New Partner");
            System.out.println("4: View All Partners");
            System.out.println("5: Exit");
            
            int response = scanner.nextInt();
            
            if (response == 1) {
                doCreateNewEmployee();
            } else if (response == 2) {
                doViewAllEmployees();
            } else if (response == 3) {
                doCreateNewEmployee();
            } else if (response == 4) {
                doViewAllPartners();
            } else if (response == 5) {
                return;
            } else {
                System.out.println();
                System.out.println("Error: Enter input again");
                System.out.println();
            }
        }
    }
    
    public void doCreateNewEmployee() {
        System.out.println("doCreateNewEmployee");
    }
    
    public void doViewAllEmployees() {
        System.out.println("doViewAllEmployees");
    }
    
    public void doCreateNewPartner() {
        System.out.println("doCreateNewPartner");
    }
    
    public void doViewAllPartners() {
        System.out.println("doViewAllPartners");
    }
}
