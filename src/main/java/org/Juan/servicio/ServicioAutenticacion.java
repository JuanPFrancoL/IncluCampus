package org.Juan.servicio;

import org.Juan.excepcion.AccesoDenegadoException;
import org.Juan.excepcion.CredencialesInvalidasException;
import org.Juan.modelo.cuenta.Administrador;
import org.Juan.modelo.cuenta.Cuenta;
import org.Juan.repositorio.RepositorioCuentas;
import org.springframework.stereotype.Service;
/**
 * Servicio que se encarga de la autenticacion: revisa que el usuario y la
 * clave de un administrador sean correctos, y comprueba que quien llama
 * tenga permiso para usar las rutas de administracion.
 *
 * @version 1.0
 */

@Service
public class ServicioAutenticacion {
    /** Repositorio donde se buscan las cuentas de los administradores. */
    private final RepositorioCuentas repositorio;
    /**
     * Crea el servicio con el repositorio que necesita. Spring lo entrega
     * solo al arrancar la aplicacion.
     *
     * @param repositorio repositorio para buscar administradores
     */
    public ServicioAutenticacion(RepositorioCuentas repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Devuelve el administrador si usuario y clave son correctos.
     * Si falla cualquiera de los dos, el error es el mismo, para no dar
     * pistas de cual fue el que estaba mal.
     *
     * @param usuario nombre de usuario escrito por la persona (puede ser null)
     * @param clave   contrasena escrita por la persona
     * @return el administrador que coincide con ese usuario y esa clave
     * @throws CredencialesInvalidasException si el usuario no existe o la clave no coincide
     */
    public Administrador autenticarAdmin(String usuario, String clave) {
        // Mismo mensaje si falla el usuario o la clave, para no dar pistas
        return repositorio.buscarAdministrador(usuario == null ? "" : usuario)
                .filter(administrador -> administrador.claveCoincide(clave))
                .orElseThrow(() -> new CredencialesInvalidasException("Usuario o contraseña incorrectos."));
    }

    /**
     * Comprueba que la cuenta pueda administrar. No devuelve nada: si todo
     * esta bien, simplemente sigue, y si no, lanza una excepcion.
     *
     * @param cuenta la cuenta que inicio sesion, o null si nadie lo hizo
     * @throws AccesoDenegadoException si no hay sesion o la cuenta no puede administrar
     */
    public void exigirAdmin(Cuenta cuenta) {
        if (cuenta == null) {
            throw new AccesoDenegadoException("Inicia sesión como administrador para continuar.");
        }
        if (!cuenta.puedeAdministrar()) {
            throw new AccesoDenegadoException("Acceso denegado: se requiere rol de administrador.");
        }
    }
}
