/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazardependecias.excepciones;

public class NegociosException extends Exception {
    public NegociosException(String message) {
        super(message);
    }

    public NegociosException(String message, Throwable cause) {
        super(message, cause);
    }
}
