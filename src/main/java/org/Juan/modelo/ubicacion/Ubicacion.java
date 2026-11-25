package org.Juan.modelo.ubicacion;

/**
 * Cualquier punto del mapa del campus.
 * Es abstracta: no existe una "Ubicacion" a secas, siempre es de un tipo concreto.
 * Guarda lo que todos los lugares tienen en comun, que es el id y el nombre.
 *
 * @version 1.0
 */
public abstract class Ubicacion {

    /** Identificador unico del lugar. Es lo que se usa para comparar ubicaciones. */
    private final String id;

    /** Nombre del lugar, para mostrarlo a la persona. */
    private final String nombre;

    /**
     * Crea una ubicacion con su id y su nombre. Es protected porque la
     * clase es abstracta: solo la usan las clases hijas al llamar a super.
     *
     * @param id     identificador unico del lugar
     * @param nombre nombre del lugar
     */
    protected Ubicacion(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * @return el identificador unico del lugar
     */
    public String getId() {
        return id;
    }

    /**
     * @return el nombre del lugar
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Cada tipo de lugar dice que es (por ejemplo, "Bloque" o "Punto de servicio").
     *
     * @return el tipo del lugar, escrito como texto
     */
    public abstract String getTipo();

    /**
     * Dice si la persona puede elegir este punto como origen o destino.
     * Por defecto es true; las clases hijas lo cambian si no quieren que
     * se pueda escoger (como {@link PuntoPaso}).
     *
     * @return true si se puede elegir, false si no
     */
    public boolean esSeleccionable() {
        return true;
    }

    /**
     * Devuelve el lugar como texto, con su tipo y su nombre.
     * Ejemplo: "Bloque: Bloque F".
     *
     * @return el tipo y el nombre separados por dos puntos
     */
    @Override
    public String toString() {
        return getTipo() + ": " + nombre;
    }
}