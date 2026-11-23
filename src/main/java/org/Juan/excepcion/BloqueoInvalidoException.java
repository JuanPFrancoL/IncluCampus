package org.Juan.excepcion;

public class BloqueoInvalidoException extends RuntimeException {
    public BloqueoInvalidoException(String mensaje) {
        super(mensaje);
    }
}