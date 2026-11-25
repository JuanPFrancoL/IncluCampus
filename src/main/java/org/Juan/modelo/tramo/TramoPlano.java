package org.Juan.modelo.tramo;

import org.Juan.modelo.ubicacion.Ubicacion;

/**
 * Tramo que se recorre en terreno plano, sin escaleras ni pendientes.
 * Es el caso mas simple: su costo es igual a su distancia, sin extras.
 *
 * @version 1.0
 */
public class TramoPlano extends Tramo {

    /**
     * Crea un tramo plano entre dos lugares.
     *
     * @param origen    un extremo del tramo
     * @param destino   el otro extremo del tramo
     * @param distancia largo del tramo, en metros
     */
    public TramoPlano(Ubicacion origen, Ubicacion destino, double distancia) {
        super(origen, destino, distancia);
    }

    /**
     * @return el tipo de este tramo, que siempre es PLANO
     */
    @Override
    public TipoTramo getTipo() {
        return TipoTramo.PLANO;
    }

    /**
     * Calcula el costo del tramo. Como es plano no tiene recargos, asi
     * que el costo es simplemente su distancia.
     *
     * @return la distancia del tramo
     */
    @Override
    public double getCosto() {
        return getDistancia();
    }
}