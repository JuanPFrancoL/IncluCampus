package org.Juan.modelo.cuenta;

public class Usuario extends Cuenta {

    private final String perfil;

    public Usuario(String nombre, String perfil) {
        super(nombre);
        this.perfil = perfil;
    }

    public String getPerfil() {
        return perfil;
    }

    @Override
    public Rol getRol() {
        return Rol.USUARIO;
    }

    @Override
    public boolean puedeAdministrar() {
        return false;
    }
}
