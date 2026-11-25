package org.Juan.servicio;

import org.Juan.modelo.tramo.Tramo;
import org.Juan.modelo.ubicacion.Ubicacion;
import org.Juan.repositorio.RepositorioCampus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * Servicio que da la informacion general del campus: los lugares que la
 * persona puede escoger y los tramos que forman el mapa. Es el que esta
 * en medio entre los controladores y el repositorio del campus.
 *
 * @version 1.0
 */

@Service
public class ServicioCampus {
    /** Repositorio donde esta guardado el mapa del campus. */
    private final RepositorioCampus repositorio;

    /**
     * Crea el servicio con el repositorio que necesita. Spring lo entrega
     * solo al arrancar la aplicacion.
     *
     * @param repositorio repositorio del campus
     */
    public ServicioCampus(RepositorioCampus repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Devuelve los lugares que la persona puede elegir como origen o
     * destino. Recorre todas las ubicaciones y se queda solo con las que
     * son seleccionables, o sea que deja por fuera los puntos de paso.
     *
     * @return la lista de lugares seleccionables
     */
    public List<Ubicacion> listarLugaresSeleccionables() {
        List<Ubicacion> resultado = new ArrayList<>();
        for (Ubicacion ubicacion : repositorio.obtenerTodas()) {
            if (ubicacion.esSeleccionable()) {
                resultado.add(ubicacion);
            }
        }
        return resultado;
    }

    /**
     * Devuelve todos los tramos del campus, tal como los entrega el repositorio.
     * No filtra los tramos bloqueados.
     *
     * @return la lista de tramos
     */
    public List<Tramo> listarTramos() {
        return repositorio.obtenerTramos();
    }
}