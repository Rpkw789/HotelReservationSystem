/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author taniafoo
 */
public class PartnerAccountExistsException extends Exception{

    /**
     * Creates a new instance of <code>PartnerAccountExists</code> without
     * detail message.
     */
    public PartnerAccountExistsException() {
    }

    /**
     * Constructs an instance of <code>PartnerAccountExists</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PartnerAccountExistsException(String msg) {
        super(msg);
    }
}
