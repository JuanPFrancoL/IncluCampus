package org.Juan.modelo.ubicacion;

/**
 * Un punto intermedio del camino: una entrada, una esquina, el pie de una rampa.
 * Sirve para dibujar caminos realistas, pero nadie va "hacia" el: no se
 * puede escoger como origen ni como destino. No guarda nada propio: el id
 * y el nombre los guarda {@link Ubicacion}.
 *
 * @version 1.0
 */
public class PuntoPaso extends Ubicacion {

    /**
     * Crea un punto de paso con su id y su nombre.
     *
     * @param id     identificador unico del punto
     * @param nombre nombre del punto (por ejemplo, "Pie de rampa")
     */
    public PuntoPaso(String id, String nombre) {
        super(id, nombre);
    }

    /**
     * @return el tipo de ubicacion, que siempre es "Punto de paso"
     */
    @Override
    public String getTipo() {
        return "Punto de paso";
    }

    /**
     * Dice si este lugar se puede escoger como origen o destino de una
     * ruta. Los puntos de paso solo sirven para pasar por ellos, asi que no.
     *
     * @return false siempre
     */
    @Override
    public boolean esSeleccionable() {
        return false;
    }
}