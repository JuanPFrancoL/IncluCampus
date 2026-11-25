package org.Juan.estrategia;

import org.Juan.excepcion.RutaNoDisponibleException;
import org.Juan.modelo.Ruta;
import org.Juan.modelo.perfil.PerfilAccesibilidad;
import org.Juan.modelo.perfil.PerfilGeneral;
import org.Juan.modelo.perfil.PerfilVisual;
import org.Juan.modelo.tramo.TipoTramo;
import org.Juan.modelo.tramo.Tramo;
import org.Juan.modelo.tramo.TramoEscalera;
import org.Juan.modelo.tramo.TramoPlano;
import org.Juan.modelo.ubicacion.Bloque;
import org.Juan.modelo.ubicacion.Ubicacion;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mapa de prueba:
 *
 *   A --escalera (10 m)-- B
 *   A --plano (20 m)-- C --plano (20 m)-- B
 *
 * Para el perfil general la escalera cuesta 10 * 3 = 30 y el rodeo cuesta 40.
 * Para el perfil visual la escalera cuesta 10 * 3 * 2 = 60, así que conviene el rodeo.
 */
class RutaMasAccesibleTest {

    private final Ubicacion a = new Bloque("a", "Bloque A");
    private final Ubicacion b = new Bloque("b", "Bloque B");
    private final Ubicacion c = new Bloque("c", "Bloque C");

    private final List<Tramo> tramos = List.of(
            new TramoEscalera(a, b, 10),
            new TramoPlano(a, c, 20),
            new TramoPlano(c, b, 20)
    );

    private final RutaMasAccesible estrategia = new RutaMasAccesible();

    @Test
    void perfilGeneralTomaLaEscaleraSiEsMasBarata() {
        PerfilAccesibilidad perfil = new PerfilGeneral();

        Ruta ruta = estrategia.calcular(a, b, tramos, perfil);

        assertEquals(1, ruta.getTramos().size());
        assertEquals(TipoTramo.ESCALERA, ruta.getTramos().get(0).getTipo());
        assertEquals(30.0, ruta.getCostoTotal(), 0.001);
    }

    @Test
    void perfilVisualRodeaLaEscalera() {
        PerfilAccesibilidad perfil = new PerfilVisual();

        Ruta ruta = estrategia.calcular(a, b, tramos, perfil);

        assertEquals(2, ruta.getTramos().size());
        assertEquals(40.0, ruta.getDistanciaTotal(), 0.001);
        assertEquals(40.0, ruta.getCostoTotal(), 0.001);
    }

    @Test
    void laRutaListaLosPuntosEnOrdenDeOrigenADestino() {
        Ruta ruta = estrategia.calcular(a, b, tramos, new PerfilVisual());

        assertEquals(List.of("a", "c", "b"),
                ruta.getPuntos().stream().map(Ubicacion::getId).toList());
    }

    @Test
    void sinTramosQueLleguenAlDestinoDaError() {
        List<Tramo> soloHastaC = List.of(new TramoPlano(a, c, 20));

        assertThrows(RutaNoDisponibleException.class,
                () -> estrategia.calcular(a, b, soloHastaC, new PerfilGeneral()));
    }
}