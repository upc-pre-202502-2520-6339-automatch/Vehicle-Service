package com.vehicle.interfaces.rest.errors;

import java.time.Instant;
import java.util.List;

public record ApiError(
        Instant timestamp,
        int status,
        String error,      // "Bad Request", "Not Found", etc.
        String message,    // detalle para humanos
        String path,       // request URI
        List<FieldError> fieldErrors // para errores de validación
) {
    public static record FieldError(String field, String message) {}
}
