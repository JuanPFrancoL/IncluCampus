package org.Juan.modelo;

import java.time.LocalDateTime;

public class Bloqueo {
    private final String id;
    private final String tramoId;
    private final String motivo;
    private final String descripcion;
    private String estado; // ACTIVO, RESUELTO
    private final LocalDateTime fecha;

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
    public String getMotivo() { return motivo; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }
    public LocalDateTime getFecha() { return fecha; }
    public void resolver() { this.estado = "RESUELTO"; }
}