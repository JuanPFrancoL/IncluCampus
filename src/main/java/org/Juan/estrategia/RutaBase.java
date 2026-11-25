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
 * Clase base para buscar rutas con el algoritmo de Dijkstra.
 * Aqui esta toda la logica de busqueda, y las clases hijas solo
 * le dicen cuanto cuesta cada tramo.
 *
 * @version 1.0
 */
public abstract class RutaBase implements EstrategiaRuta {

    /**
     * Dice cuanto cuesta recorrer un tramo. Cada estrategia lo
     * calcula a su manera (por ejemplo, por distancia o por accesibilidad).
     *
     * @param tramo  el tramo que se quiere evaluar
     * @param perfil el perfil de accesibilidad de la persona
     * @return el costo del tramo (no puede ser negativo)
     */
    protected abstract double calcularCosto(Tramo tramo, PerfilAccesibilidad perfil);

    /**
     * Busca el camino mas barato entre dos lugares.
     *
     * @param origen  de donde sale la persona
     * @param destino a donde quiere llegar
     * @param tramos  todos los caminos que existen
     * @param perfil  perfil de accesibilidad de la persona
     * @return la ruta con los puntos, los tramos y el costo total
     * @throws RutaNoDisponibleException si no hay forma de llegar del origen al destino
     */
    @Override
    public Ruta calcular(Ubicacion origen, Ubicacion destino, List<Tramo> tramos, PerfilAccesibilidad perfil) {
        // Guarda el costo mas bajo que llevamos para llegar a cada lugar (por id)
        Map<String, Double> costos = new HashMap<>();
        // Guarda por cual tramo llegamos a cada lugar
        Map<String, Tramo> tramoPrevio = new HashMap<>();
        // Guarda los lugares que ya terminamos de revisar
        Set<String> visitados = new HashSet<>();
        // Guarda todos los lugares que salen en los tramos
        Map<String, Ubicacion> lugares = new HashMap<>();

        for (Tramo tramo : tramos) {
            lugares.put(tramo.getOrigen().getId(), tramo.getOrigen());
            lugares.put(tramo.getDestino().getId(), tramo.getDestino());
        }

        // Empezamos en el origen, que no cuesta nada llegar a el
        costos.put(origen.getId(), 0.0);

        while (true) {
            Ubicacion actual = elegirElMasBarato(lugares, costos, visitados);

            if (actual == null) {
                break; // ya no queda nada por revisar
            }
            if (actual.getId().equals(destino.getId())) {
                break; // llegamos al destino, listo
            }

            visitados.add(actual.getId());

            // Miramos todos los caminos que salen de este lugar
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

                // Si este camino es mas barato que el que teniamos, nos quedamos con este
                if (nuevoCosto < costoAnterior) {
                    costos.put(vecino.getId(), nuevoCosto);
                    tramoPrevio.put(vecino.getId(), tramo);
                }
            }
        }

        // Si nunca pudimos llegar al destino, es que no hay camino
        if (!costos.containsKey(destino.getId())) {
            throw new RutaNoDisponibleException(origen.getNombre(), destino.getNombre());
        }

        return armarRuta(origen, destino, tramoPrevio, costos.get(destino.getId()));
    }

    /**
     * Entre los lugares que faltan por revisar, escoge el que tenga
     * el costo mas bajo.
     *
     * @param lugares   todos los lugares, buscados por id
     * @param costos    el costo mas bajo conocido para cada lugar
     * @param visitados los lugares que ya se revisaron
     * @return el lugar mas barato, o null si no queda ninguno
     */
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

    /**
     * Arma la ruta final. Empieza desde el destino y va devolviendose
     * hasta el origen, y al final le da la vuelta para dejarla en orden.
     *
     * @param origen      de donde sale la ruta
     * @param destino     donde termina la ruta
     * @param tramoPrevio por cual tramo se llego a cada lugar
     * @param costoTotal  lo que cuesta en total todo el camino
     * @return la ruta en orden, del origen al destino
     */
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

        // Quedo al reves (destino a origen), asi que la volteamos
        Collections.reverse(puntos);
        Collections.reverse(camino);

        return new Ruta(puntos, camino, costoTotal);
    }
}