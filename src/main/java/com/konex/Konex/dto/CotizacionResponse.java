package com.konex.Konex.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
/**
 * DTO de salida que representa el resultado de una cotización de venta.
 * <p>
 * Incluye información del medicamento, la cantidad solicitada, el stock disponible,
 * el precio unitario y el valor total estimado, además de un indicador de si la venta
 * puede realizarse con el inventario actual.
 * </p>
 *
 * <p><b>Notas:</b></p>
 * <ul>
 *   <li>Los importes monetarios se modelan con {@link BigDecimal} para evitar pérdidas de precisión.</li>
 *   <li>{@code puedeVender} suele ser verdadero cuando {@code cantidadSolicitada ≤ stockDisponible}.</li>
 *   <li>El {@code valorTotal} típicamente corresponde a {@code cantidadSolicitada × valorUnitario}
 *       (sin impuestos/descuentos salvo que la lógica de negocio indique lo contrario).</li>
 * </ul>
 *
 * @see MedicamentoResponse
 * @see VentaCreateRequest
 */
@Data
@Builder
public class CotizacionResponse {


    /**
     * Identificador del medicamento cotizado.
     */
    private Long medicamentoId;


    /**
     * Nombre del medicamento cotizado.
     */
    private String medicamentoNombre;
    /**
     * Cantidad solicitada por el cliente para la cotización.
     */
    private Long cantidadSolicitada;
    /**
     * Unidades disponibles en inventario al momento de la cotización.
     */
    private Long stockDisponible;

    /**
     * Precio unitario aplicado en la cotización.
     */
    private BigDecimal valorUnitario;
    /**
     * Valor total estimado de la cotización.
     * <p>Usualmente: {@code cantidadSolicitada × valorUnitario}.</p>
     */
    private BigDecimal valorTotal;

    /**
     * Indica si es posible concretar la venta inmediatamente con el stock actual.
     * <p>Convención típica: {@code true} si {@code cantidadSolicitada ≤ stockDisponible}.</p>
     */
    private boolean puedeVender;
}
