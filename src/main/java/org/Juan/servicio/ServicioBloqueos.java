package org.Juan.servicio;

import org.Juan.excepcion.BloqueoInvalidoException;
import org.Juan.modelo.Bloqueo;
import org.Juan.repositorio.RepositorioBloqueos;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
/**
 * Servicio que maneja los bloqueos de los caminos: permite reportar uno
 * nuevo (con sus validaciones), ver los bloqueos y levantarlos. Es el que
 * esta en medio entre los controladores y el repositorio.
 *
 * @version 1.0
 */

@Service
public class ServicioBloqueos {
    /** Repositorio donde se guardan y se consultan los bloqueos. */
    private final RepositorioBloqueos repoBloqueos;
    /**
     * Crea el servicio con el repositorio que necesita. Spring lo entrega
     * solo al arrancar la aplicacion.
     *
     * @param repoBloqueos repositorio de bloqueos
     */
    public ServicioBloqueos(RepositorioBloqueos repoBloqueos) {
        this.repoBloqueos = repoBloqueos;
    }

    /**
     * Reporta un bloqueo nuevo en un camino. Primero revisa que el camino
     * y el motivo no vengan vacios, y que el camino no tenga ya otro
     * bloqueo activo. Si todo esta bien, crea el bloqueo con un id al azar
     * de 8 caracteres y lo guarda.
     *
     * @param camino  id del tramo que se quiere bloquear
     * @param motivo  razon corta del bloqueo
     * @param detalle explicacion mas detallada (puede ir vacia)
     * @return el bloqueo que se creo
     * @throws BloqueoInvalidoException si falta el camino o el motivo, o si el camino ya tiene un bloqueo activo
     */
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

    /**
     * Devuelve solo los bloqueos que siguen activos.
     *
     * @return la lista de bloqueos activos
     */
    public List<Bloqueo> listarActivos() {
        return repoBloqueos.obtenerActivos();
    }

    /**
     * Devuelve todos los bloqueos, tanto los activos como los resueltos.
     *
     * @return la lista de todos los bloqueos
     */
    public List<Bloqueo> listarTodos() {
        return repoBloqueos.obtenerTodos();
    }

    public void levantarBloqueo(String id) {
        repoBloqueos.resolverBloqueo(id);
    }
}
