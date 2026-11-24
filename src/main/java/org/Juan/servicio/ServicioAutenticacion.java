package org.Juan.servicio;

import org.Juan.excepcion.AccesoDenegadoException;
import org.Juan.excepcion.CredencialesInvalidasException;
import org.Juan.modelo.cuenta.Administrador;
import org.Juan.modelo.cuenta.Cuenta;
import org.Juan.repositorio.RepositorioCuentas;
import org.springframework.stereotype.Service;

@Service
public class ServicioAutenticacion {

    private final RepositorioCuentas repositorio;

    public ServicioAutenticacion(RepositorioCuentas repositorio) {
        this.repositorio = repositorio;
    }

    /** Devuelve el administrador si usuario y clave son correctos. */
    public Administrador autenticarAdmin(String usuario, String clave) {
        // Mismo mensaje si falla el usuario o la clave, para no dar pistas
        return repositorio.buscarAdministrador(usuario == null ? "" : usuario)
                .filter(administrador -> administrador.claveCoincide(clave))
                .orElseThrow(() -> new CredencialesInvalidasException("Usuario o contraseña incorrectos."));
    }

    /** Lanza AccesoDenegadoException si no hay sesión o la cuenta no puede administrar. */
    public void exigirAdmin(Cuenta cuenta) {
        if (cuenta == null) {
            throw new AccesoDenegadoException("Inicia sesión como administrador para continuar.");
        }
        if (!cuenta.puedeAdministrar()) {
            throw new AccesoDenegadoException("Acceso denegado: se requiere rol de administrador.");
        }
    }
}
