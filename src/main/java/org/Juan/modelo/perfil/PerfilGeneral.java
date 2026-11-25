package org.Juan.modelo.perfil;

import org.Juan.modelo.tramo.Tramo;

/**
 * Perfil para personas sin ninguna restriccion de accesibilidad.
 * Puede usar cualquier tramo y lo unico que le importa es llegar por
 * el camino mas corto.
 *
 * @version 1.0
 */
public class PerfilGeneral extends PerfilAccesibilidad {

    /**
     * Crea el perfil general con sus datos fijos: codigo "general",
     * nombre "General" y la descripcion "Sin restricciones". No recibe
     * parametros porque estos datos nunca cambian.
     */
    public PerfilGeneral() {
        super("general", "General", "Sin restricciones");
    }

    /**
     * Este perfil no tiene restricciones, asi que puede pasar por cualquier tramo.
     *
     * @param tramo el tramo que se quiere revisar (no importa cual sea)
     * @return true siempre
     */
    @Override
    public boolean puedeUsar(Tramo tramo) {
        return true;
    }

    /**
     * Este perfil solo busca el camino mas corto.
     *
     * @return true siempre
     */
    @Override
    public boolean priorizaDistancia() {
        return true;
    }
}