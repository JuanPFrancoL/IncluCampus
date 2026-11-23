package org.Juan.repositorio;

import org.Juan.modelo.cuenta.Administrador;
import java.util.Optional;

public interface RepositorioCuentas {
    // Devuelve el administrador con ese usuario o vacio si no existe
    Optional<Administrador> buscarAdministrador(String usuario);
}
