package org.Juan.controlador;

import jakarta.servlet.http.HttpServletRequest;
import org.Juan.modelo.Bloqueo;
import org.Juan.servicio.ServicioAutenticacion;
import org.Juan.servicio.ServicioBloqueos;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * Controlador de los bloqueos (todas las rutas empiezan con /api/bloqueos).
 * Permite ver los bloqueos, que cualquier persona reporte uno nuevo, y que
 * solo el administrador levante uno.
 *
 * @version 1.0
 */

@RestController
@RequestMapping("/api/bloqueos")
public class BloqueoControlador {

    /** Servicio que maneja los bloqueos (listarlos, reportarlos y levantarlos). */
    private final ServicioBloqueos servicioBloqueos;
    /** Servicio que revisa si la persona que llama es administrador. */
    private final ServicioAutenticacion servicioAutenticacion;

    /**
     * Crea el controlador con los servicios que necesita. Spring los
     * entrega solo al arrancar la aplicacion.
     *
     * @param servicioBloqueos      servicio para manejar los bloqueos
     * @param servicioAutenticacion servicio para verificar al administrador
     */
    public BloqueoControlador(ServicioBloqueos servicioBloqueos, ServicioAutenticacion servicioAutenticacion) {
        this.servicioBloqueos = servicioBloqueos;
        this.servicioAutenticacion = servicioAutenticacion;
    }

    /**
     * Devuelve todos los bloqueos. No pide sesion, cualquiera puede verlos.
     * Ruta: GET /api/bloqueos
     *
     * @return la lista de bloqueos
     */
    @GetMapping
    public List<Bloqueo> listarTodos() {
        return servicioBloqueos.listarTodos();
    }

    /**
     * Cualquier persona puede reportar un bloqueo.
     * Ruta: POST /api/bloqueos
     *
     * <p>Acepta nombres alternativos en los datos: para el tramo sirve
     * "camino" o "tramoId", y para la explicacion sirve "detalle" o
     * "descripcion" (si no viene ninguno, se deja vacia).</p>
     *
     * @param datos mapa con "camino" (o "tramoId"), "motivo" y "detalle" (o "descripcion")
     * @return el bloqueo que se creo
     */
    @PostMapping
    public Bloqueo crear(@RequestBody Map<String, String> datos) {
        String camino = datos.getOrDefault("camino", datos.get("tramoId"));
        String motivo = datos.get("motivo");
        String detalle = datos.getOrDefault("detalle", datos.getOrDefault("descripcion", ""));
        return servicioBloqueos.reportar(camino, motivo, detalle);
    }

    /**
     * Solo el administrador puede levantar un bloqueo.
     * Ruta: PATCH /api/bloqueos/{id}/levantar
     *
     * @param id       id del bloqueo que se quiere levantar (viene en la URL)
     * @param peticion la peticion HTTP, se usa para verificar al administrador
     * @return un mapa con el campo "mensaje" confirmando que se levanto
     */
    @PatchMapping("/{id}/levantar")
    public Map<String, String> levantar(@PathVariable String id, HttpServletRequest peticion) {
        servicioAutenticacion.exigirAdmin(AuthControlador.cuentaDe(peticion));
        servicioBloqueos.levantarBloqueo(id);
        return Map.of("mensaje", "Bloqueo levantado correctamente.");
    }
}
