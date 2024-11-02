/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author taniafoo
 */
public class RoomTypeExistsException extends Exception{

    /**
     * Creates a new instance of <code>RoomTypeExistsException</code> without
     * detail message.
     */
    public RoomTypeExistsException() {
    }

    /**
     * Constructs an instance of <code>RoomTypeExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomTypeExistsException(String msg) {
        super(msg);
    }
}
