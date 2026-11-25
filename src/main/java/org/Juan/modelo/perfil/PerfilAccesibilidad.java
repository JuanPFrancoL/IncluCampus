package org.Juan.modelo.perfil;

import org.Juan.modelo.tramo.Tramo;

/**
 * Un tipo de usuario segun su discapacidad.
 * Es abstracta: cada perfil decide a su manera que caminos puede usar.
 * Las clases hijas definen las reglas, y esta clase guarda lo que todos
 * los perfiles tienen en comun.
 *
 * @version 1.0
 */
public abstract class PerfilAccesibilidad {

    /** Identificador corto que usa la pagina: "movilidad", "visual" o "general". */
    private final String codigo;

    /** Nombre del perfil, para mostrarlo a la persona. */
    private final String nombre;

    /** Explicacion corta de a quien va dirigido el perfil y que tiene en cuenta. */
    private final String descripcion;

    /**
     * Crea un perfil con su codigo, nombre y descripcion. Es protected
     * porque la clase es abstracta: solo la usan las clases hijas al
     * llamar a super.
     *
     * @param codigo      identificador corto del perfil
     * @param nombre      nombre del perfil
     * @param descripcion explicacion del perfil
     */
    protected PerfilAccesibilidad(String codigo, String nombre, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /**
     * @return el identificador corto del perfil, que usa la pagina
     *         ("movilidad", "visual" o "general")
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @return el nombre del perfil
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return la descripcion del perfil
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Regla dura: dice si este perfil puede pasar por un tramo.
     * Si devuelve false, el tramo se descarta por completo para este perfil.
     *
     * @param tramo el tramo que se quiere revisar
     * @return true si el perfil puede usar el tramo, false si no
     */
    public abstract boolean puedeUsar(Tramo tramo);

    /**
     * Dice que tan dificil le resulta un tramo a este perfil.
     * Por defecto es el costo normal del tramo, pero un perfil puede
     * cambiarlo si necesita otra cuenta.
     *
     * @param tramo el tramo que se quiere evaluar
     * @return el costo del tramo para este perfil
     */
    public double calcularCosto(Tramo tramo) {
        return tramo.getCosto();
    }

    /**
     * Dice si este perfil quiere simplemente el camino mas corto.
     * Por defecto es false.
     *
     * @return true si el perfil prioriza la distancia, false si no
     */
    public boolean priorizaDistancia() {
        return false;
    }
}