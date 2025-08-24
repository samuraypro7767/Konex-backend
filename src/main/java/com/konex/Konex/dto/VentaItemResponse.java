package com.konex.Konex.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO de salida que representa una línea (ítem) dentro de una venta.
 * <p>
 * Contiene la identificación y el nombre del medicamento, la cantidad vendida,
 * el precio unitario y el valor total de la línea.
 * </p>
 * <p><b>Notas:</b> Los importes monetarios se modelan con {@link BigDecimal}
 * para evitar pérdidas de precisión.</p>
 *
 * @see VentaResponse
 */
@Data
@Builder
public class VentaItemResponse {

    /**
     * Identificador del medicamento vendido.
     */
    private Long medicamentoId;
    /**
     * Nombre del medicamento vendido.
     */
    private String medicamentoNombre;
    /**
     * Cantidad vendida del medicamento.
     */
    private Long cantidad;
    /**
     * Precio unitario aplicado al medicamento en la venta.
     */
    private BigDecimal valorUnitario;
    /**
     * Importe total de la línea.
     * <p>Usualmente corresponde a: <em>cantidad × valorUnitario</em>.</p>
     */
    private BigDecimal valorLinea;  // cantidad * valorUnitario
}
