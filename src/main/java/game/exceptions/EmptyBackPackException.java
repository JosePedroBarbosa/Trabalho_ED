/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package game.exceptions;

/**
 *
 * @author carlo
 */
public class EmptyBackPackException extends Exception{

    /**
     * Creates a new instance of <code>EmptyBackPackException</code> without
     * detail message.
     */
    public EmptyBackPackException() {
    }

    /**
     * Constructs an instance of <code>EmptyBackPackException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EmptyBackPackException(String msg) {
        super(msg);
    }
}
