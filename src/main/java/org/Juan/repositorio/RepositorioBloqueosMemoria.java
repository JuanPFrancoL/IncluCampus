package org.Juan.repositorio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.Juan.modelo.Bloqueo;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Repositorio que guarda y consulta los bloqueos en la base de datos,
 * usando JPA. Es el unico lugar que habla directamente con la base de
 * datos para los bloqueos. Cada metodo corre dentro de una transaccion.
 *
 * <p>El nombre dice "Memoria", pero en realidad guarda en la base de datos
 * por medio de JPA.</p>
 *
 * @version 1.0
 */
@Repository
@Transactional
public class RepositorioBloqueosMemoria implements RepositorioBloqueos {
    /** Herramienta de JPA para hablar con la base de datos. Spring la entrega sola. */
    @PersistenceContext
    private EntityManager em;
    /**
     * Guarda un bloqueo nuevo en la base de datos.
     *
     * @param bloqueo el bloqueo que se quiere guardar
     */

    @Override
    public void guardar(Bloqueo bloqueo) {
        em.persist(bloqueo);
    }
    /**
     * Devuelve todos los bloqueos, tanto los activos como los resueltos.
     *
     * @return la lista de todos los bloqueos
     */

    @Override
    public List<Bloqueo> obtenerTodos() {
        return em.createQuery("SELECT b FROM Bloqueo b", Bloqueo.class).getResultList();
    }
    /**
     * Devuelve solo los bloqueos que siguen activos.
     *
     * @return la lista de bloqueos con estado ACTIVO
     */

    @Override
    public List<Bloqueo> obtenerActivos() {
        return em.createQuery("SELECT b FROM Bloqueo b WHERE b.estado = 'ACTIVO'", Bloqueo.class)
                .getResultList();
    }
    /**
     * Dice si un tramo tiene al menos un bloqueo activo.
     *
     * @param tramoId id del tramo que se quiere revisar
     * @return true si hay algun bloqueo activo en ese tramo, false si no
     */

    @Override
    public boolean estaTramoBloqueado(String tramoId) {
        Long count = em.createQuery(
                        "SELECT COUNT(b) FROM Bloqueo b WHERE b.estado = 'ACTIVO' AND b.tramoId = :tramoId",
                        Long.class)
                .setParameter("tramoId", tramoId)
                .getSingleResult();
        return count > 0;
    }
    /**
     * Marca un bloqueo como resuelto. Si no existe ninguno con ese id, no
     * hace nada. El cambio se guarda solo al terminar la transaccion.
     *
     * @param id id del bloqueo que se quiere resolver
     */

    @Override
    public void resolverBloqueo(String id) {
        Bloqueo bloqueo = em.find(Bloqueo.class, id);
        if (bloqueo != null) {
            bloqueo.resolver();
        }
    }
}