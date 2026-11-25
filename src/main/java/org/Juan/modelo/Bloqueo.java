package org.Juan.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Bloqueo {
    @Id
    private String id;
    private String tramoId;
    private String motivo;
    private String descripcion;
    private String estado; // ACTIVO, RESUELTO
    private LocalDateTime fecha;

    protected Bloqueo() {}

    public Bloqueo(String id, String tramoId, String motivo, String descripcion) {
        this.id = id;
        this.tramoId = tramoId;
        this.motivo = motivo;
        this.descripcion = descripcion;
        this.estado = "ACTIVO";
        this.fecha = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getTramoId() { return tramoId; }
    public String getCamino() { return tramoId; }
    public String getMotivo() { return motivo; }
    public String getDescripcion() { return descripcion; }
    public String getDetalle() { return descripcion; }
    public String getEstado() { return estado; }
    public LocalDateTime getFecha() { return fecha; }
    public void resolver() { this.estado = "RESUELTO"; }
}