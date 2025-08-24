package com.konex.Konex.exception;

/**
 * Excepción de negocio (no verificada) utilizada para señalar violaciones
 * de reglas de dominio o condiciones inválidas en los casos de uso.
 * <p>
 * Suele lanzarse desde la capa de servicio o utilidades como
 * {@code Validators.check(...)} cuando una pre/postcondición no se cumple.
 * Puede ser manejada por un {@code @ControllerAdvice} para devolver respuestas
 * HTTP 400/409 según la política de la API.
 * </p>
 */
public class BusinessException extends RuntimeException {
    /**
     * Crea una nueva excepción de negocio con el mensaje especificado.
     *
     * @param message descripción legible de la causa del error
     */
    public BusinessException(String message) {
        super(message);
    }
}
