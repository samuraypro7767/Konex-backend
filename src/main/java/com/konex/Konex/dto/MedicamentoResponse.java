package com.konex.Konex.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class MedicamentoResponse {
    private Long id;
    private String nombre;
    private Long laboratorioId;
    private String laboratorioNombre;
    private LocalDate fechaFabricacion;
    private LocalDate fechaVencimiento;
    private Long cantidadStock;
    private BigDecimal valorUnitario;
}
