package org.Juan.servicio;

import org.Juan.excepcion.BloqueoInvalidoException;
import org.Juan.modelo.Bloqueo;
import org.Juan.repositorio.RepositorioBloqueos;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServicioBloqueos {

    private final RepositorioBloqueos repoBloqueos;

    public ServicioBloqueos(RepositorioBloqueos repoBloqueos) {
        this.repoBloqueos = repoBloqueos;
    }

    public Bloqueo reportar(String camino, String motivo, String detalle) {
        if (camino == null || camino.isBlank() || motivo == null || motivo.isBlank()) {
            throw new BloqueoInvalidoException("El camino y el motivo son obligatorios.");
        }
        // Un camino solo puede tener un bloqueo activo: así, al levantarlo, queda libre de verdad
        if (repoBloqueos.estaTramoBloqueado(camino)) {
            throw new BloqueoInvalidoException("Ese camino ya tiene un bloqueo activo reportado.");
        }
        Bloqueo nuevo = new Bloqueo(UUID.randomUUID().toString().substring(0, 8), camino, motivo, detalle);
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
