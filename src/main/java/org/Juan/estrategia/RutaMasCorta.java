package org.Juan.estrategia;

import org.Juan.modelo.perfil.PerfilAccesibilidad;
import org.Juan.modelo.tramo.Tramo;

/**
 * Busca el camino con menos metros, sin importar que tan facil o dificil
 * sea para la persona. No tiene logica de busqueda propia: usa la de
 * RutaBase y solo define que el costo de un tramo es su distancia.
 *
 * @version 1.0
 */
public class RutaMasCorta extends RutaBase {

    /**
     * Calcula el costo de un tramo segun su distancia. Mientras mas largo
     * sea el tramo, mas cuesta. El perfil de accesibilidad no se usa aqui,
     * pero hay que recibirlo porque asi lo pide el metodo de la clase base.
     *
     * @param tramo  el tramo que se quiere evaluar
     * @param perfil el perfil de la persona (se ignora en esta estrategia)
     * @return la distancia del tramo, que es su costo
     */
    @Override
    protected double calcularCosto(Tramo tramo, PerfilAccesibilidad perfil) {
        return tramo.getDistancia();
    }
}