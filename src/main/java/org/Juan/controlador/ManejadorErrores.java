package org.Juan.controlador;

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
    public ResponseEntity<Map<String, String>> noEncontrado(RuntimeException error) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", error.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> pedidoInvalido(IllegalArgumentException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", error.getMessage()));
    }
}