package org.Juan.repositorio;

import org.Juan.modelo.ubicacion.Ubicacion;

import java.util.List;

/** De dónde salen los lugares del campus. */
public interface RepositorioCampus {

    List<Ubicacion> obtenerTodas();

    /** Devuelve el lugar con ese id o lanza UbicacionNoEncontradaException. */
    Ubicacion buscarPorId(String id);
}