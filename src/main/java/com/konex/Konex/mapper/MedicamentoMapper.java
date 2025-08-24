package com.konex.Konex.mapper;

import com.konex.Konex.dto.MedicamentoRequest;
import com.konex.Konex.dto.MedicamentoResponse;
import com.konex.Konex.model.Laboratorio;
import com.konex.Konex.model.Medicamento;

/**
 * Clase utilitaria que proporciona métodos de mapeo entre el dominio y los DTOs
 * para la entidad {@link Medicamento}.
 * <p>
 * Ofrece conversiones:
 * </p>
 * <ul>
 *   <li>De {@link MedicamentoRequest} + {@link Laboratorio} a entidad {@link Medicamento}.</li>
 *   <li>De entidad {@link Medicamento} a {@link MedicamentoResponse}.</li>
 * </ul>
 * <p>
 * Nota: al no establecer explícitamente el campo {@code activo} al construir la entidad,
 * se respetará el valor por defecto definido mediante {@code @Builder.Default} (1 = activo).
 * Asegúrate de que las asociaciones perezosas estén disponibles para evitar
 * {@code LazyInitializationException} al convertir a DTO.
 * </p>
 *
 * @see MedicamentoRequest
 * @see MedicamentoResponse
 * @see Medicamento
 * @see Laboratorio
 */
public class MedicamentoMapper {
    /**
     * Convierte un DTO de entrada {@link MedicamentoRequest} y un {@link Laboratorio}
     * ya resuelto a una entidad {@link Medicamento}.
     *
     * @param dto          datos de entrada del medicamento; no debe ser {@code null}
     * @param laboratorio  entidad de laboratorio asociada; no debe ser {@code null}
     * @return instancia de {@link Medicamento} construida a partir del DTO y el laboratorio
     */
    public static Medicamento toEntity(MedicamentoRequest dto, Laboratorio laboratorio) {
        return Medicamento.builder()
                .nombre(dto.getNombre())
                .laboratorio(laboratorio)
                .fechaFabricacion(dto.getFechaFabricacion())
                .fechaVencimiento(dto.getFechaVencimiento())
                .cantidadStock(dto.getCantidadStock())
                .valorUnitario(dto.getValorUnitario())
                .build();
    }

    /**
     * Convierte una entidad {@link Medicamento} a su DTO de salida {@link MedicamentoResponse}.
     *
     * @param entity entidad del dominio; no debe ser {@code null}
     * @return DTO con la información pública del medicamento
     */
    public static MedicamentoResponse toResponse(Medicamento entity) {
        return MedicamentoResponse.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .laboratorioId(entity.getLaboratorio().getId())
                .laboratorioNombre(entity.getLaboratorio().getNombre())
                .fechaFabricacion(entity.getFechaFabricacion())
                .fechaVencimiento(entity.getFechaVencimiento())
                .cantidadStock(entity.getCantidadStock())
                .valorUnitario(entity.getValorUnitario())
                .activo(entity.getActivo())
                .build();
    }
}
