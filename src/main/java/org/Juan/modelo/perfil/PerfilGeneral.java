package org.Juan.modelo.perfil;

import org.Juan.modelo.tramo.Tramo;

public class PerfilGeneral extends PerfilAccesibilidad {

    public PerfilGeneral() {
        super("general", "General", "Sin restricciones");
    }

    @Override
    public boolean puedeUsar(Tramo tramo) {
        return true;
    }
    @Override
    public boolean priorizaDistancia() {
        return true;
    }
}