/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author taniafoo
 */
public class EmployeeExistsException extends Exception{

    /**
     * Creates a new instance of <code>EmployeeExistsException</code> without
     * detail message.
     */
    public EmployeeExistsException() {
    }

    /**
     * Constructs an instance of <code>EmployeeExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EmployeeExistsException(String msg) {
        super(msg);
    }
}
