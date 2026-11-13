package org.Juan.repositorio;

import org.Juan.excepcion.UbicacionNoEncontradaException;
import org.Juan.modelo.tramo.Tramo;
import org.Juan.modelo.tramo.TramoAscensor;
import org.Juan.modelo.tramo.TramoEscalera;
import org.Juan.modelo.tramo.TramoPlano;
import org.Juan.modelo.tramo.TramoRampa;
import org.Juan.modelo.ubicacion.Bloque;
import org.Juan.modelo.ubicacion.PuntoPaso;
import org.Juan.modelo.ubicacion.PuntoServicio;
import org.Juan.modelo.ubicacion.Ubicacion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Guarda el campus en listas en memoria (sin base de datos).
 * Las distancias y conexiones son de EJEMPLO: se corrigen con las medidas reales.
 */
@Repository
public class RepositorioCampusMemoria implements RepositorioCampus {

    private final List<Ubicacion> ubicaciones = new ArrayList<>();
    private final List<Tramo> tramos = new ArrayList<>();

    public RepositorioCampusMemoria() {
        // Lugares a los que se puede ir
        Ubicacion biblioteca = new PuntoServicio("BIBLIOTECA", "Biblioteca");
        Ubicacion bloqueF = new Bloque("BLOQUE_F", "Bloque F");
        Ubicacion bloque16 = new Bloque("BLOQUE_16", "Bloque 16");
        Ubicacion cafeteria = new PuntoServicio("CAFETERIA", "Cafetería");
        Ubicacion tienda = new PuntoServicio("TIENDA_UAM", "Tienda UAM");

        // Puntos intermedios (nadie va "hacia" ellos)
        Ubicacion entrada = new PuntoPaso("ENTRADA_PRINCIPAL", "Entrada principal");
        Ubicacion pieRampa = new PuntoPaso("PIE_RAMPA", "Pie de rampa");

        ubicaciones.add(biblioteca);
        ubicaciones.add(bloqueF);
        ubicaciones.add(bloque16);
        ubicaciones.add(cafeteria);
        ubicaciones.add(tienda);
        ubicaciones.add(entrada);
        ubicaciones.add(pieRampa);

        // Caminos entre los puntos (distancia en metros)
        tramos.add(new TramoPlano(biblioteca, entrada, 40));
        tramos.add(new TramoPlano(entrada, pieRampa, 90));
        tramos.add(new TramoRampa(pieRampa, bloqueF, 35));
        tramos.add(new TramoPlano(entrada, cafeteria, 70));
        tramos.add(new TramoPlano(cafeteria, tienda, 30));
        tramos.add(new TramoPlano(cafeteria, bloque16, 60));
        tramos.add(new TramoAscensor(bloque16, bloqueF, 25));
        tramos.add(new TramoEscalera(entrada, bloque16, 30));
    }

    @Override
    public List<Ubicacion> obtenerTodas() {
        return new ArrayList<>(ubicaciones);
    }

    @Override
    public Ubicacion buscarPorId(String id) {
        for (Ubicacion ubicacion : ubicaciones) {
            if (ubicacion.getId().equals(id)) {
                return ubicacion;
            }
        }
        throw new UbicacionNoEncontradaException(id);
    }

    @Override
    public List<Tramo> obtenerTramos() {
        return new ArrayList<>(tramos);
    }
}