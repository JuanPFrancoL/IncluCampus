package org.Juan.modelo.cuenta;

import java.io.Serializable;

public abstract class Cuenta implements Serializable {

    private final String nombre;

    protected Cuenta(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    /** Cada tipo de cuenta dice qué rol tiene. */
    public abstract Rol getRol();

    /** Cada tipo de cuenta responde si puede administrar (polimorfismo). */
    public abstract boolean puedeAdministrar();
}
