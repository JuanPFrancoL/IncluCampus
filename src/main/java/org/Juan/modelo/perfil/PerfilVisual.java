package org.Juan.modelo.perfil;

import org.Juan.modelo.tramo.TipoTramo;
import org.Juan.modelo.tramo.Tramo;
import org.Juan.util.Constantes;

/**
 * Perfil para personas con discapacidad visual. Puede pasar por cualquier
 * tramo, pero prefiere caminos planos, directos y senalizados. Por eso las
 * escaleras no se descartan, solo se vuelven mas "caras" para que la ruta
 * las evite cuando haya otra opcion.
 *
 * @version 1.0
 */
public class PerfilVisual extends PerfilAccesibilidad {

    /**
     * Crea el perfil visual con sus datos fijos: codigo "visual", nombre
     * "Visual" y la descripcion "Prefiere caminos planos, directos y
     * senalizados". No recibe parametros porque estos datos nunca cambian.
     */
    public PerfilVisual() {
        super("visual", "Visual", "Prefiere caminos planos, directos y señalizados");
    }

    /**
     * Este perfil puede usar todos los caminos, aunque algunos los
     * prefiere menos (eso se maneja en {@link #calcularCosto(Tramo)}).
     *
     * @param tramo el tramo que se quiere revisar (no importa cual sea)
     * @return true siempre
     */
    @Override
    public boolean puedeUsar(Tramo tramo) {
        // Puede usar todos los caminos, pero algunos los prefiere menos
        return true;
    }

    /**
     * Calcula que tan dificil le resulta un tramo a este perfil. Las
     * escaleras cuestan mas, porque son mas riesgosas: se multiplica su
     * costo normal por una penalizacion. Los demas tramos cuestan lo normal.
     *
     * @param tramo el tramo que se quiere evaluar
     * @return el costo normal del tramo, o ese costo multiplicado por la
     *         penalizacion si es una escalera
     */
    @Override
    public double calcularCosto(Tramo tramo) {
        if (tramo.getTipo() == TipoTramo.ESCALERA) {
            // Las escaleras resultan más riesgosas: pesan más
            return super.calcularCosto(tramo) * Constantes.PENALIZACION_ESCALERA_VISUAL;
        }
        return super.calcularCosto(tramo);
    }
}