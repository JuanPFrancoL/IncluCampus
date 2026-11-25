package org.Juan.modelo.cuenta;

/**
 * Cuenta de una persona normal que usa el sistema, sin permisos de
 * administracion. Ademas del nombre, guarda el perfil de accesibilidad
 * con el que se le calculan las rutas.
 *
 * @version 1.0
 */
public class Usuario extends Cuenta {

    /** Perfil de la persona (por ejemplo, silla de ruedas o baja vision). */
    private final String perfil;

    /**
     * Crea un usuario con su nombre y su perfil.
     *
     * @param nombre nombre del usuario
     * @param perfil perfil de la persona, que se usa para adaptar las rutas
     */
    public Usuario(String nombre, String perfil) {
        super(nombre);
        this.perfil = perfil;
    }

    /**
     * @return el perfil del usuario
     */
    public String getPerfil() {
        return perfil;
    }

    /**
     * @return el rol de esta cuenta, que siempre es USUARIO
     */
    @Override
    public Rol getRol() {
        return Rol.USUARIO;
    }

    /**
     * @return false siempre, porque un usuario normal no puede administrar
     */
    @Override
    public boolean puedeAdministrar() {
        return false;
    }
}