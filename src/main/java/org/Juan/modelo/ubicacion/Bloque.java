package org.Juan.modelo.ubicacion;

/**
 * Un edificio del campus. Bloque F y Bloque 16 son dos objetos de esta clase.
 * No guarda nada propio: solo dice que su tipo es "Bloque", y el id y el
 * nombre los guarda {@link Ubicacion}.
 *
 * @version 1.0
 */
public class Bloque extends Ubicacion {

    /**
     * Crea un bloque con su id y su nombre.
     *
     * @param id     identificador unico del bloque
     * @param nombre nombre del bloque (por ejemplo, "Bloque F")
     */
    public Bloque(String id, String nombre) {
        super(id, nombre);
    }

    /**
     * @return el tipo de ubicacion, que siempre es "Bloque"
     */
    @Override
    public String getTipo() {
        return "Bloque";
    }
}