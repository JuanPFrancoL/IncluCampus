package org.Juan.modelo;

import org.Juan.modelo.tramo.Tramo;
import org.Juan.modelo.ubicacion.Ubicacion;

import java.util.List;

/** El resultado: los lugares por los que pasa la ruta y los tramos que la forman. */
public class Ruta {

    private final List<Ubicacion> puntos;   // de origen a destino, en orden
    private final List<Tramo> tramos;       // los caminos entre esos puntos
    private final double costoTotal;

    public Ruta(List<Ubicacion> puntos, List<Tramo> tramos, double costoTotal) {
        this.puntos = puntos;
        this.tramos = tramos;
        this.costoTotal = costoTotal;
    }

    public List<Ubicacion> getPuntos() {
        return puntos;
    }

    public List<Tramo> getTramos() {
        return tramos;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    /** Suma de las distancias de todos los tramos, en metros. */
    public double getDistanciaTotal() {
        double total = 0;
        for (Tramo tramo : tramos) {
            total += tramo.getDistancia();
        }
        return total;
    }
}