package org.Juan.estrategia;

import org.Juan.modelo.perfil.PerfilAccesibilidad;
import org.Juan.modelo.tramo.Tramo;

/** Busca el camino con menos metros. */
public class RutaMasCorta extends RutaBase {

    @Override
    protected double calcularCosto(Tramo tramo, PerfilAccesibilidad perfil) {
        return tramo.getDistancia();
    }
}