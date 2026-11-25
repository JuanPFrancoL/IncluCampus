package org.Juan.controlador;

import jakarta.servlet.http.HttpServletRequest;
import org.Juan.modelo.Bloqueo;
import org.Juan.servicio.ServicioAutenticacion;
import org.Juan.servicio.ServicioBloqueos;
import org.Juan.servicio.ServicioCampus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * Controlador con las rutas que solo puede usar un administrador
 * (todas empiezan con /api/admin). Sirve para ver el historial de
 * bloqueos, resolverlos y ver un resumen del campus. Cada ruta revisa
 * primero que quien llama tenga sesion de administrador.
 *
 * @version 1.0
 */

@RestController
@RequestMapping("/api/admin")
public class AdminControlador {

    /** Servicio que maneja los bloqueos (listarlos y levantarlos). */
    private final ServicioBloqueos servicioBloqueos;
    /** Servicio que da la informacion del campus (lugares y tramos). */
    private final ServicioCampus servicioCampus;
    /** Servicio que revisa si la persona que llama es administrador. */
    private final ServicioAutenticacion servicioAutenticacion;

    /**
     * Crea el controlador con los servicios que necesita. Spring los
     * entrega solo al arrancar la aplicacion.
     *
     * @param servicioBloqueos      servicio para manejar los bloqueos
     * @param servicioCampus        servicio para consultar lugares y tramos
     * @param servicioAutenticacion servicio para verificar al administrador
     */
    public AdminControlador(ServicioBloqueos servicioBloqueos,
                            ServicioCampus servicioCampus,
                            ServicioAutenticacion servicioAutenticacion) {
        this.servicioBloqueos = servicioBloqueos;
        this.servicioCampus = servicioCampus;
        this.servicioAutenticacion = servicioAutenticacion;
    }

    /** Todas las rutas de /api/admin pasan por aquí: sin sesión de administrador, error 403. */
    private void verificarAdmin(HttpServletRequest peticion) {
        servicioAutenticacion.exigirAdmin(AuthControlador.cuentaDe(peticion));
    }
    /**
     * Devuelve el historial completo de bloqueos, tanto los activos
     * como los ya resueltos.
     * Ruta: GET /api/admin/bloqueos
     *
     * @param peticion la peticion HTTP, se usa para verificar al administrador
     * @return la lista de todos los bloqueos
     */

    @GetMapping("/bloqueos")
    public List<Bloqueo> historialBloqueos(HttpServletRequest peticion) {
        verificarAdmin(peticion);
        return servicioBloqueos.listarTodos();
    }
    /**
     * Marca un bloqueo como resuelto, para que el camino se pueda volver a usar.
     * Ruta: PATCH /api/admin/bloqueos/{id}/resolver
     *
     * @param id       id del bloqueo que se quiere resolver (viene en la URL)
     * @param peticion la peticion HTTP, se usa para verificar al administrador
     * @return un mapa con el campo "mensaje" confirmando que se resolvio
     */
    @PatchMapping("/bloqueos/{id}/resolver")
    public Map<String, String> resolverBloqueo(@PathVariable String id, HttpServletRequest peticion) {
        verificarAdmin(peticion);
        servicioBloqueos.levantarBloqueo(id);
        return Map.of("mensaje", "Bloqueo resuelto y camino habilitado nuevamente.");
    }
    /**
     * Devuelve un resumen rapido del campus: cuantos lugares se pueden
     * escoger, cuantos tramos hay y cuantos bloqueos siguen activos.
     * Ruta: GET /api/admin/resumen
     *
     * @param peticion la peticion HTTP, se usa para verificar al administrador
     * @return un mapa con "totalLugares", "totalTramos" y "bloqueosActivos"
     */
    @GetMapping("/resumen")
    public Map<String, Object> resumenCampus(HttpServletRequest peticion) {
        verificarAdmin(peticion);
        int totalLugares = servicioCampus.listarLugaresSeleccionables().size();
        int totalTramos = servicioCampus.listarTramos().size();
        int bloqueosActivos = servicioBloqueos.listarActivos().size();

        return Map.of(
                "totalLugares", totalLugares,
                "totalTramos", totalTramos,
                "bloqueosActivos", bloqueosActivos
        );
    }
}
