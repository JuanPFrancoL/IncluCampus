package org.Juan.servicio;

import org.Juan.excepcion.RutaNoDisponibleException;
import org.Juan.excepcion.UbicacionNoEncontradaException;
import org.Juan.modelo.Bloqueo;
import org.Juan.modelo.Ruta;
import org.Juan.modelo.tramo.TipoTramo;
import org.Juan.modelo.tramo.Tramo;
import org.Juan.repositorio.RepositorioCampusMemoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServicioRutasTest {

    // Ids de los tramos del campus (origen-destino) que usan los bloqueos
    private static final String ASCENSOR_BLOQUE16_BLOQUEF = "bloque16-bloquef";
    private static final String BIBLIOTECA_ENTRADA = "biblioteca-entradaprincipal";

    private final RepositorioBloqueosFalso bloqueos = new RepositorioBloqueosFalso();
    private final ServicioRutas servicio = new ServicioRutas(new RepositorioCampusMemoria(), bloqueos);

    @Test
    void movilidadReducidaEvitaEscalerasYPrefiereElAscensor() {
        Ruta ruta = servicio.buscarRuta("biblioteca", "bloquef", "movilidad");

        // Por Bloque 16 + ascensor: 40 + 50 + 45 + 25 = 160 m (costo 180, menor que el de la rampa: 182.5)
        assertEquals(160.0, ruta.getDistanciaTotal(), 0.001);
        assertSinEscaleras(ruta);
        assertTrue(contieneTipo(ruta, TipoTramo.ASCENSOR));
    }

    @Test
    void movilidadReducidaVaPorLaRampaSiElAscensorEstaBloqueado() {
        bloqueos.guardar(new Bloqueo("b1", ASCENSOR_BLOQUE16_BLOQUEF, "Mantenimiento", "Ascensor fuera de servicio"));

        Ruta ruta = servicio.buscarRuta("biblioteca", "bloquef", "movilidad");

        // Por la rampa: 40 + 50 + 40 + 35 = 165 m
        assertEquals(165.0, ruta.getDistanciaTotal(), 0.001);
        assertSinEscaleras(ruta);
        assertTrue(contieneTipo(ruta, TipoTramo.RAMPA));
    }

    @Test
    void alLevantarElBloqueoVuelveLaRutaConAscensor() {
        bloqueos.guardar(new Bloqueo("b1", ASCENSOR_BLOQUE16_BLOQUEF, "Mantenimiento", "Ascensor fuera de servicio"));
        servicio.buscarRuta("biblioteca", "bloquef", "movilidad");

        bloqueos.resolverBloqueo("b1");
        Ruta ruta = servicio.buscarRuta("biblioteca", "bloquef", "movilidad");

        assertEquals(160.0, ruta.getDistanciaTotal(), 0.001);
    }

    @Test
    void perfilGeneralVaPorElCaminoMasCorto() {
        Ruta ruta = servicio.buscarRuta("biblioteca", "bloquef", "general");

        // Usa la escalera de Entrada a Bloque 16: 40 + 30 + 25 = 95 m
        assertEquals(95.0, ruta.getDistanciaTotal(), 0.001);
        assertTrue(contieneTipo(ruta, TipoTramo.ESCALERA));
    }

    @Test
    void perfilVisualRodeaLaEscalera() {
        Ruta ruta = servicio.buscarRuta("biblioteca", "bloquef", "visual");

        assertSinEscaleras(ruta);
    }

    @Test
    void lugaresSeBuscanSinImportarMayusculas() {
        Ruta ruta = servicio.buscarRuta("BIBLIOTECA", "BLOQUEF", "general");

        assertEquals(95.0, ruta.getDistanciaTotal(), 0.001);
    }

    @Test
    void origenYDestinoIgualesDanError() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.buscarRuta("bloquef", "bloquef", "general"));
    }

    @Test
    void lugarInexistenteDaError() {
        assertThrows(UbicacionNoEncontradaException.class,
                () -> servicio.buscarRuta("no_existe", "bloquef", "general"));
    }

    @Test
    void perfilDesconocidoDaError() {
        assertThrows(IllegalArgumentException.class,
                () -> servicio.buscarRuta("biblioteca", "bloquef", "inventado"));
    }

    @Test
    void sinCaminoDisponibleDaError() {
        // La Biblioteca solo sale hacia la Entrada y hacia su baño; si se bloquea, queda aislada
        bloqueos.guardar(new Bloqueo("b1", BIBLIOTECA_ENTRADA, "Obra", "Camino cerrado"));

        assertThrows(RutaNoDisponibleException.class,
                () -> servicio.buscarRuta("biblioteca", "bloquef", "general"));
    }

    private static void assertSinEscaleras(Ruta ruta) {
        for (Tramo tramo : ruta.getTramos()) {
            assertNotEquals(TipoTramo.ESCALERA, tramo.getTipo());
        }
    }

    private static boolean contieneTipo(Ruta ruta, TipoTramo tipo) {
        for (Tramo tramo : ruta.getTramos()) {
            if (tramo.getTipo() == tipo) {
                return true;
            }
        }
        return false;
    }
}