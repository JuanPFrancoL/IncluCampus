package org.Juan.estrategia;

import org.Juan.modelo.Ruta;
import org.Juan.modelo.perfil.PerfilAccesibilidad;
import org.Juan.modelo.tramo.Tramo;
import org.Juan.modelo.ubicacion.Ubicacion;

import java.util.List;

/** Una forma de calcular una ruta. */
public interface EstrategiaRuta {

    /**
     * Calcula la mejor ruta usando solo los tramos que se le entregan.
     * Lanza RutaNoDisponibleException si no hay camino.
     */
    Ruta calcular(Ubicacion origen, Ubicacion destino, List<Tramo> tramos, PerfilAccesibilidad perfil);
}