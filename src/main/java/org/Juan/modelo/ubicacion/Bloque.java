package org.Juan.modelo.ubicacion;

/** Un edificio del campus. Bloque F y Bloque 16 son dos objetos de esta clase. */
public class Bloque extends Ubicacion {

    public Bloque(String id, String nombre) {
        super(id, nombre);
    }

    @Override
    public String getTipo() {
        return "Bloque";
    }
}