/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author ranen
 */
public class InvalidCredentialException extends Exception {

    /**
     * Creates a new instance of <code>InvalidCredentialException</code> without
     * detail message.
     */
    public InvalidCredentialException() {
    }

    /**
     * Constructs an instance of <code>InvalidCredentialException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidCredentialException(String msg) {
        super(msg);
    }
}
