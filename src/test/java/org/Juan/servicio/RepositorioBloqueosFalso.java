package org.Juan.servicio;

import org.Juan.modelo.Bloqueo;
import org.Juan.repositorio.RepositorioBloqueos;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de bloqueos en memoria solo para los tests.
 * Así las pruebas no necesitan base de datos ni Spring.
 */
class RepositorioBloqueosFalso implements RepositorioBloqueos {

    private final List<Bloqueo> bloqueos = new ArrayList<>();

    @Override
    public void guardar(Bloqueo bloqueo) {
        bloqueos.add(bloqueo);
    }

    @Override
    public List<Bloqueo> obtenerTodos() {
        return new ArrayList<>(bloqueos);
    }

    @Override
    public List<Bloqueo> obtenerActivos() {
        List<Bloqueo> activos = new ArrayList<>();
        for (Bloqueo bloqueo : bloqueos) {
            if ("ACTIVO".equals(bloqueo.getEstado())) {
                activos.add(bloqueo);
            }
        }
        return activos;
    }

    @Override
    public boolean estaTramoBloqueado(String tramoId) {
        for (Bloqueo bloqueo : obtenerActivos()) {
            if (bloqueo.getTramoId().equals(tramoId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void resolverBloqueo(String id) {
        for (Bloqueo bloqueo : bloqueos) {
            if (bloqueo.getId().equals(id)) {
                bloqueo.resolver();
            }
        }
    }
}