package com.konex.Konex.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
/**
 * DTO de entrada para registrar una venta de un medicamento.
 * <p>
 * Este objeto se utiliza en la capa de API para recibir los datos necesarios
 * al crear una venta (o agregar un detalle) indicando el medicamento y la cantidad.
 * Incluye validaciones con Jakarta Bean Validation.
 * </p>
 *
 * @see VentaResponse
 * @see VentaItemResponse
 */
@Data
public class VentaCreateRequest {
    /**
     * Identificador del medicamento a vender.
     * <p>Debe corresponder a un {@code Medicamento} existente.</p>
     */
    @NotNull
    private Long medicamentoId;

    /**
     * Cantidad de unidades a vender.
     * <p>Debe ser un valor entero mayor o igual a 1.</p>
     */
    @NotNull @Min(1)
    private Long cantidad;
}
