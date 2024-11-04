/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author taniafoo
 */
public class GuestExistsException extends Exception {

    /**
     * Creates a new instance of <code>GuestExistsException</code> without
     * detail message.
     */
    public GuestExistsException() {
    }

    /**
     * Constructs an instance of <code>GuestExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GuestExistsException(String msg) {
        super(msg);
    }
}
