/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author taniafoo
 */
public class RateNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>RateNotFoundException</code> without
     * detail message.
     */
    public RateNotFoundException() {
    }

    /**
     * Constructs an instance of <code>RateNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RateNotFoundException(String msg) {
        super(msg);
    }
}
