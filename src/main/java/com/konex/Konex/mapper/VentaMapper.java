package com.konex.Konex.mapper;

import com.konex.Konex.dto.VentaItemResponse;
import com.konex.Konex.dto.VentaResponse;
import com.konex.Konex.model.DetalleVenta;
import com.konex.Konex.model.Venta;

import java.util.List;
import java.util.stream.Collectors;

public class VentaMapper {

    public static VentaResponse toResponse(Venta entity) {
        List<VentaItemResponse> items = entity.getDetalles().stream()
                .map(VentaMapper::toItemResponse)
                .collect(Collectors.toList());

        return VentaResponse.builder()
                .id(entity.getId())
                .fechaHora(entity.getFechaHora())
                .valorTotal(entity.getValorTotal())
                .items(items)
                .build();
    }

    private static VentaItemResponse toItemResponse(DetalleVenta d) {
        return VentaItemResponse.builder()
                .medicamentoId(d.getMedicamento().getId())
                .medicamentoNombre(d.getMedicamento().getNombre())
                .cantidad(d.getCantidad())
                .valorUnitario(d.getValorUnitario())
                .valorLinea(d.getValorLinea())
                .build();
    }
}
