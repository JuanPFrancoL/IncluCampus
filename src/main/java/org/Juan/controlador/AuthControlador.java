package org.Juan.controlador;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.Juan.modelo.cuenta.Cuenta;
import org.Juan.servicio.ServicioAutenticacion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * Controlador de la autenticacion (todas las rutas empiezan con /api/auth).
 * Permite iniciar sesion como administrador, cerrar sesion y preguntar si
 * hay alguien conectado. La cuenta se guarda en la sesion del servidor.
 *
 * @version 1.0
 */

@RestController
@RequestMapping("/api/auth")
public class AuthControlador {

    /** Nombre con el que se guarda la cuenta dentro de la sesión del servidor. */
    public static final String ATRIBUTO_CUENTA = "cuenta";

    /** Servicio que revisa el usuario y la clave del administrador. */
    private final ServicioAutenticacion servicioAutenticacion;

    /**
     * Crea el controlador con el servicio que necesita. Spring lo entrega
     * solo al arrancar la aplicacion.
     *
     * @param servicioAutenticacion servicio para verificar las credenciales
     */
    public AuthControlador(ServicioAutenticacion servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;
    }
    /**
     * Inicia sesion como administrador. Si el usuario y la clave son
     * correctos, guarda la cuenta en la sesion. Al entrar se cambia el id
     * de la sesion para evitar que se la roben.
     * Ruta: POST /api/auth/login
     *
     * @param credenciales mapa con los campos "usuario" y "clave"
     * @param peticion     la peticion HTTP, de donde se saca la sesion
     * @return un mapa con "autenticado", "rol" y "nombre" de la cuenta
     */

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> credenciales,
                                     HttpServletRequest peticion) {
        Cuenta cuenta = servicioAutenticacion.autenticarAdmin(
                credenciales.get("usuario"),
                credenciales.get("clave"));

        HttpSession sesion = peticion.getSession(true);
        peticion.changeSessionId(); // sesión nueva al entrar, evita robo de sesión
        sesion.setAttribute(ATRIBUTO_CUENTA, cuenta);
        return datosDe(cuenta);
    }
    /**
     * Cierra la sesion de quien este conectado. Si no habia sesion, no
     * pasa nada.
     * Ruta: POST /api/auth/logout
     *
     * @param peticion la peticion HTTP, de donde se saca la sesion
     * @return un mapa con "autenticado" en false
     */

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest peticion) {
        HttpSession sesion = peticion.getSession(false);
        if (sesion != null) {
            sesion.invalidate();
        }
        return datosDe(null);
    }
    /**
     * La pagina pregunta aqui si hay un administrador conectado.
     * Ruta: GET /api/auth/sesion
     *
     * @param peticion la peticion HTTP, de donde se saca la sesion
     * @return un mapa con "autenticado" y, si hay sesion, "rol" y "nombre"
     */

    /** La página pregunta aquí si hay un administrador conectado. */
    @GetMapping("/sesion")
    public Map<String, Object> sesion(HttpServletRequest peticion) {
        return datosDe(cuentaDe(peticion));
    }


    /**
     * Busca la cuenta que inicio sesion. Es estatica para que otros
     * controladores (como {@link AdminControlador}) la puedan usar.
     *
     * @param peticion la peticion HTTP, de donde se saca la sesion
     * @return la cuenta que inicio sesion, o null si nadie lo hizo
     */
    public static Cuenta cuentaDe(HttpServletRequest peticion) {
        HttpSession sesion = peticion.getSession(false);
        if (sesion == null) {
            return null;
        }
        return (Cuenta) sesion.getAttribute(ATRIBUTO_CUENTA);
    }

    /**
     * Arma los datos que se le envian a la pagina. Solo se envia lo que
     * necesita (nunca la clave).
     *
     * @param cuenta la cuenta conectada, o null si no hay nadie
     * @return un mapa con "autenticado" y, si hay cuenta, "rol" y "nombre"
     */
    private Map<String, Object> datosDe(Cuenta cuenta) {
        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("autenticado", cuenta != null);
        if (cuenta != null) {
            datos.put("rol", cuenta.getRol().name());
            datos.put("nombre", cuenta.getNombre());
        }
        return datos;
    }
}
