// ServicioBloqueos.java
package org.Juan.servicio;

import org.Juan.excepcion.BloqueoInvalidoException;
import org.Juan.modelo.Bloqueo;
import org.Juan.repositorio.RepositorioBloqueos;
import org.Juan.repositorio.RepositorioCampus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServicioBloqueos {

    private final RepositorioBloqueos repoBloqueos;
    private final RepositorioCampus repoCampus;

    public ServicioBloqueos(RepositorioBloqueos repoBloqueos, RepositorioCampus repoCampus) {
        this.repoBloqueos = repoBloqueos;
        this.repoCampus = repoCampus;
    }

    public Bloqueo reportar(String tramoId, String motivo, String descripcion) {
        if (tramoId == null || tramoId.isBlank() || motivo == null || motivo.isBlank()) {
            throw new BloqueoInvalidoException("El camino y el motivo son obligatorios.");
        }
        boolean existeTramo = repoCampus.obtenerTramos().stream().anyMatch(t -> t.getId().equals(tramoId));
        if (!existeTramo) {
            throw new BloqueoInvalidoException("El camino seleccionado no existe.");
        }
        Bloqueo nuevo = new Bloqueo(UUID.randomUUID().toString().substring(0, 8), tramoId, motivo, descripcion);
        repoBloqueos.guardar(nuevo);
        return nuevo;
    }

    public List<Bloqueo> listarActivos() {
        return repoBloqueos.obtenerActivos();
    }

    public List<Bloqueo> listarTodos() {
        return repoBloqueos.obtenerTodos();
    }

    public void levantarBloqueo(String id) {
        repoBloqueos.resolverBloqueo(id);
    }
}