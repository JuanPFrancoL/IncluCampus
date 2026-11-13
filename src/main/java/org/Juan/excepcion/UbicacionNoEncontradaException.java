package org.Juan.excepcion;

/** Se lanza cuando se busca un lugar que no existe. */
public class UbicacionNoEncontradaException extends RuntimeException {

    public UbicacionNoEncontradaException(String id) {
        super("No existe el lugar con id: " + id);
    }
}