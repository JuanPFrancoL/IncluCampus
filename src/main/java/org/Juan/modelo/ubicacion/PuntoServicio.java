package org.Juan.modelo.ubicacion;

/**
 * Un lugar de servicio: Cafeteria, Biblioteca, Tienda UAM... A diferencia
 * de un punto de paso, aqui si se puede ir como origen o destino. No
 * guarda nada propio: el id y el nombre los guarda {@link Ubicacion}.
 *
 * @version 1.0
 */
public class PuntoServicio extends Ubicacion {

    /**
     * Crea un punto de servicio con su id y su nombre.
     *
     * @param id     identificador unico del lugar
     * @param nombre nombre del lugar (por ejemplo, "Biblioteca")
     */
    public PuntoServicio(String id, String nombre) {
        super(id, nombre);
    }

    /**
     * @return el tipo de ubicacion, que siempre es "Punto de servicio"
     */
    @Override
    public String getTipo() {
        return "Punto de servicio";
    }
}