package org.Juan.modelo.perfil;

import org.Juan.modelo.tramo.Tramo;

/**
 * Un tipo de usuario según su discapacidad.
 * Es abstracta: cada perfil decide a su manera qué caminos puede usar.
 */
public abstract class PerfilAccesibilidad {

    private final String codigo;
    private final String nombre;
    private final String descripcion;

    protected PerfilAccesibilidad(String codigo, String nombre, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /** Identificador corto que usa la página: "movilidad", "visual" o "general". */
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /** Regla dura: si devuelve false, ese tramo se descarta para este perfil. */
    public abstract boolean puedeUsar(Tramo tramo);

    /**
     * Qué tan difícil le resulta este tramo a este perfil.
     * Por defecto es el costo normal del tramo; un perfil puede cambiarlo.
     */
    public double calcularCosto(Tramo tramo) {
        return tramo.getCosto();
    }
    /** Si es true, este perfil quiere simplemente el camino más corto. */
    public boolean priorizaDistancia() {
        return false;
    }
}