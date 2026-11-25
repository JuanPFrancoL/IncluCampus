package org.Juan.modelo.tramo;

import org.Juan.modelo.ubicacion.Ubicacion;

/**
 * Un camino entre dos puntos del campus. Se puede recorrer en los dos sentidos.
 * Es abstracta: cada tipo de camino (escalera, rampa, etc.) calcula su
 * "costo" a su manera. Esta clase guarda lo que todos los tramos tienen
 * en comun: sus dos extremos, su distancia y su id.
 *
 * @version 1.0
 */
public abstract class Tramo {

    /** Identificador del tramo, formado con los ids de sus dos extremos (origen-destino). */
    private final String id;

    /** Un extremo del tramo, el lugar desde donde se define el camino. */
    private final Ubicacion origen;

    /** El otro extremo del tramo. Como se puede recorrer en ambos sentidos, es solo el "otro lado". */
    private final Ubicacion destino;

    /** Largo del tramo, en metros. */
    private final double distancia; // en metros

    /**
     * Crea un tramo entre dos lugares. Es protected porque la clase es
     * abstracta: solo la usan las clases hijas al llamar a super. El id se
     * arma solo, juntando el id del origen y el del destino con un guion.
     *
     * @param origen    un extremo del tramo
     * @param destino   el otro extremo del tramo
     * @param distancia largo del tramo, en metros
     */
    protected Tramo(Ubicacion origen, Ubicacion destino, double distancia) {
        this.id = origen.getId() + "-" + destino.getId();
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
    }

    /**
     * @return el id del tramo, con la forma "idOrigen-idDestino"
     */
    public String getId() {
        return id;
    }

    /**
     * @return el lugar que es el origen del tramo
     */
    public Ubicacion getOrigen() {
        return origen;
    }

    /**
     * @return el lugar que es el destino del tramo
     */
    public Ubicacion getDestino() {
        return destino;
    }

    /**
     * @return el largo del tramo, en metros
     */
    public double getDistancia() {
        return distancia;
    }

    /**
     * Cada tipo de tramo dice de que tipo es (escalera, rampa, etc.).
     *
     * @return el tipo del tramo
     */
    public abstract TipoTramo getTipo();

    /**
     * Dice que tan "pesado" es recorrer este tramo. Cada tipo lo calcula distinto.
     *
     * @return el costo del tramo
     */
    public abstract double getCosto();

    /**
     * Arma un texto corto que describe el tramo, util para mostrarlo.
     * Ejemplo: "Pie de rampa ↔ Bloque F (rampa)".
     *
     * @return la descripcion del tramo con sus dos extremos y su tipo
     */
    public String getDescripcion() {
        return origen.getNombre() + " ↔ " + destino.getNombre() + " (" + getTipo().getEtiqueta() + ")";
    }

    /**
     * Dice si este tramo pasa por un lugar, o sea, si ese lugar es uno de
     * sus dos extremos. Compara por id, no por objeto.
     *
     * @param ubicacion el lugar que se quiere revisar
     * @return true si el lugar es el origen o el destino del tramo, false si no
     */
    public boolean conecta(Ubicacion ubicacion) {
        return origen.getId().equals(ubicacion.getId()) || destino.getId().equals(ubicacion.getId());
    }

    /**
     * Si estas en un extremo del tramo, devuelve el otro extremo.
     *
     * @param ubicacion el extremo donde estas parado
     * @return el extremo contrario del tramo
     * @throws IllegalArgumentException si el lugar no es ninguno de los dos extremos del tramo
     */
    public Ubicacion getOtroExtremo(Ubicacion ubicacion) {
        if (origen.getId().equals(ubicacion.getId())) {
            return destino;
        }
        if (destino.getId().equals(ubicacion.getId())) {
            return origen;
        }
        throw new IllegalArgumentException(ubicacion.getNombre() + " no pertenece a este tramo");
    }
}