package com.konex.Konex.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
/**
 * Manejador global de excepciones para la API.
 * <p>
 * Anotado con {@link RestControllerAdvice} para interceptar excepciones lanzadas
 * por los controladores REST y transformar las mismas en respuestas HTTP
 * estandarizadas (cuerpo JSON) con el código de estado correspondiente.
 * </p>
 *
 * <p><b>Esquema de respuesta:</b></p>
 * <pre>
 * {
 *   "error":   "Mensaje legible",
 *   "status":  400|404|500,
 *   "details": "Mensaje técnico (solo en errores genéricos)"
 * }
 * </pre>
 *
 * <p><b>Seguridad/observabilidad:</b></p>
 * <ul>
 *   <li>Este handler no expone trazas de pila. Para depuración, usa logs.</li>
 *   <li>El endpoint genérico devuelve un mensaje controlado para evitar fugas de información.</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Maneja recursos no encontrados en el dominio (404).
     * <p>Mapea {@link NotFoundException} a <code>HTTP 404 Not Found</code>.</p>
     *
     * @param ex excepción de recurso no encontrado
     * @return respuesta con mensaje de error y código 404
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Maneja violaciones de reglas de negocio (400).
     * <p>Mapea {@link BusinessException} a <code>HTTP 400 Bad Request</code>.</p>
     *
     * @param ex excepción de negocio
     * @return respuesta con mensaje de error y código 400
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(BusinessException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja errores de validación provenientes de {@code @Valid} (400).
     * <p>
     * Toma el primer error de campo reportado por {@link MethodArgumentNotValidException}
     * y lo devuelve como mensaje de la respuesta.
     * </p>
     *
     * @param ex excepción arrojada por fallos de validación de bean
     * @return respuesta con mensaje del primer error y código 400
     * @implNote Si necesitas devolver <i>todos</i> los errores, puedes iterar
     *           sobre {@code ex.getBindingResult().getFieldErrors()} y construir
     *           una lista en el cuerpo en lugar de solo el primero.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getBindingResult().getFieldError().getDefaultMessage());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Manejo genérico de excepciones no contempladas (500).
     * <p>
     * Devuelve un mensaje controlado para el cliente y el detalle técnico
     * en el campo {@code details}. Considera registrar la traza en logs.
     * </p>
     *
     * @param ex excepción no controlada
     * @return respuesta con mensaje genérico y código 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Error interno del servidor");
        error.put("details", ex.getMessage());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
