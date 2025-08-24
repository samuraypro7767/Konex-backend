package com.konex.Konex.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
/**
 * DTO de entrada para crear o actualizar un {@code Medicamento}.
 * <p>
 * Se utiliza en la capa de API para recibir los datos requeridos del medicamento.
 * Incluye restricciones de validación con Jakarta Bean Validation.
 * </p>
 *
 * <p><b>Sugerencias de validación adicionales (opcional):</b></p>
 * <ul>
 *   <li>Aplicar {@code @PastOrPresent} a {@link #fechaFabricacion} si no puede ser futura.</li>
 *   <li>Aplicar {@code @Future} o {@code @FutureOrPresent} a {@link #fechaVencimiento} si corresponde.</li>
 *   <li>Verificar en lógica de negocio que {@code fechaFabricacion} ≤ {@code fechaVencimiento}.</li>
 * </ul>
 *
 * @see com.konex.Konex.model.Medicamento
 * @see MedicamentoResponse
 */
@Data
public class MedicamentoRequest {

    /**
     * Nombre comercial o genérico del medicamento.
     * <p>No debe ser vacío o solo espacios.</p>
     */
    @NotBlank
    private String nombre;
    /**
     * Identificador del laboratorio asociado.
     * <p>Debe referenciar un laboratorio existente.</p>
     */
    @NotNull
    private Long laboratorioId;
    /**
     * Fecha de fabricación del medicamento.
     * <p>No debe ser nula. Usualmente no futura.</p>
     */
    @NotNull
    private LocalDate fechaFabricacion;
    /**
     * Fecha de vencimiento del medicamento.
     * <p>No debe ser nula. Debe ser posterior (o igual) a la fecha de fabricación según reglas de negocio.</p>
     */
    @NotNull
    private LocalDate fechaVencimiento;
    /**
     * Cantidad disponible para inventario inicial o ajuste.
     * <p>No debe ser negativa.</p>
     */
    @NotNull @PositiveOrZero
    private Long cantidadStock;
    /**
     * Precio unitario vigente del medicamento.
     * <p>Debe ser estrictamente positivo. Se recomienda {@link BigDecimal} para precisión financiera.</p>
     */
    @NotNull @Positive
    private BigDecimal valorUnitario;
}
