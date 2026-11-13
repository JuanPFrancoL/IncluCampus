package org.Juan.estrategia;

import org.Juan.modelo.perfil.PerfilAccesibilidad;
import org.Juan.modelo.tramo.Tramo;

/** Busca el camino más fácil según el perfil de la persona. */
public class RutaMasAccesible extends RutaBase {

    @Override
    protected double calcularCosto(Tramo tramo, PerfilAccesibilidad perfil) {
        return perfil.calcularCosto(tramo);
    }
}