package org.Juan.controlador;

import jakarta.servlet.http.HttpServletRequest;
import org.Juan.modelo.Bloqueo;
import org.Juan.servicio.ServicioAutenticacion;
import org.Juan.servicio.ServicioBloqueos;
import org.Juan.servicio.ServicioCampus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminControlador {

    private final ServicioBloqueos servicioBloqueos;
    private final ServicioCampus servicioCampus;
    private final ServicioAutenticacion servicioAutenticacion;

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

    @GetMapping("/bloqueos")
    public List<Bloqueo> historialBloqueos(HttpServletRequest peticion) {
        verificarAdmin(peticion);
        return servicioBloqueos.listarTodos();
    }

    @PatchMapping("/bloqueos/{id}/resolver")
    public Map<String, String> resolverBloqueo(@PathVariable String id, HttpServletRequest peticion) {
        verificarAdmin(peticion);
        servicioBloqueos.levantarBloqueo(id);
        return Map.of("mensaje", "Bloqueo resuelto y camino habilitado nuevamente.");
    }

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
