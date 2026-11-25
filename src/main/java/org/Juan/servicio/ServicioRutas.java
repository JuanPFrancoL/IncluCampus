package org.Juan.servicio;

import org.Juan.estrategia.EstrategiaRuta;
import org.Juan.estrategia.RutaMasAccesible;
import org.Juan.estrategia.RutaMasCorta;
import org.Juan.modelo.Ruta;
import org.Juan.modelo.perfil.PerfilAccesibilidad;
import org.Juan.modelo.perfil.PerfilGeneral;
import org.Juan.modelo.perfil.PerfilMovilidadReducida;
import org.Juan.modelo.perfil.PerfilVisual;
import org.Juan.modelo.tramo.Tramo;
import org.Juan.modelo.ubicacion.Ubicacion;
import org.Juan.repositorio.RepositorioBloqueos;
import org.Juan.repositorio.RepositorioCampus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * Servicio que arma las rutas del campus. Junta todas las piezas: busca los
 * lugares, crea el perfil de la persona, descarta los tramos que ese perfil
 * no puede usar o que estan bloqueados, escoge la estrategia correcta
 * (la mas corta o la mas accesible) y le pide que calcule la ruta.
 *
 * @version 1.0
 */

@Service
public class ServicioRutas {

    /** Repositorio donde esta guardado el mapa del campus. */
    private final RepositorioCampus repositorio;
    /** Repositorio para saber que tramos tienen un bloqueo activo. */
    private final RepositorioBloqueos repositorioBloqueos;
    /** Estrategia que busca el camino con menos metros. */
    private final EstrategiaRuta rutaMasCorta = new RutaMasCorta();
    /** Estrategia que busca el camino mas facil segun el perfil de la persona. */
    private final EstrategiaRuta rutaMasAccesible = new RutaMasAccesible();
    /**
     * Crea el servicio con los repositorios que necesita. Spring los
     * entrega solo al arrancar la aplicacion. Las dos estrategias se
     * crean aqui mismo, porque no necesitan nada para funcionar.
     *
     * @param repositorio         repositorio del campus
     * @param repositorioBloqueos repositorio de bloqueos
     */

    public ServicioRutas(RepositorioCampus repositorio, RepositorioBloqueos repositorioBloqueos) {
        this.repositorio = repositorio;
        this.repositorioBloqueos = repositorioBloqueos;
    }
    /**
     * Busca la mejor ruta entre dos lugares para un perfil de accesibilidad.
     *
     * <p>Los pasos son: revisar que origen y destino sean distintos,
     * buscar los dos lugares, crear el perfil, quedarse solo con los tramos
     * disponibles (que el perfil pueda usar y que no tengan bloqueo
     * activo), escoger la estrategia segun el perfil y calcular la ruta.</p>
     *
     * @param idOrigen     id del lugar de partida
     * @param idDestino    id del lugar de llegada
     * @param codigoPerfil codigo del perfil ("general", "movilidad" o "visual")
     * @return la ruta encontrada, con sus puntos, tramos y costo total
     * @throws IllegalArgumentException si el origen y el destino son el mismo, o si el perfil no existe
     * @throws org.Juan.excepcion.UbicacionNoEncontradaException si el origen o el destino no existen
     * @throws org.Juan.excepcion.RutaNoDisponibleException si no hay camino con los tramos disponibles
     */

    public Ruta buscarRuta(String idOrigen, String idDestino, String codigoPerfil) {
        if (idOrigen.equals(idDestino)) {
            throw new IllegalArgumentException("El origen y el destino son el mismo lugar.");
        }

        Ubicacion origen = repositorio.buscarPorId(idOrigen);
        Ubicacion destino = repositorio.buscarPorId(idDestino);
        PerfilAccesibilidad perfil = crearPerfil(codigoPerfil);

        List<Tramo> disponibles = new ArrayList<>();
        for (Tramo tramo : repositorio.obtenerTramos()) {
            if (perfil.puedeUsar(tramo) && !repositorioBloqueos.estaTramoBloqueado(tramo.getId())) {
                disponibles.add(tramo);
            }
        }

        EstrategiaRuta estrategia = perfil.priorizaDistancia() ? rutaMasCorta : rutaMasAccesible;
        return estrategia.calcular(origen, destino, disponibles, perfil);
    }

    /**
     * Convierte el codigo de un perfil (un texto) en el objeto del perfil.
     *
     * @param codigo codigo del perfil: "movilidad", "visual" o "general"
     * @return el perfil que corresponde a ese codigo
     * @throws IllegalArgumentException si el codigo no es ninguno de los tres
     */
    private PerfilAccesibilidad crearPerfil(String codigo) {
        switch (codigo) {
            case "movilidad":
                return new PerfilMovilidadReducida();
            case "visual":
                return new PerfilVisual();
            case "general":
                return new PerfilGeneral();
            default:
                throw new IllegalArgumentException("Perfil desconocido: " + codigo);
        }
    }
}