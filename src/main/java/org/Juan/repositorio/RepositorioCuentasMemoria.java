package org.Juan.repositorio;

import org.Juan.modelo.cuenta.Administrador;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Guarda los administradores en memoria.
 * La contrasena NO esta en el codigo: viene de la variable de entorno
 * INCLUCAMPUS_ADMIN_CLAVE. Si no existe, se inventa una al azar y se
 * muestra una sola vez en la consola. Por ahora solo hay un administrador,
 * con el usuario "admin".
 *
 * @version 1.0
 */
@Repository
public class RepositorioCuentasMemoria implements RepositorioCuentas {

    /** Lista de los administradores que existen. Por ahora solo tiene a "admin". */
    private final List<Administrador> administradores = new ArrayList<>();

    /**
     * Crea el repositorio y deja listo al administrador "admin". Si la
     * clave configurada viene vacia, genera una al azar y la muestra en la
     * consola para que se pueda usar. Despues crea el administrador con esa
     * clave (de ella solo se guarda el hash, nunca la clave en texto).
     *
     * @param claveConfigurada clave tomada de la configuracion
     *                         (INCLUCAMPUS_ADMIN_CLAVE); puede venir vacia
     */
    public RepositorioCuentasMemoria(@Value("${inclucampus.admin.clave:}") String claveConfigurada) {
        String clave = claveConfigurada;
        if (clave == null || clave.isBlank()) {
            clave = new BigInteger(60, new SecureRandom()).toString(36);
            System.out.println("=====================================================");
            System.out.println(" Administrador: usuario 'admin'  clave '" + clave + "'");
            System.out.println(" (cambia por INCLUCAMPUS_ADMIN_CLAVE para fijarla)");
            System.out.println("=====================================================");
        }
        administradores.add(Administrador.crear("admin", clave));
    }

    /**
     * Busca un administrador por su nombre de usuario. La comparacion
     * distingue mayusculas de minusculas.
     *
     * @param usuario nombre de usuario que se busca
     * @return el administrador, o un Optional vacio si no existe ninguno con ese nombre
     */
    @Override
    public Optional<Administrador> buscarAdministrador(String usuario) {
        for (Administrador administrador : administradores) {
            if (administrador.getNombre().equals(usuario)) {
                return Optional.of(administrador);
            }
        }
        return Optional.empty();
    }
}
