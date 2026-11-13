package org.Juan.modelo.tramo;

import org.Juan.modelo.ubicacion.Ubicacion;

/**
 * Un camino entre dos puntos del campus. Se puede recorrer en los dos sentidos.
 * Es abstracta: cada tipo de camino calcula su "costo" a su manera.
 */
public abstract class Tramo {

    private final String id;
    private final Ubicacion origen;
    private final Ubicacion destino;
    private final double distancia; // en metros

    protected Tramo(Ubicacion origen, Ubicacion destino, double distancia) {
        this.id = origen.getId() + "-" + destino.getId();
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
    }

    public String getId() {
        return id;
    }

    public Ubicacion getOrigen() {
        return origen;
    }

    public Ubicacion getDestino() {
        return destino;
    }

    public double getDistancia() {
        return distancia;
    }

    public abstract TipoTramo getTipo();

    /** Qué tan "pesado" es recorrer este tramo. Cada tipo lo calcula distinto. */
    public abstract double getCosto();

    /** Ejemplo: "Pie de rampa ↔ Bloque F (rampa)". */
    public String getDescripcion() {
        return origen.getNombre() + " ↔ " + destino.getNombre() + " (" + getTipo().getEtiqueta() + ")";
    }

    /** Dice si este tramo pasa por ese lugar. */
    public boolean conecta(Ubicacion ubicacion) {
        return origen.getId().equals(ubicacion.getId()) || destino.getId().equals(ubicacion.getId());
    }

    /** Si estás en un extremo del tramo, devuelve el otro. */
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