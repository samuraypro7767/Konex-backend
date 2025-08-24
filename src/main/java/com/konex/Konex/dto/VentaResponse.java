package com.konex.Konex.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de salida que representa la respuesta de una venta.
 * <p>
 * Incluye los datos principales de la venta (identificador, fecha/hora y total)
 * junto con el detalle de ítems vendidos como una lista de {@link VentaItemResponse}.
 * Este objeto está pensado para ser serializado y expuesto por la capa de API.
 * </p>
 *
 * <p><b>Sugerencias:</b></p>
 * <ul>
 *   <li>Si deseas evitar listas nulas al construir con Lombok, considera
 *       inicializar {@link #items} con una lista vacía o usar {@code @Builder.Default}.</li>
 * </ul>
 *
 * @see VentaItemResponse
 */
@Data
@Builder
public class VentaResponse {

    /**
     * Identificador único de la venta.
     */
    private Long id;
    /**
     * Fecha y hora en que se registró la venta.
     */
    private LocalDateTime fechaHora;

    /**
     * Monto total de la venta.
     */
    private BigDecimal valorTotal;

    /**
     * Ítems (líneas) que componen la venta, cada uno con su información resumida.
     */
    private List<VentaItemResponse> items;
}
