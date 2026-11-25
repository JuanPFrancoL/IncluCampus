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
 * La contraseña NO está en el código: viene de la variable de entorno INCLUCAMPUS_ADMIN_CLAVE.
 * Si no existe, se inventa una al azar y se muestra una sola vez en la consola.
 */
@Repository
public class RepositorioCuentasMemoria implements RepositorioCuentas {

    private final List<Administrador> administradores = new ArrayList<>();

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
