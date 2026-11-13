package org.Juan.modelo.tramo;

import org.Juan.modelo.ubicacion.Ubicacion;
import org.Juan.util.Constantes;

public class TramoEscalera extends Tramo {

    public TramoEscalera(Ubicacion origen, Ubicacion destino, double distancia) {
        super(origen, destino, distancia);
    }

    @Override
    public TipoTramo getTipo() {
        return TipoTramo.ESCALERA;
    }

    @Override
    public double getCosto() {
        return getDistancia() * Constantes.FACTOR_ESCALERA;
    }
}