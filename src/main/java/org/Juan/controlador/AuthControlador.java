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

@RestController
@RequestMapping("/api/auth")
public class AuthControlador {

    /** Nombre con el que se guarda la cuenta dentro de la sesión del servidor. */
    public static final String ATRIBUTO_CUENTA = "cuenta";

    private final ServicioAutenticacion servicioAutenticacion;

    public AuthControlador(ServicioAutenticacion servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;
    }

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

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest peticion) {
        HttpSession sesion = peticion.getSession(false);
        if (sesion != null) {
            sesion.invalidate();
        }
        return datosDe(null);
    }

    /** La página pregunta aquí si hay un administrador conectado. */
    @GetMapping("/sesion")
    public Map<String, Object> sesion(HttpServletRequest peticion) {
        return datosDe(cuentaDe(peticion));
    }

    /** La cuenta que inició sesión, o null si nadie lo hizo. */
    public static Cuenta cuentaDe(HttpServletRequest peticion) {
        HttpSession sesion = peticion.getSession(false);
        if (sesion == null) {
            return null;
        }
        return (Cuenta) sesion.getAttribute(ATRIBUTO_CUENTA);
    }

    /** Solo se envía lo que la página necesita (nunca la clave). */
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
