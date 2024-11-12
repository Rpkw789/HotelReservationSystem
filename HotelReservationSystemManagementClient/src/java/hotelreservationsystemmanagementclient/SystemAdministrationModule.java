/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.Employee;
import entity.Partner;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeRoleEnum;
import util.exception.EmployeeExistsException;
import util.exception.PartnerExistsException;

/**
 *
 * @author ranen
 */
public class SystemAdministrationModule {

    private EmployeeSessionBeanRemote employeeSessionBean;
    private PartnerSessionBeanRemote partnerSessionBean;

    private Employee loggedInEmployee;

    public SystemAdministrationModule(EmployeeSessionBeanRemote employeeSessionBean, PartnerSessionBeanRemote partnerSessionBean, Employee employee) {
        this.employeeSessionBean = employeeSessionBean;
        this.partnerSessionBean = partnerSessionBean;
        this.loggedInEmployee = employee;
    }

    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("");
            System.out.println("***System Administration Module***");

            if (!loggedInEmployee.getEmployeeRoles().contains(EmployeeRoleEnum.SYSTEM_ADMINISTRATOR)) {
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
                doCreateNewPartner();
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
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.println("*Create New Employee*");
        System.out.print("Name > ");
        String name = scanner.nextLine().trim();
        String username = "";
        while (true) {
            System.out.print("Username > ");
            username = scanner.nextLine().trim();
            if (username.length() < 5) {
                System.out.println("");
                System.out.println("Error: Username too short. Must be more than 5 characters");
                continue;
            }
            if (employeeSessionBean.isUniqueUsername(username)) {
                break;
            } else {
                System.out.println("");
                System.out.println("Error: Username taken. Enter another username");
            }
        }

        String password = "";
        while (true) {
            System.out.print("Password > ");
            password = scanner.nextLine().trim();
            if (password.length() < 5) {
                System.out.println("");
                System.out.println("Error: Password too short. Must be more than 5 characters");
            } else {
                break;
            }
        }

        Employee newEmployee = new Employee(username, password, name);
        try {
            Long employeeId = employeeSessionBean.createNewEmployee(newEmployee);
            System.out.println("");
            System.out.println("Employee created with Employee ID: " + employeeId);
        } catch (EmployeeExistsException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void doViewAllEmployees() {
        Scanner scanner = new Scanner(System.in);
        List<Employee> employees = employeeSessionBean.getAllEmployees();
        while (true) {
            System.out.println("");
            System.out.println("*View All Employees*");
            for (int i = 0; i < employees.size(); i++) {
                Employee employee = employees.get(i);
                System.out.println((i + 1) + ": " + employee.getName());
            }
            int exit = employees.size() + 1;
            System.out.println(exit + ": Exit");
            System.out.println("Select an Employee to view / Exit");

            int response = scanner.nextInt();

            if (response == exit) {
                break;
            }
            if (response <= 0 || response > exit) {
                System.out.println("");
                System.out.println("Error: Enter input again");
                continue;
            }

            Employee employee = employees.get(response - 1);
            System.out.println("");
            System.out.println("Employee - " + employee.getEmployeeId());
            System.out.println("Name: " + employee.getName());
            System.out.println("Username: " + employee.getUsername());
            System.out.println("Password: " + employee.getPassword());
        }
    }

    public void doCreateNewPartner() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("");
        System.out.println("*Create New Partner*");
        System.out.print("Organisation Name > ");
        String organisationName = scanner.nextLine().trim();
        System.out.print("Email > ");
        String email = scanner.nextLine().trim();
        System.out.print("Mobile Number > ");
        String mobileNumber = scanner.nextLine().trim();
        String username = "";
        while (true) {
            System.out.print("Username > ");
            username = scanner.nextLine().trim();
            if (username.length() < 5) {
                System.out.println("");
                System.out.println("Error: Username too short. Must be more than 5 characters");
                continue;
            }
            if (partnerSessionBean.isUniqueUsername(username)) {
                break;
            } else {
                System.out.println("");
                System.out.println("Error: Username taken. Enter another username");
            }
        }
        String password = "";
        while (true) {
            System.out.print("Password > ");
            password = scanner.nextLine().trim();
            if (password.length() < 5) {
                System.out.println("");
                System.out.println("Error: Password too short. Must be more than 5 characters");
            } else {
                break;
            }
        }

        Partner partner = new Partner(username, password, organisationName, email, mobileNumber);
        try {
            Long partnerId = partnerSessionBean.createNewPartner(partner);
            System.out.println("");
            System.out.println("Partner created with Partner ID: " + partnerId);
        } catch (PartnerExistsException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void doViewAllPartners() {
        Scanner scanner = new Scanner(System.in);
        List<Partner> partners = partnerSessionBean.getAllPartners();
        while (true) {
            System.out.println("");
            System.out.println("*View All Partners*");
            for (int i = 0; i < partners.size(); i++) {
                Partner partner = partners.get(i);
                System.out.println((i + 1) + ": " + partner.getOrganisationName());
            }
            int exit = partners.size() + 1;
            System.out.println(exit + ": Exit");
            System.out.println("Select a Partner to view / Exit");

            int response = scanner.nextInt();

            if (response == exit) {
                break;
            }
            if (response <= 0 || response > exit) {
                System.out.println("");
                System.out.println("Error: Enter input again");
                continue;
            }

            Partner partner = partners.get(response - 1);
            System.out.println("");
            System.out.println("Partner - " + partner.getPartnerId());
            System.out.println("Organisation Name: " + partner.getOrganisationName());
            System.out.println("Email: " + partner.getEmail());
            System.out.println("Mobile Number: " + partner.getMobileNumber());
            System.out.println("Username: " + partner.getUsername());
            System.out.println("Password: " + partner.getPassword());
        }
    }
}
