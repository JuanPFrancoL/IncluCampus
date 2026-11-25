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
    /** Servicio que da la informacion del campus (lugares y tramos). */
    private final ServicioCampus servicioCampus;

    /**
     * Crea el controlador con el servicio que necesita. Spring lo entrega
     * solo al arrancar la aplicacion.
     *
     * @param servicioCampus servicio para consultar lugares y tramos
     */
    public CampusControlador(ServicioCampus servicioCampus) {
        this.servicioCampus = servicioCampus;
    }

    @GetMapping("/lugares")
    public List<Ubicacion> lugares() {
        return servicioCampus.listarLugaresSeleccionables();
    }

    /**
     * Devuelve todos los tramos del campus, o sea los caminos que unen los lugares.
     * Ruta: GET /api/tramos
     *
     * @return la lista de tramos
     */
    @GetMapping("/tramos")
    public List<Tramo> tramos() {
        return servicioCampus.listarTramos();
    }
}