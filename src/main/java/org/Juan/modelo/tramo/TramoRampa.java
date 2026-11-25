package org.Juan.modelo.tramo;

import org.Juan.modelo.ubicacion.Ubicacion;
import org.Juan.util.Constantes;

/**
 * Tramo que se recorre por una rampa. Su costo es la distancia
 * multiplicada por un factor fijo, porque una rampa suele ser un poco
 * mas pesada que caminar en plano, pero mucho mas facil que una escalera.
 *
 * @version 1.0
 */
public class TramoRampa extends Tramo {

    /**
     * Crea un tramo de rampa entre dos lugares.
     *
     * @param origen    un extremo del tramo
     * @param destino   el otro extremo del tramo
     * @param distancia largo del tramo, en metros
     */
    public TramoRampa(Ubicacion origen, Ubicacion destino, double distancia) {
        super(origen, destino, distancia);
    }

    /**
     * @return el tipo de este tramo, que siempre es RAMPA
     */
    @Override
    public TipoTramo getTipo() {
        return TipoTramo.RAMPA;
    }

    /**
     * Calcula el costo del tramo: la distancia multiplicada por el factor
     * de las rampas.
     *
     * @return la distancia del tramo por la constante de la rampa
     */
    @Override
    public double getCosto() {
        return getDistancia() * Constantes.FACTOR_RAMPA;
    }
}