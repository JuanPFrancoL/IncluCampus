package org.Juan.modelo.cuenta;

import java.io.Serializable;

/**
 * Clase base de todas las cuentas del sistema (por ejemplo, el administrador).
 * Guarda lo que tienen en comun, que es el nombre, y deja que cada tipo de
 * cuenta decida su rol y si puede administrar. Es Serializable para que se
 * pueda guardar o enviar como objeto.
 *
 * @version 1.0
 */
public abstract class Cuenta implements Serializable {

    /** Nombre de la cuenta, o sea con el que se identifica en el sistema. */
    private final String nombre;

    /**
     * Crea una cuenta con su nombre. Es protected porque la clase es
     * abstracta: solo la usan las clases hijas (como {@link Administrador})
     * al llamar a super.
     *
     * @param nombre nombre de la cuenta
     */
    protected Cuenta(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return el nombre de la cuenta
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Cada tipo de cuenta dice que rol tiene.
     *
     * @return el rol de la cuenta
     */
    public abstract Rol getRol();

    /**
     * Cada tipo de cuenta responde si puede administrar (polimorfismo).
     *
     * @return true si la cuenta puede administrar, false si no
     */
    public abstract boolean puedeAdministrar();
}