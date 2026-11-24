package org.Juan.servicio;

import org.Juan.excepcion.AccesoDenegadoException;
import org.Juan.excepcion.CredencialesInvalidasException;
import org.Juan.modelo.cuenta.Administrador;
import org.Juan.modelo.cuenta.Rol;
import org.Juan.modelo.cuenta.Usuario;
import org.Juan.repositorio.RepositorioCuentasMemoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServicioAutenticacionTest {

    private static final String CLAVE = "clave-de-prueba";

    private final ServicioAutenticacion servicio =
            new ServicioAutenticacion(new RepositorioCuentasMemoria(CLAVE));

    @Test
    void administradorConClaveCorrectaEntra() {
        Administrador admin = servicio.autenticarAdmin("admin", CLAVE);

        assertEquals(Rol.ADMIN, admin.getRol());
        assertTrue(admin.puedeAdministrar());
    }

    @Test
    void claveIncorrectaDaError() {
        assertThrows(CredencialesInvalidasException.class,
                () -> servicio.autenticarAdmin("admin", "otra"));
    }

    @Test
    void usuarioQueNoExisteDaError() {
        assertThrows(CredencialesInvalidasException.class,
                () -> servicio.autenticarAdmin("nadie", CLAVE));
    }

    @Test
    void usuarioOClaveNulosDanError() {
        assertThrows(CredencialesInvalidasException.class,
                () -> servicio.autenticarAdmin(null, CLAVE));
        assertThrows(CredencialesInvalidasException.class,
                () -> servicio.autenticarAdmin("admin", null));
    }

    @Test
    void cadaAdministradorTieneSuPropiaSal() {
        Administrador uno = Administrador.crear("a", CLAVE);
        Administrador otro = Administrador.crear("b", CLAVE);

        // Misma clave, pero ambos validan bien y ninguno acepta una clave distinta
        assertTrue(uno.claveCoincide(CLAVE));
        assertTrue(otro.claveCoincide(CLAVE));
        assertFalse(uno.claveCoincide("x"));
    }

    @Test
    void soloElAdministradorPuedeAdministrar() {
        Administrador admin = servicio.autenticarAdmin("admin", CLAVE);
        Usuario usuario = new Usuario("Ana", "movilidad");

        servicio.exigirAdmin(admin); // no lanza error
        assertThrows(AccesoDenegadoException.class, () -> servicio.exigirAdmin(usuario));
        assertThrows(AccesoDenegadoException.class, () -> servicio.exigirAdmin(null));
    }
}
