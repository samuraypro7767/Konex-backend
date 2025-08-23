package com.konex.Konex.mapper;

import com.konex.Konex.dto.CotizacionResponse;
import com.konex.Konex.model.Medicamento;

import java.math.BigDecimal;

public class CotizacionMapper {

    public static CotizacionResponse toResponse(Medicamento med, long cantidad) {
        BigDecimal total = med.getValorUnitario().multiply(BigDecimal.valueOf(cantidad));
        boolean puedeVender = cantidad <= med.getCantidadStock();

        return CotizacionResponse.builder()
                .medicamentoId(med.getId())
                .medicamentoNombre(med.getNombre())
                .cantidadSolicitada(cantidad)
                .stockDisponible(med.getCantidadStock())
                .valorUnitario(med.getValorUnitario())
                .valorTotal(total)
                .puedeVender(puedeVender)
                .build();
    }
}
