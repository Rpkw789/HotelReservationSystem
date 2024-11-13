/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package entity;

/**
 *
 * @author taniafoo
 */
public class DateTimeParseException extends Exception{

    /**
     * Creates a new instance of <code>DateTimeParseException</code> without
     * detail message.
     */
    public DateTimeParseException() {
    }

    /**
     * Constructs an instance of <code>DateTimeParseException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DateTimeParseException(String msg) {
        super(msg);
    }
}
