package org.Juan.modelo.cuenta;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * Administrador del sistema.
 * La contraseña nunca se guarda: solo se guarda su "huella" (hash) mezclada con una sal.
 */
public class Administrador extends Cuenta {

    private static final int ITERACIONES = 65_000;
    private static final int BITS_HASH = 256;

    private final byte[] sal;
    private final byte[] hashClave;

    public Administrador(String usuario, byte[] sal, byte[] hashClave) {
        super(usuario);
        this.sal = sal;
        this.hashClave = hashClave;
    }

    /** Crea un administrador a partir de una contraseña en texto: genera su sal y guarda solo el hash. */
    public static Administrador crear(String usuario, String clave) {
        byte[] sal = new byte[16];
        new SecureRandom().nextBytes(sal);
        return new Administrador(usuario, sal, calcularHash(clave, sal));
    }

    /** Dice si la contraseña escrita coincide, sin revelar la guardada. */
    public boolean claveCoincide(String intento) {
        if (intento == null) {
            return false;
        }
        // isEqual compara siempre todos los bytes, así no se filtra información por el tiempo
        return MessageDigest.isEqual(hashClave, calcularHash(intento, sal));
    }

    private static byte[] calcularHash(String clave, byte[] sal) {
        try {
            PBEKeySpec especificacion = new PBEKeySpec(clave.toCharArray(), sal, ITERACIONES, BITS_HASH);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(especificacion)
                    .getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo calcular el hash de la contraseña.", e);
        }
    }

    @Override
    public Rol getRol() {
        return Rol.ADMIN;
    }

    @Override
    public boolean puedeAdministrar() {
        return true;
    }
}
