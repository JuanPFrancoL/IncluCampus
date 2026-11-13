package org.Juan.modelo.perfil;

import org.Juan.modelo.tramo.TipoTramo;
import org.Juan.modelo.tramo.Tramo;
import org.Juan.util.Constantes;

public class PerfilVisual extends PerfilAccesibilidad {

    public PerfilVisual() {
        super("visual", "Visual", "Prefiere caminos planos, directos y señalizados");
    }

    @Override
    public boolean puedeUsar(Tramo tramo) {
        // Puede usar todos los caminos, pero algunos los prefiere menos
        return true;
    }

    @Override
    public double calcularCosto(Tramo tramo) {
        if (tramo.getTipo() == TipoTramo.ESCALERA) {
            // Las escaleras resultan más riesgosas: pesan más
            return super.calcularCosto(tramo) * Constantes.PENALIZACION_ESCALERA_VISUAL;
        }
        return super.calcularCosto(tramo);
    }
}