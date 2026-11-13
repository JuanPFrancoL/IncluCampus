package org.Juan.modelo.tramo;

import org.Juan.modelo.ubicacion.Ubicacion;
import org.Juan.util.Constantes;

public class TramoAscensor extends Tramo {

    public TramoAscensor(Ubicacion origen, Ubicacion destino, double distancia) {
        super(origen, destino, distancia);
    }

    @Override
    public TipoTramo getTipo() {
        return TipoTramo.ASCENSOR;
    }

    @Override
    public double getCosto() {
        // El ascensor suma un tiempo de espera
        return getDistancia() + Constantes.ESPERA_ASCENSOR;
    }
}