package org.Juan.repositorio;

import org.Juan.excepcion.UbicacionNoEncontradaException;
import org.Juan.modelo.ubicacion.Bloque;
import org.Juan.modelo.ubicacion.PuntoPaso;
import org.Juan.modelo.ubicacion.PuntoServicio;
import org.Juan.modelo.ubicacion.Ubicacion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/** Guarda los lugares en una lista en memoria (sin base de datos). */
@Repository
public class RepositorioCampusMemoria implements RepositorioCampus {

    private final List<Ubicacion> ubicaciones = new ArrayList<>();

    public RepositorioCampusMemoria() {
        // Lugares a los que se puede ir
        ubicaciones.add(new PuntoServicio("BIBLIOTECA", "Biblioteca"));
        ubicaciones.add(new Bloque("BLOQUE_F", "Bloque F"));
        ubicaciones.add(new Bloque("BLOQUE_16", "Bloque 16"));
        ubicaciones.add(new PuntoServicio("CAFETERIA", "Cafetería"));
        ubicaciones.add(new PuntoServicio("TIENDA_UAM", "Tienda UAM"));

        // Puntos intermedios (nadie va "hacia" ellos)
        ubicaciones.add(new PuntoPaso("ENTRADA_PRINCIPAL", "Entrada principal"));
        ubicaciones.add(new PuntoPaso("PIE_RAMPA", "Pie de rampa"));
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
}