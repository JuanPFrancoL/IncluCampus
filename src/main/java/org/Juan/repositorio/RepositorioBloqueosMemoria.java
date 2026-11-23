// RepositorioBloqueosMemoria.java
package org.Juan.repositorio;

import org.Juan.modelo.Bloqueo;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositorioBloqueosMemoria implements RepositorioBloqueos {

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
        return bloqueos.stream()
                .filter(b -> "ACTIVO".equals(b.getEstado()))
                .toList();
    }

    @Override
    public boolean estaTramoBloqueado(String tramoId) {
        return bloqueos.stream()
                .anyMatch(b -> "ACTIVO".equals(b.getEstado()) && b.getTramoId().equals(tramoId));
    }

    @Override
    public void resolverBloqueo(String id) {
        bloqueos.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .ifPresent(Bloqueo::resolver);
    }
}