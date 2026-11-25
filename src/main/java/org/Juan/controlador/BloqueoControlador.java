package org.Juan.controlador;

import jakarta.servlet.http.HttpServletRequest;
import org.Juan.modelo.Bloqueo;
import org.Juan.servicio.ServicioAutenticacion;
import org.Juan.servicio.ServicioBloqueos;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bloqueos")
public class BloqueoControlador {

    private final ServicioBloqueos servicioBloqueos;
    private final ServicioAutenticacion servicioAutenticacion;

    public BloqueoControlador(ServicioBloqueos servicioBloqueos, ServicioAutenticacion servicioAutenticacion) {
        this.servicioBloqueos = servicioBloqueos;
        this.servicioAutenticacion = servicioAutenticacion;
    }

    @GetMapping
    public List<Bloqueo> listarTodos() {
        return servicioBloqueos.listarTodos();
    }

    /** Cualquier persona puede reportar un bloqueo. */
    @PostMapping
    public Bloqueo crear(@RequestBody Map<String, String> datos) {
        String camino = datos.getOrDefault("camino", datos.get("tramoId"));
        String motivo = datos.get("motivo");
        String detalle = datos.getOrDefault("detalle", datos.getOrDefault("descripcion", ""));
        return servicioBloqueos.reportar(camino, motivo, detalle);
    }

    /** Solo el administrador puede levantar un bloqueo. */
    @PatchMapping("/{id}/levantar")
    public Map<String, String> levantar(@PathVariable String id, HttpServletRequest peticion) {
        servicioAutenticacion.exigirAdmin(AuthControlador.cuentaDe(peticion));
        servicioBloqueos.levantarBloqueo(id);
        return Map.of("mensaje", "Bloqueo levantado correctamente.");
    }
}
