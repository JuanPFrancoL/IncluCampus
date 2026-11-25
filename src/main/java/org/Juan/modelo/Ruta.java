package org.Juan.modelo;

import org.Juan.modelo.tramo.Tramo;
import org.Juan.modelo.ubicacion.Ubicacion;

import java.util.List;

/**
 * Es el resultado de calcular una ruta: guarda los lugares por los que
 * pasa la persona, los tramos que hay entre esos lugares y cuanto cuesta
 * en total el recorrido. Una vez creada no se puede modificar.
 *
 * @version 1.0
 */
public class Ruta {

    /** Lugares por los que pasa la ruta, en orden de origen a destino. */
    private final List<Ubicacion> puntos;   // de origen a destino, en orden

    /** Caminos que conectan esos lugares, en el mismo orden del recorrido. */
    private final List<Tramo> tramos;       // los caminos entre esos puntos

    /** Costo total de la ruta, segun la estrategia que se uso para calcularla. */
    private final double costoTotal;

    /**
     * Crea una ruta con los lugares, los tramos y el costo ya calculados.
     *
     * @param puntos     lugares por los que pasa la ruta, de origen a destino
     * @param tramos     caminos que unen esos lugares
     * @param costoTotal costo total del recorrido
     */
    public Ruta(List<Ubicacion> puntos, List<Tramo> tramos, double costoTotal) {
        this.puntos = puntos;
        this.tramos = tramos;
        this.costoTotal = costoTotal;
    }

    /**
     * @return los lugares por los que pasa la ruta, en orden de origen a destino
     */
    public List<Ubicacion> getPuntos() {
        return puntos;
    }

    /**
     * @return los tramos que forman la ruta, en orden
     */
    public List<Tramo> getTramos() {
        return tramos;
    }

    /**
     * @return el costo total de la ruta (no es lo mismo que la distancia,
     *         depende de la estrategia usada)
     */
    public double getCostoTotal() {
        return costoTotal;
    }

    /**
     * Suma las distancias de todos los tramos de la ruta.
     *
     * @return la distancia total del recorrido, en metros
     */
    public double getDistanciaTotal() {
        double total = 0;
        for (Tramo tramo : tramos) {
            total += tramo.getDistancia();
        }
        return total;
    }
}