package org.Juan.modelo.ubicacion;

/**
 * Cualquier punto del mapa del campus.
 * Es abstracta: no existe una "Ubicacion" a secas, siempre es de un tipo concreto.
 */
public abstract class Ubicacion {

    private final String id;
    private final String nombre;

    protected Ubicacion(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    /** Cada tipo de lugar dice qué es. */
    public abstract String getTipo();

    /** Si la persona puede elegir este punto como origen o destino. */
    public boolean esSeleccionable() {
        return true;
    }

    @Override
    public String toString() {
        return getTipo() + ": " + nombre;
    }
}