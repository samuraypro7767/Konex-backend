package com.konex.Konex.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VentaCreateRequest {
    @NotNull
    private Long medicamentoId;

    @NotNull @Min(1)
    private Long cantidad;
}
