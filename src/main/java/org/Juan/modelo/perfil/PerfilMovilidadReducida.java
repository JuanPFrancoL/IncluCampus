package org.Juan.modelo.perfil;

import org.Juan.modelo.tramo.TipoTramo;
import org.Juan.modelo.tramo.Tramo;

public class PerfilMovilidadReducida extends PerfilAccesibilidad {

    public PerfilMovilidadReducida() {
        super("movilidad", "Movilidad reducida", "Evita escaleras; usa rampas y ascensores");
    }

    @Override
    public boolean puedeUsar(Tramo tramo) {
        // Las escaleras quedan descartadas por completo
        return tramo.getTipo() != TipoTramo.ESCALERA;
    }
}