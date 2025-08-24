package com.konex.Konex.mapper;

import com.konex.Konex.dto.CotizacionResponse;
import com.konex.Konex.model.Medicamento;

import java.math.BigDecimal;

/**
 * Mapper utilitario para construir un {@link CotizacionResponse} a partir de un
 * {@link Medicamento} y una cantidad solicitada.
 * <p>
 * Reglas aplicadas:
 * </p>
 * <ul>
 *   <li><b>valorTotal</b> = {@code valorUnitario × cantidad} (sin impuestos/ descuentos).</li>
 *   <li><b>puedeVender</b> es {@code true} si {@code cantidad ≤ cantidadStock}.</li>
 * </ul>
 * <p>
 * Notas: los importes se manejan con {@link BigDecimal} para preservar precisión
 * financiera. Este método asume que {@code med} y su {@code valorUnitario} no son nulos
 * y que {@code cantidad} es no negativa; valida estas condiciones a nivel de servicio/controlador.
 * </p>
 */
public class CotizacionMapper {
    /**
     * Construye un {@link CotizacionResponse} a partir del medicamento y la cantidad solicitada.
     *
     * @param med      entidad de medicamento a cotizar; no debe ser {@code null}
     * @param cantidad cantidad solicitada; se asume {@code ≥ 0}
     * @return DTO con los datos de la cotización (stock, precio, total y posibilidad de venta)
     */
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
