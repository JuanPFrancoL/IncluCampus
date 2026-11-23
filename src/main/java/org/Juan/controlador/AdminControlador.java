package org.Juan.controlador;

import org.Juan.excepcion.AccesoDenegadoException;
import org.Juan.modelo.Bloqueo;
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

    public AdminControlador(ServicioBloqueos servicioBloqueos, ServicioCampus servicioCampus) {
        this.servicioBloqueos = servicioBloqueos;
        this.servicioCampus = servicioCampus;
    }

    private void verificarAdmin(String rol) {
        if (!"ADMIN".equals(rol)) {
            throw new AccesoDenegadoException("Acceso denegado: se requiere rol de administrador.");
        }
    }

    @GetMapping("/bloqueos")
    public List<Bloqueo> historialBloqueos(@RequestHeader(value = "X-Rol", defaultValue = "") String rol) {
        verificarAdmin(rol);
        return servicioBloqueos.listarTodos();
    }

    @PatchMapping("/bloqueos/{id}/resolver")
    public Map<String, String> resolverBloqueo(
            @PathVariable String id,
            @RequestHeader(value = "X-Rol", defaultValue = "") String rol) {
        verificarAdmin(rol);
        servicioBloqueos.levantarBloqueo(id);
        return Map.of("mensaje", "Bloqueo resuelto y camino habilitado nuevamente.");
    }

    @GetMapping("/resumen")
    public Map<String, Object> resumenCampus(@RequestHeader(value = "X-Rol", defaultValue = "") String rol) {
        verificarAdmin(rol);
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