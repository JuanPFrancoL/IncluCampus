package org.Juan.modelo.cuenta;

public class Administrador extends Cuenta {

    private final byte[] sal;
    private final byte[] hashClave;

    public Administrador(String usuario, byte[] sal, byte[] hashClave) {
        super(usuario);
        this.sal = sal;
        this.hashClave = hashClave;
    }

    public byte[] getSal() {
        return sal;
    }

    public byte[] getHashClave() {
        return hashClave;
    }

    @Override
    public Rol getRol() {
        return Rol.ADMIN;
    }
}
