// BloqueoControlador.java
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
    public List<Bloqueo> listarActivos() {
        return servicioBloqueos.listarActivos();
    }

    @PostMapping
    public Bloqueo crear(@RequestBody Map<String, String> datos) {
        return servicioBloqueos.reportar(
                datos.get("tramoId"),
                datos.get("motivo"),
                datos.getOrDefault("descripcion", "")
        );
    }

    @PatchMapping("/{id}/levantar")
    public Map<String, String> levantar(@PathVariable String id) {
        servicioBloqueos.levantarBloqueo(id);
        return Map.of("mensaje", "Bloqueo levantado correctamente.");
    }
}