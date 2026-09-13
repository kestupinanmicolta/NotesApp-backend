package com.notes.api.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return buildResponse(HttpStatus.BAD_REQUEST, "Error de validación", errors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas. Verifica tu usuario y contraseña.");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFound(UsernameNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        String message = ex.getMessage();
        if (message != null) {
            if (message.contains("already exists")) {
                if (message.contains("Username")) {
                    return buildResponse(HttpStatus.CONFLICT, "El nombre de usuario ya está en uso. Elige otro nombre.");
                }
                if (message.contains("Email")) {
                    return buildResponse(HttpStatus.CONFLICT, "El correo electrónico ya está registrado. Usa otro correo.");
                }
            }
            if (message.contains("Access denied")) {
                return buildResponse(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esta acción.");
            }
            if (message.contains("not found")) {
                return buildResponse(HttpStatus.NOT_FOUND, "Recurso no encontrado: " + message);
            }
            if (message.contains("Invalid") || message.contains("invalid")) {
                return buildResponse(HttpStatus.BAD_REQUEST, "Datos inválidos: " + message);
            }
        }
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + (message != null ? message : "Error desconocido"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor: " + ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message, List<String> details) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("details", details);
        return ResponseEntity.status(status).body(body);
    }
}