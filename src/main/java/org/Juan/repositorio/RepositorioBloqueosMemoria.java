package org.Juan.repositorio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.Juan.modelo.Bloqueo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class RepositorioBloqueosMemoria implements RepositorioBloqueos {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void guardar(Bloqueo bloqueo) {
        em.persist(bloqueo);
    }

    @Override
    public List<Bloqueo> obtenerTodos() {
        return em.createQuery("SELECT b FROM Bloqueo b", Bloqueo.class).getResultList();
    }

    @Override
    public List<Bloqueo> obtenerActivos() {
        return em.createQuery("SELECT b FROM Bloqueo b WHERE b.estado = 'ACTIVO'", Bloqueo.class)
                .getResultList();
    }

    @Override
    public boolean estaTramoBloqueado(String tramoId) {
        Long count = em.createQuery(
                        "SELECT COUNT(b) FROM Bloqueo b WHERE b.estado = 'ACTIVO' AND b.tramoId = :tramoId",
                        Long.class)
                .setParameter("tramoId", tramoId)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public void resolverBloqueo(String id) {
        Bloqueo bloqueo = em.find(Bloqueo.class, id);
        if (bloqueo != null) {
            bloqueo.resolver();
        }
    }
}