package org.Juan.controlador;

import org.Juan.modelo.tramo.Tramo;
import org.Juan.modelo.ubicacion.Ubicacion;
import org.Juan.servicio.ServicioCampus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CampusControlador {

    private final ServicioCampus servicioCampus;

    public CampusControlador(ServicioCampus servicioCampus) {
        this.servicioCampus = servicioCampus;
    }

    @GetMapping("/lugares")
    public List<Ubicacion> lugares() {
        return servicioCampus.listarLugaresSeleccionables();
    }

    @GetMapping("/tramos")
    public List<Tramo> tramos() {
        return servicioCampus.listarTramos();
    }
}