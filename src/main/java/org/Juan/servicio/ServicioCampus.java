package org.Juan.servicio;

import org.Juan.modelo.ubicacion.Ubicacion;
import org.Juan.repositorio.RepositorioCampus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioCampus {

    private final RepositorioCampus repositorio;

    public ServicioCampus(RepositorioCampus repositorio) {
        this.repositorio = repositorio;
    }

    /** Los lugares que la persona puede elegir como origen o destino. */
    public List<Ubicacion> listarLugaresSeleccionables() {
        List<Ubicacion> resultado = new ArrayList<>();
        for (Ubicacion ubicacion : repositorio.obtenerTodas()) {
            if (ubicacion.esSeleccionable()) {
                resultado.add(ubicacion);
            }
        }
        return resultado;
    }
}