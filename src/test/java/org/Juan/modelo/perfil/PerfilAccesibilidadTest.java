package org.Juan.modelo.perfil;

import org.Juan.modelo.tramo.Tramo;
import org.Juan.modelo.tramo.TramoAscensor;
import org.Juan.modelo.tramo.TramoEscalera;
import org.Juan.modelo.tramo.TramoPlano;
import org.Juan.modelo.tramo.TramoRampa;
import org.Juan.modelo.ubicacion.Bloque;
import org.Juan.modelo.ubicacion.Ubicacion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PerfilAccesibilidadTest {

    private final Ubicacion a = new Bloque("A", "Bloque A");
    private final Ubicacion b = new Bloque("B", "Bloque B");

    @Test
    void movilidadReducidaDescartaEscaleras() {
        PerfilAccesibilidad perfil = new PerfilMovilidadReducida();

        assertFalse(perfil.puedeUsar(new TramoEscalera(a, b, 10)));
        assertTrue(perfil.puedeUsar(new TramoRampa(a, b, 10)));
        assertTrue(perfil.puedeUsar(new TramoAscensor(a, b, 10)));
        assertTrue(perfil.puedeUsar(new TramoPlano(a, b, 10)));
    }

    @Test
    void perfilGeneralAceptaTodosLosTramos() {
        PerfilAccesibilidad perfil = new PerfilGeneral();

        assertTrue(perfil.puedeUsar(new TramoEscalera(a, b, 10)));
        assertTrue(perfil.puedeUsar(new TramoRampa(a, b, 10)));
    }

    @Test
    void perfilVisualHaceMasCostosasLasEscaleras() {
        Tramo escalera = new TramoEscalera(a, b, 10);

        double costoVisual = new PerfilVisual().calcularCosto(escalera);
        double costoGeneral = new PerfilGeneral().calcularCosto(escalera);

        assertTrue(costoVisual > costoGeneral);
    }
}