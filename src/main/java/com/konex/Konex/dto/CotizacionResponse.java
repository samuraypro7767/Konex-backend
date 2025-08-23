package com.konex.Konex.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CotizacionResponse {
    private Long medicamentoId;
    private String medicamentoNombre;
    private Long cantidadSolicitada;
    private Long stockDisponible;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
    private boolean puedeVender;
}
