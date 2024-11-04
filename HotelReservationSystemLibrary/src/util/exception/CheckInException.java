/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author ranen
 */
public class CheckInException extends Exception {

    /**
     * Creates a new instance of <code>CheckInException</code> without detail
     * message.
     */
    public CheckInException() {
    }

    /**
     * Constructs an instance of <code>CheckInException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CheckInException(String msg) {
        super(msg);
    }
}
