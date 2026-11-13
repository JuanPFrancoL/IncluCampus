package org.Juan.excepcion;

/** Se lanza cuando no existe ningún camino posible entre dos lugares. */
public class RutaNoDisponibleException extends RuntimeException {

    public RutaNoDisponibleException(String origen, String destino) {
        super("No hay una ruta disponible de " + origen + " a " + destino + " para tu perfil.");
    }
}