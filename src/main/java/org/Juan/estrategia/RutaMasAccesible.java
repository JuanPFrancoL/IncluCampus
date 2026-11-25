package org.Juan.estrategia;

import org.Juan.modelo.perfil.PerfilAccesibilidad;
import org.Juan.modelo.tramo.Tramo;

/**
 * Busca el camino mas facil para la persona, segun su perfil de accesibilidad.
 * No tiene logica de busqueda propia: usa la de RutaBase y solo define
 * como se calcula el costo de cada tramo.
 *
 * @version 1.0
 */
public class RutaMasAccesible extends RutaBase {

    /**
     * Calcula el costo de un tramo segun el perfil de la persona.
     * La cuenta la hace el mismo perfil, asi que un tramo puede costar mas
     * o menos dependiendo de las necesidades de cada quien.
     *
     * @param tramo  el tramo que se quiere evaluar
     * @param perfil el perfil de accesibilidad de la persona
     * @return el costo del tramo segun ese perfil
     */
    @Override
    protected double calcularCosto(Tramo tramo, PerfilAccesibilidad perfil) {
        return perfil.calcularCosto(tramo);
    }
}