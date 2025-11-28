package com.mcic.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones
 * Evita exposición de información técnica sensible (DAST: Information Disclosure)
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Maneja excepciones no capturadas
     * Retorna un mensaje genérico sin detalles técnicos
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Se ha producido un error en el servidor");
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        // Log interno (sin exponer al cliente)
        System.err.println("Error: " + ex.getClass().getName());
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja excepciones de validación
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleValidationException(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Datos inválidos");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de estado ilegal
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleStateException(IllegalStateException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Operación no permitida");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.CONFLICT.value());
        
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
