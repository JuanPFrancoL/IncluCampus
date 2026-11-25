package org.Juan.modelo.tramo;

import org.Juan.modelo.ubicacion.Ubicacion;
import org.Juan.util.Constantes;

/**
 * Tramo que se recorre en ascensor. Su costo es la distancia mas un
 * extra fijo que representa el tiempo de espera del ascensor.
 *
 * @version 1.0
 */
public class TramoAscensor extends Tramo {

    /**
     * Crea un tramo de ascensor entre dos lugares.
     *
     * @param origen    un extremo del tramo
     * @param destino   el otro extremo del tramo
     * @param distancia largo del tramo, en metros
     */
    public TramoAscensor(Ubicacion origen, Ubicacion destino, double distancia) {
        super(origen, destino, distancia);
    }

    /**
     * @return el tipo de este tramo, que siempre es ASCENSOR
     */
    @Override
    public TipoTramo getTipo() {
        return TipoTramo.ASCENSOR;
    }

    /**
     * Calcula el costo del tramo: la distancia mas el tiempo de espera
     * del ascensor.
     *
     * @return la distancia del tramo sumada a la constante de espera del ascensor
     */
    @Override
    public double getCosto() {
        // El ascensor suma un tiempo de espera
        return getDistancia() + Constantes.ESPERA_ASCENSOR;
    }
}