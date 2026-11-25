package org.Juan.modelo.cuenta;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * Administrador del sistema.
 * La contrasena nunca se guarda tal cual: solo se guarda su "huella" (hash)
 * mezclada con una sal, asi nadie puede ver la contrasena real ni siquiera
 * mirando la base de datos.
 *
 * @version 1.0
 */
public class Administrador extends Cuenta {

    /** Cuantas veces se repite el calculo del hash. Mas vueltas hacen mas dificil adivinar la clave. */
    private static final int ITERACIONES = 65_000;

    /** Tamano del hash resultante, en bits. */
    private static final int BITS_HASH = 256;

    /** Datos aleatorios que se mezclan con la clave para que dos claves iguales no den el mismo hash. */
    private final byte[] sal;

    /** La huella de la contrasena. Es lo unico que se guarda de ella. */
    private final byte[] hashClave;

    /**
     * Crea un administrador con su sal y su hash ya calculados.
     * Se usa, por ejemplo, para armar el objeto con los datos guardados.
     * Para crear uno nuevo desde una contrasena, usa {@link #crear(String, String)}.
     *
     * @param usuario   nombre de usuario del administrador
     * @param sal       sal que se uso para calcular el hash
     * @param hashClave huella de la contrasena
     */
    public Administrador(String usuario, byte[] sal, byte[] hashClave) {
        super(usuario);
        this.sal = sal;
        this.hashClave = hashClave;
    }

    /**
     * Crea un administrador nuevo a partir de una contrasena en texto.
     * Genera una sal aleatoria y guarda solo el hash, nunca la contrasena.
     *
     * @param usuario nombre de usuario del administrador
     * @param clave   contrasena en texto normal (no se guarda)
     * @return el administrador listo, con su sal y su hash
     */
    public static Administrador crear(String usuario, String clave) {
        byte[] sal = new byte[16];
        new SecureRandom().nextBytes(sal);
        return new Administrador(usuario, sal, calcularHash(clave, sal));
    }

    /**
     * Dice si la contrasena escrita coincide con la guardada, sin
     * revelar la guardada.
     *
     * @param intento la contrasena que escribio la persona
     * @return true si coincide, false si no coincide o si es null
     */
    public boolean claveCoincide(String intento) {
        if (intento == null) {
            return false;
        }
        // isEqual compara siempre todos los bytes, asi no se filtra informacion por el tiempo
        return MessageDigest.isEqual(hashClave, calcularHash(intento, sal));
    }

    /**
     * Calcula el hash de una contrasena mezclada con una sal, usando
     * PBKDF2 con HmacSHA256.
     *
     * @param clave contrasena en texto normal
     * @param sal   sal que se mezcla con la contrasena
     * @return el hash resultante
     * @throws IllegalStateException si el algoritmo no esta disponible o falla el calculo
     */
    private static byte[] calcularHash(String clave, byte[] sal) {
        try {
            PBEKeySpec especificacion = new PBEKeySpec(clave.toCharArray(), sal, ITERACIONES, BITS_HASH);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(especificacion)
                    .getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo calcular el hash de la contrasena.", e);
        }
    }

    /**
     * @return el rol de esta cuenta, que siempre es ADMIN
     */
    @Override
    public Rol getRol() {
        return Rol.ADMIN;
    }

    /**
     * @return true siempre, porque un administrador si puede administrar
     */
    @Override
    public boolean puedeAdministrar() {
        return true;
    }
}