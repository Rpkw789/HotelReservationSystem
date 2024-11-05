/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemreservationclient;

import util.enumeration.EmployeeRoleEnum;

/**
 *
 * @author ranen
 */
public class InputNumberRolePair {
    private int number;
    private EmployeeRoleEnum role;

    public InputNumberRolePair(EmployeeRoleEnum role) {
        this.role = role;
    }

    
    public InputNumberRolePair(int number, EmployeeRoleEnum role) {
        this.number = number;
        this.role = role;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return the role
     */
    public EmployeeRoleEnum getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(EmployeeRoleEnum role) {
        this.role = role;
    }
    
    
}
