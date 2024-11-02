/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author taniafoo
 */
public class ReservationExistsException extends Exception {

    /**
     * Creates a new instance of <code>ReservationExistsException</code> without
     * detail message.
     */
    public ReservationExistsException() {
    }

    /**
     * Constructs an instance of <code>ReservationExistsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ReservationExistsException(String msg) {
        super(msg);
    }
}
