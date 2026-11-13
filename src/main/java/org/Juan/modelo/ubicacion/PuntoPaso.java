package org.Juan.modelo.ubicacion;

/**
 * Un punto intermedio del camino: una entrada, una esquina, el pie de una rampa.
 * Sirve para dibujar caminos realistas, pero nadie va "hacia" él.
 */
public class PuntoPaso extends Ubicacion {

    public PuntoPaso(String id, String nombre) {
        super(id, nombre);
    }

    @Override
    public String getTipo() {
        return "Punto de paso";
    }

    @Override
    public boolean esSeleccionable() {
        return false;
    }
}