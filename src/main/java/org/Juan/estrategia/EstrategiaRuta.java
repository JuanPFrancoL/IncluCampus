package org.Juan.estrategia;

import org.Juan.modelo.Ruta;
import org.Juan.modelo.perfil.PerfilAccesibilidad;
import org.Juan.modelo.tramo.Tramo;
import org.Juan.modelo.ubicacion.Ubicacion;

import java.util.List;

/**
 * Representa una forma de calcular una ruta. Cada clase que la implemente
 * decide con que criterio se escoge el camino (el mas corto, el mas
 * accesible, etc.), asi se puede cambiar de estrategia sin tocar el resto
 * del programa.
 *
 * @version 1.0
 */
public interface EstrategiaRuta {

    /**
     * Calcula la mejor ruta entre dos lugares, usando solo los tramos
     * que se le entregan.
     *
     * @param origen  de donde sale la persona
     * @param destino a donde quiere llegar
     * @param tramos  los caminos disponibles para armar la ruta
     * @param perfil  perfil de accesibilidad de la persona
     * @return la ruta encontrada, con sus puntos, tramos y costo total
     * @throws org.Juan.excepcion.RutaNoDisponibleException si no hay camino entre el origen y el destino
     */
    Ruta calcular(Ubicacion origen, Ubicacion destino, List<Tramo> tramos, PerfilAccesibilidad perfil);
}