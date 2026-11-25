package org.Juan.controlador;

import org.Juan.excepcion.AccesoDenegadoException;
import org.Juan.excepcion.BloqueoInvalidoException;
import org.Juan.excepcion.CredencialesInvalidasException;
import org.Juan.excepcion.RutaNoDisponibleException;
import org.Juan.excepcion.UbicacionNoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
/**
 * Manejador de errores de todos los controladores. Cuando en cualquier
 * parte del programa se lanza una de estas excepciones, esta clase la
 * atrapa y la convierte en una respuesta HTTP con el codigo de error
 * correcto y un mensaje, para que la pagina sepa que paso.
 * Todas las respuestas tienen la forma {"mensaje": "..."}.
 *
 * @version 1.0
 */

@RestControllerAdvice
public class ManejadorErrores {
    /**
     * Responde cuando no se encontro lo que se pedia: un lugar que no
     * existe, o una ruta que no se puede armar.
     * Codigo HTTP: 404 (no encontrado).
     *
     * @param error la excepcion que se lanzo
     * @return la respuesta con el codigo 404 y el mensaje del error
     */

    @ExceptionHandler({UbicacionNoEncontradaException.class, RutaNoDisponibleException.class})
    public ResponseEntity<?> noEncontrado(RuntimeException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", error.getMessage()));
    }
    /**
     * Responde cuando el pedido que mando la persona esta mal hecho: un
     * dato invalido o un bloqueo con datos incorrectos.
     * Codigo HTTP: 400 (pedido incorrecto).
     *
     * @param error la excepcion que se lanzo
     * @return la respuesta con el codigo 400 y el mensaje del error
     */

    @ExceptionHandler({IllegalArgumentException.class, BloqueoInvalidoException.class})
    public ResponseEntity<?> pedidoInvalido(RuntimeException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", error.getMessage()));
    }
    /**
     * Responde cuando el usuario o la clave del inicio de sesion no son correctos.
     * Codigo HTTP: 401 (no autorizado).
     *
     * @param error la excepcion que se lanzo
     * @return la respuesta con el codigo 401 y el mensaje del error
     */

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<?> noAutorizado(CredencialesInvalidasException error) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensaje", error.getMessage()));
    }
    /**
     * Responde cuando la persona intenta hacer algo para lo que no tiene
     * permiso, como usar una ruta de administrador sin serlo.
     * Codigo HTTP: 403 (prohibido).
     *
     * @param error la excepcion que se lanzo
     * @return la respuesta con el codigo 403 y el mensaje del error
     */
    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<?> prohibido(AccesoDenegadoException error) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", error.getMessage()));
    }
}