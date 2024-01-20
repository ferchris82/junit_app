package org.chrisferdev.junit5.app.ejemplos.exceptions;

public class DineroInsuficienteException extends RuntimeException{
    public DineroInsuficienteException(String message) {
        super(message);
    }
}
