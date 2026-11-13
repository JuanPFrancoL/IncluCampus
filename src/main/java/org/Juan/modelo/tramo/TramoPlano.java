package org.Juan.modelo.tramo;

import org.Juan.modelo.ubicacion.Ubicacion;

public class TramoPlano extends Tramo {

    public TramoPlano(Ubicacion origen, Ubicacion destino, double distancia) {
        super(origen, destino, distancia);
    }

    @Override
    public TipoTramo getTipo() {
        return TipoTramo.PLANO;
    }

    @Override
    public double getCosto() {
        return getDistancia();
    }
}