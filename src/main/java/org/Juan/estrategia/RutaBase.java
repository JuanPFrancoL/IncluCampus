package org.Juan.estrategia;

import org.Juan.excepcion.RutaNoDisponibleException;
import org.Juan.modelo.Ruta;
import org.Juan.modelo.perfil.PerfilAccesibilidad;
import org.Juan.modelo.tramo.Tramo;
import org.Juan.modelo.ubicacion.Ubicacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tiene el algoritmo de búsqueda (Dijkstra).
 * Las hijas solo definen cuánto cuesta cada tramo.
 */
public abstract class RutaBase implements EstrategiaRuta {

    /** Cada estrategia dice cuánto cuesta recorrer un tramo. */
    protected abstract double calcularCosto(Tramo tramo, PerfilAccesibilidad perfil);

    @Override
    public Ruta calcular(Ubicacion origen, Ubicacion destino, List<Tramo> tramos, PerfilAccesibilidad perfil) {
        // Costo mínimo conocido para llegar a cada lugar (por id)
        Map<String, Double> costos = new HashMap<>();
        // Por qué tramo llegamos a cada lugar
        Map<String, Tramo> tramoPrevio = new HashMap<>();
        // Lugares ya resueltos
        Set<String> visitados = new HashSet<>();
        // Todos los lugares que aparecen en los tramos
        Map<String, Ubicacion> lugares = new HashMap<>();

        for (Tramo tramo : tramos) {
            lugares.put(tramo.getOrigen().getId(), tramo.getOrigen());
            lugares.put(tramo.getDestino().getId(), tramo.getDestino());
        }

        costos.put(origen.getId(), 0.0);

        while (true) {
            Ubicacion actual = elegirElMasBarato(lugares, costos, visitados);

            if (actual == null) {
                break; // no queda nada por explorar
            }
            if (actual.getId().equals(destino.getId())) {
                break; // ya llegamos al destino
            }

            visitados.add(actual.getId());

            // Revisamos los caminos que salen de este lugar
            for (Tramo tramo : tramos) {
                if (!tramo.conecta(actual)) {
                    continue;
                }
                Ubicacion vecino = tramo.getOtroExtremo(actual);
                if (visitados.contains(vecino.getId())) {
                    continue;
                }

                double nuevoCosto = costos.get(actual.getId()) + calcularCosto(tramo, perfil);
                double costoAnterior = costos.getOrDefault(vecino.getId(), Double.POSITIVE_INFINITY);

                if (nuevoCosto < costoAnterior) {
                    costos.put(vecino.getId(), nuevoCosto);
                    tramoPrevio.put(vecino.getId(), tramo);
                }
            }
        }

        // Si el destino nunca recibió costo, no hay camino
        if (!costos.containsKey(destino.getId())) {
            throw new RutaNoDisponibleException(origen.getNombre(), destino.getNombre());
        }

        return armarRuta(origen, destino, tramoPrevio, costos.get(destino.getId()));
    }

    /** Entre los lugares aún no resueltos, devuelve el de menor costo (o null si no hay). */
    private Ubicacion elegirElMasBarato(Map<String, Ubicacion> lugares,
                                        Map<String, Double> costos,
                                        Set<String> visitados) {
        Ubicacion mejor = null;
        double mejorCosto = Double.POSITIVE_INFINITY;

        for (Ubicacion lugar : lugares.values()) {
            String id = lugar.getId();
            if (visitados.contains(id) || !costos.containsKey(id)) {
                continue;
            }
            if (costos.get(id) < mejorCosto) {
                mejor = lugar;
                mejorCosto = costos.get(id);
            }
        }
        return mejor;
    }

    /** Va desde el destino hacia atrás hasta el origen y arma la ruta en orden. */
    private Ruta armarRuta(Ubicacion origen, Ubicacion destino,
                           Map<String, Tramo> tramoPrevio, double costoTotal) {
        List<Ubicacion> puntos = new ArrayList<>();
        List<Tramo> camino = new ArrayList<>();

        Ubicacion actual = destino;
        puntos.add(actual);

        while (!actual.getId().equals(origen.getId())) {
            Tramo tramo = tramoPrevio.get(actual.getId());
            camino.add(tramo);
            actual = tramo.getOtroExtremo(actual);
            puntos.add(actual);
        }

        // Lo armamos al revés, así que lo damos vuelta
        Collections.reverse(puntos);
        Collections.reverse(camino);

        return new Ruta(puntos, camino, costoTotal);
    }
}