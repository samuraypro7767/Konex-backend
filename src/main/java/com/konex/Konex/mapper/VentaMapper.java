package com.konex.Konex.mapper;

import com.konex.Konex.dto.VentaItemResponse;
import com.konex.Konex.dto.VentaResponse;
import com.konex.Konex.model.DetalleVenta;
import com.konex.Konex.model.Venta;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Mapper utilitario para convertir entidades del dominio relacionadas con ventas
 * ({@link Venta} y {@link DetalleVenta}) a sus respectivos DTOs de salida
 * ({@link VentaResponse} y {@link VentaItemResponse}).
 * <p>
 * <b>Nota:</b> Este mapper asume que las colecciones y asociaciones necesarias
 * están cargadas (o dentro de un contexto de persistencia activo) al momento de
 * la conversión para evitar {@code LazyInitializationException}.
 * </p>
 */
public class VentaMapper {
    /**
     * Convierte una entidad {@link Venta} a su representación DTO {@link VentaResponse}.
     *
     * @param entity entidad de venta a convertir; no debe ser {@code null}
     * @return DTO con los datos principales de la venta y la lista de ítems
     */
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
    /**
     * Convierte un {@link DetalleVenta} a su DTO {@link VentaItemResponse}.
     *
     * @param d detalle de venta a convertir; no debe ser {@code null}
     * @return DTO con la información del ítem (medicamento, cantidad, precios)
     */
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
