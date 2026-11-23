// ServicioAutenticacion.java
package org.Juan.servicio;

import org.Juan.excepcion.CredencialesInvalidasException;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ServicioAutenticacion {

    public Map<String, String> autenticarAdmin(String usuario, String clave) {
        // Credenciales de administrador de planta física
        if ("admin".equals(usuario) && "uam2026".equals(clave)) {
            return Map.of(
                    "rol", "ADMIN",
                    "nombre", "Administrador Planta Física"
            );
        }
        throw new CredencialesInvalidasException("Usuario o contraseña incorrectos.");
    }
}