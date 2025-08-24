package com.konex.Konex.exception;

/**
 * Excepción (no verificada) para indicar que un recurso del dominio
 * no fue encontrado.
 * <p>
 * Suele lanzarse en la capa de servicio cuando una entidad requerida
 * (p. ej. Medicamento, Venta, Laboratorio) no existe. El
 * {@code GlobalExceptionHandler} la mapea a <b>HTTP 404 Not Found</b>.
 * </p>
 *
 * @see GlobalExceptionHandler
 */
public class NotFoundException extends RuntimeException {
    /**
     * Crea una nueva excepción de recurso no encontrado con el mensaje dado.
     *
     * @param message descripción legible del recurso ausente
     */
    public NotFoundException(String message) {
        super(message);
    }
}
