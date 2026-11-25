package org.Juan.controlador;

import org.Juan.modelo.Ruta;
import org.Juan.servicio.ServicioRutas;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controlador para pedir una ruta entre dos lugares del campus (la ruta
 * empieza con /api). La persona manda el origen, el destino y su perfil
 * de accesibilidad, y recibe la ruta calculada. No pide sesion.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api")
public class RutaControlador {
    /** Servicio que calcula las rutas. */
    private final ServicioRutas servicioRutas;
    /**
     * Crea el controlador con el servicio que necesita. Spring lo entrega
     * solo al arrancar la aplicacion.
     *
     * @param servicioRutas servicio para buscar rutas
     */
    public RutaControlador(ServicioRutas servicioRutas) {
        this.servicioRutas = servicioRutas;
    }
    /**
     * Busca la mejor ruta entre dos lugares, segun el perfil de la persona.
     * Ruta: GET /api/ruta?origen=...&destino=...&perfil=...
     *
     * <p>Si no se manda el perfil, se usa "general". Si no hay camino entre
     * los dos lugares, o alguno no existe, el {@link ManejadorErrores}
     * responde con un error 404.</p>
     *
     * @param origen  id del lugar de partida
     * @param destino id del lugar de llegada
     * @param perfil  codigo del perfil de accesibilidad ("general", "movilidad"
     *                o "visual"); si no se manda, es "general"
     * @return la ruta encontrada, con sus puntos, tramos y costo total
     */

    @GetMapping("/ruta")
    public Ruta ruta(@RequestParam String origen,
                     @RequestParam String destino,
                     @RequestParam(defaultValue = "general") String perfil) {
        return servicioRutas.buscarRuta(origen, destino, perfil);
    }
}