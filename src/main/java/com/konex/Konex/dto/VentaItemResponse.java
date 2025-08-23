package com.konex.Konex.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class VentaItemResponse {
    private Long medicamentoId;
    private String medicamentoNombre;
    private Long cantidad;
    private BigDecimal valorUnitario;
    private BigDecimal valorLinea;  // cantidad * valorUnitario
}
