package org.Juan.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

/**
 * Representa un bloqueo en un tramo del campus (por ejemplo, un ascensor
 * danado u obras en un camino). Se guarda en la base de datos y sirve para
 * saber que tramos no se pueden usar y por que.
 *
 * @version 1.0
 */
@Entity
public class Bloqueo {

    /** Identificador unico del bloqueo. */
    @Id
    private String id;

    /** Id del tramo que esta bloqueado. */
    private String tramoId;

    /** Razon corta del bloqueo (por ejemplo, "obra" o "dano"). */
    private String motivo;

    /** Explicacion mas detallada de lo que pasa. */
    private String descripcion;

    /** Estado actual del bloqueo: ACTIVO o RESUELTO. */
    private String estado; // ACTIVO, RESUELTO

    /** Fecha y hora en que se creo el bloqueo. */
    private LocalDateTime fecha;

    /**
     * Constructor vacio que necesita JPA para poder crear el objeto
     * cuando lo lee de la base de datos. Es protected para que no se
     * use por fuera por accidente.
     */
    protected Bloqueo() {}

    /**
     * Crea un bloqueo nuevo. Siempre empieza como ACTIVO y guarda la
     * fecha y hora del momento en que se crea.
     *
     * @param id          identificador unico del bloqueo
     * @param tramoId     id del tramo que queda bloqueado
     * @param motivo      razon corta del bloqueo
     * @param descripcion explicacion detallada del problema
     */
    public Bloqueo(String id, String tramoId, String motivo, String descripcion) {
        this.id = id;
        this.tramoId = tramoId;
        this.motivo = motivo;
        this.descripcion = descripcion;
        this.estado = "ACTIVO";
        this.fecha = LocalDateTime.now();
    }

    /**
     * @return el identificador unico del bloqueo
     */
    public String getId() { return id; }

    /**
     * @return el id del tramo bloqueado
     */
    public String getTramoId() { return tramoId; }

    /**
     * Hace lo mismo que {@link #getTramoId()}, es otro nombre para
     * lo mismo.
     *
     * @return el id del tramo bloqueado
     */
    public String getCamino() { return tramoId; }

    /**
     * @return la razon corta del bloqueo
     */
    public String getMotivo() { return motivo; }

    /**
     * @return la explicacion detallada del bloqueo
     */
    public String getDescripcion() { return descripcion; }

    /**
     * Hace lo mismo que {@link #getDescripcion()}, es otro nombre para
     * lo mismo.
     *
     * @return la explicacion detallada del bloqueo
     */
    public String getDetalle() { return descripcion; }

    /**
     * @return el estado del bloqueo: "ACTIVO" o "RESUELTO"
     */
    public String getEstado() { return estado; }

    /**
     * @return la fecha y hora en que se creo el bloqueo
     */
    public LocalDateTime getFecha() { return fecha; }

    /**
     * Marca el bloqueo como resuelto, o sea que el tramo ya se puede
     * volver a usar.
     */
    public void resolver() { this.estado = "RESUELTO"; }
}