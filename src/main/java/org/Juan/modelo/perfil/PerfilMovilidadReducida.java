package org.Juan.modelo.perfil;

import org.Juan.modelo.tramo.TipoTramo;
import org.Juan.modelo.tramo.Tramo;

/**
 * Perfil para personas con movilidad reducida (por ejemplo, quienes usan
 * silla de ruedas). Descarta las escaleras por completo y deja que la
 * ruta pase por rampas y ascensores.
 *
 * @version 1.0
 */
public class PerfilMovilidadReducida extends PerfilAccesibilidad {

    /**
     * Crea el perfil de movilidad reducida con sus datos fijos: codigo
     * "movilidad", nombre "Movilidad reducida" y la descripcion
     * "Evita escaleras; usa rampas y ascensores". No recibe parametros
     * porque estos datos nunca cambian.
     */
    public PerfilMovilidadReducida() {
        super("movilidad", "Movilidad reducida", "Evita escaleras; usa rampas y ascensores");
    }

    /**
     * Dice si este perfil puede pasar por un tramo. Todo se puede usar
     * menos las escaleras, que quedan descartadas por completo.
     *
     * @param tramo el tramo que se quiere revisar
     * @return false si el tramo es una escalera, true en cualquier otro caso
     */
    @Override
    public boolean puedeUsar(Tramo tramo) {
        // Las escaleras quedan descartadas por completo
        return tramo.getTipo() != TipoTramo.ESCALERA;
    }
}