// AuthControlador.java
package org.Juan.controlador;

import org.Juan.servicio.ServicioAutenticacion;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthControlador {

    private final ServicioAutenticacion servicioAutenticacion;

    public AuthControlador(ServicioAutenticacion servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> credenciales) {
        return servicioAutenticacion.autenticarAdmin(
                credenciales.get("usuario"),
                credenciales.get("clave")
        );
    }
}