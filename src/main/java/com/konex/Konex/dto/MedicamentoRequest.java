package com.konex.Konex.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MedicamentoRequest {
    @NotBlank
    private String nombre;

    @NotNull
    private Long laboratorioId;

    @NotNull
    private LocalDate fechaFabricacion;

    @NotNull
    private LocalDate fechaVencimiento;

    @NotNull @PositiveOrZero
    private Long cantidadStock;

    @NotNull @Positive
    private BigDecimal valorUnitario;
}
