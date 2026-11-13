package org.Juan.servicio;

import org.Juan.excepcion.UbicacionNoEncontradaException;
import org.Juan.modelo.Ruta;
import org.Juan.modelo.tramo.TipoTramo;
import org.Juan.modelo.tramo.Tramo;
import org.Juan.repositorio.RepositorioCampusMemoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServicioRutasTest {

    private final ServicioRutas servicio = new ServicioRutas(new RepositorioCampusMemoria());

    @Test
    void movilidadReducidaVaPorLaRampaYSinEscaleras() {
        Ruta ruta = servicio.buscarRuta("BIBLIOTECA", "BLOQUE_F", "movilidad");

        assertEquals(165.0, ruta.getDistanciaTotal(), 0.001);
        for (Tramo tramo : ruta.getTramos()) {
            assertNotEquals(TipoTramo.ESCALERA, tramo.getTipo());
        }
    }

    @Test
    void perfilGeneralVaPorElCaminoMasCorto() {
        Ruta ruta = servicio.buscarRuta("BIBLIOTECA", "BLOQUE_F", "general");

        assertEquals(95.0, ruta.getDistanciaTotal(), 0.001);
    }

    @Test
    void origenYDestinoIgualesDanError() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.buscarRuta("BLOQUE_F", "BLOQUE_F", "general"));
    }

    @Test
    void lugarInexistenteDaError() {
        assertThrows(UbicacionNoEncontradaException.class,
                () -> servicio.buscarRuta("NO_EXISTE", "BLOQUE_F", "general"));
    }
}