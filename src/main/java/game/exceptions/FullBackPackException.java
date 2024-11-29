/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package game.exceptions;

public class FullBackPackException extends Exception{

    /**
     * Creates a new instance of <code>FullBackPackException</code> without
     * detail message.
     */
    public FullBackPackException() {
    }

    /**
     * Constructs an instance of <code>FullBackPackException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FullBackPackException(String msg) {
        super(msg);
    }
}
