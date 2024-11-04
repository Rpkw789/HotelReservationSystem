/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author ranen
 */
public class CheckOutException extends Exception {

    /**
     * Creates a new instance of <code>CheckOutException</code> without detail
     * message.
     */
    public CheckOutException() {
    }

    /**
     * Constructs an instance of <code>CheckOutException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CheckOutException(String msg) {
        super(msg);
    }
}
