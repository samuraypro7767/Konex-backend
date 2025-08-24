package com.konex.Konex.mapper;

import com.konex.Konex.dto.MedicamentoRequest;
import com.konex.Konex.dto.MedicamentoResponse;
import com.konex.Konex.model.Laboratorio;
import com.konex.Konex.model.Medicamento;

public class MedicamentoMapper {

    // Convierte DTO de entrada a Entidad
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

    // Convierte Entidad a DTO de respuesta
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
