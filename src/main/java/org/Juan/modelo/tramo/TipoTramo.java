package org.Juan.modelo.tramo;

/** Los tipos de camino que puede haber entre dos puntos. */
public enum TipoTramo {
    PLANO("plano"),
    RAMPA("rampa"),
    ESCALERA("escalera"),
    ASCENSOR("ascensor");

    private final String etiqueta;

    TipoTramo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /** Texto para mostrar a la persona. */
    public String getEtiqueta() {
        return etiqueta;
    }
}