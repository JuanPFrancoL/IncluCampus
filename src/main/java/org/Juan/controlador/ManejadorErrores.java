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

@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler({UbicacionNoEncontradaException.class, RutaNoDisponibleException.class})
    public ResponseEntity<?> noEncontrado(RuntimeException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", error.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, BloqueoInvalidoException.class})
    public ResponseEntity<?> pedidoInvalido(RuntimeException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", error.getMessage()));
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<?> noAutorizado(CredencialesInvalidasException error) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensaje", error.getMessage()));
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<?> prohibido(AccesoDenegadoException error) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", error.getMessage()));
    }
}