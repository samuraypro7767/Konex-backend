package com.konex.Konex.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class VentaResponse {
    private Long id;
    private LocalDateTime fechaHora;
    private BigDecimal valorTotal;
    private List<VentaItemResponse> items;
}
