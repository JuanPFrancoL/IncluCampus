package org.Juan.controlador;

import org.Juan.modelo.Ruta;
import org.Juan.servicio.ServicioRutas;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RutaControlador {

    private final ServicioRutas servicioRutas;

    public RutaControlador(ServicioRutas servicioRutas) {
        this.servicioRutas = servicioRutas;
    }

    @GetMapping("/ruta")
    public Ruta ruta(@RequestParam String origen,
                     @RequestParam String destino,
                     @RequestParam(defaultValue = "general") String perfil) {
        return servicioRutas.buscarRuta(origen, destino, perfil);
    }
}