package org.Juan.repositorio;

import org.Juan.modelo.tramo.Tramo;
import org.Juan.modelo.ubicacion.Ubicacion;

import java.util.List;

/** De dónde salen los lugares y los caminos del campus. */
public interface RepositorioCampus {

    List<Ubicacion> obtenerTodas();

    /** Devuelve el lugar con ese id o lanza UbicacionNoEncontradaException. */
    Ubicacion buscarPorId(String id);

    List<Tramo> obtenerTramos();
}