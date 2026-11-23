// RepositorioBloqueos.java
package org.Juan.repositorio;

import org.Juan.modelo.Bloqueo;
import java.util.List;

public interface RepositorioBloqueos {
    void guardar(Bloqueo bloqueo);
    List<Bloqueo> obtenerTodos();
    List<Bloqueo> obtenerActivos();
    boolean estaTramoBloqueado(String tramoId);
    void resolverBloqueo(String id);
}