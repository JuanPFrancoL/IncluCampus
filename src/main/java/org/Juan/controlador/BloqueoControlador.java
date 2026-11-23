package org.Juan.controlador;

import org.Juan.modelo.Bloqueo;
import org.Juan.servicio.ServicioBloqueos;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bloqueos")
public class BloqueoControlador {

    private final ServicioBloqueos servicioBloqueos;

    public BloqueoControlador(ServicioBloqueos servicioBloqueos) {
        this.servicioBloqueos = servicioBloqueos;
    }

    @GetMapping
    public List<Bloqueo> listarTodos() {
        return servicioBloqueos.listarTodos();
    }

    @PostMapping
    public Bloqueo crear(@RequestBody Map<String, String> datos) {
        String camino = datos.getOrDefault("camino", datos.get("tramoId"));
        String motivo = datos.get("motivo");
        String detalle = datos.getOrDefault("detalle", datos.getOrDefault("descripcion", ""));
        return servicioBloqueos.reportar(camino, motivo, detalle);
    }

    @PatchMapping("/{id}/levantar")
    public Map<String, String> levantar(@PathVariable String id) {
        servicioBloqueos.levantarBloqueo(id);
        return Map.of("mensaje", "Bloqueo levantado correctamente.");
    }
}