package org.Juan.modelo.tramo;

import org.Juan.modelo.ubicacion.Ubicacion;
import org.Juan.util.Constantes;

/**
 * Tramo que se recorre por una escalera. Su costo es la distancia
 * multiplicada por un factor fijo, porque subir o bajar escaleras
 * cuesta mas esfuerzo que caminar en plano.
 *
 * @version 1.0
 */
public class TramoEscalera extends Tramo {

    /**
     * Crea un tramo de escalera entre dos lugares.
     *
     * @param origen    un extremo del tramo
     * @param destino   el otro extremo del tramo
     * @param distancia largo del tramo, en metros
     */
    public TramoEscalera(Ubicacion origen, Ubicacion destino, double distancia) {
        super(origen, destino, distancia);
    }

    /**
     * @return el tipo de este tramo, que siempre es ESCALERA
     */
    @Override
    public TipoTramo getTipo() {
        return TipoTramo.ESCALERA;
    }

    /**
     * Calcula el costo del tramo: la distancia multiplicada por el factor
     * de las escaleras.
     *
     * @return la distancia del tramo por la constante de la escalera
     */
    @Override
    public double getCosto() {
        return getDistancia() * Constantes.FACTOR_ESCALERA;
    }
}