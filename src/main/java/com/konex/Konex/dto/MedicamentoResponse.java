package com.konex.Konex.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
/**
 * DTO de salida que representa la información pública de un medicamento.
 * <p>
 * Está pensado para ser devuelto por la API, exponiendo datos básicos del
 * medicamento y su laboratorio asociado, así como fechas, stock, precio
 * unitario y estado (activo/inactivo).
 * </p>
 *
 * <p><b>Notas:</b></p>
 * <ul>
 *   <li>El campo {@code activo} sigue la convención: 1 = activo, 0 = inactivo.</li>
 *   <li>Los importes monetarios usan {@link BigDecimal} para precisión financiera.</li>
 * </ul>
 *
 * @see com.konex.Konex.model.Medicamento
 */
@Data
@Builder
public class MedicamentoResponse {


    /**
     * Identificador único del medicamento.
     */
    private Long id;
    /**
     * Nombre comercial o genérico del medicamento.
     */
    private String nombre;
    /**
     * Identificador del laboratorio asociado.
     */
    private Long laboratorioId;
    /**
     * Nombre del laboratorio asociado.
     */
    private String laboratorioNombre;
    /**
     * Fecha de fabricación del medicamento.
     */
    private LocalDate fechaFabricacion;
    /**
     * Fecha de vencimiento del medicamento.
     */
    private LocalDate fechaVencimiento;
    /**
     * Cantidad disponible en inventario (stock).
     */
    private Long cantidadStock;

    /**
     * Precio unitario vigente del medicamento.
     */
    private BigDecimal valorUnitario;
    /**
     * Indicador de estado: 1 = activo, 0 = inactivo.
     */
    private Integer activo;
}
