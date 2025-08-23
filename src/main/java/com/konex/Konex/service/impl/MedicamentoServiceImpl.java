package com.konex.Konex.service.impl;


import com.konex.Konex.dto.CotizacionResponse;
import com.konex.Konex.dto.MedicamentoRequest;
import com.konex.Konex.dto.MedicamentoResponse;
import com.konex.Konex.exception.NotFoundException;
import com.konex.Konex.mapper.CotizacionMapper;
import com.konex.Konex.mapper.MedicamentoMapper;
import com.konex.Konex.model.Laboratorio;
import com.konex.Konex.model.Medicamento;
import com.konex.Konex.repository.LaboratorioRepository;
import com.konex.Konex.repository.MedicamentoRepository;
import com.konex.Konex.service.MedicamentoService;
import com.konex.Konex.utils.Validators;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicamentoServiceImpl implements MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;
    private final LaboratorioRepository laboratorioRepository;

    @Transactional
    @Override
    public MedicamentoResponse crear(MedicamentoRequest req) {
        Laboratorio lab = laboratorioRepository.findById(req.getLaboratorioId())
                .orElseThrow(() -> new NotFoundException("Laboratorio no encontrado"));

        Medicamento entity = MedicamentoMapper.toEntity(req, lab);
        entity = medicamentoRepository.save(entity);
        return MedicamentoMapper.toResponse(entity);
    }

    @Transactional
    @Override
    public MedicamentoResponse actualizar(Long id, MedicamentoRequest req) {
        Medicamento entity = medicamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Medicamento no encontrado"));

        if (req.getLaboratorioId() != null) {
            Laboratorio lab = laboratorioRepository.findById(req.getLaboratorioId())
                    .orElseThrow(() -> new NotFoundException("Laboratorio no encontrado"));
            entity.setLaboratorio(lab);
        }
        entity.setNombre(req.getNombre());
        entity.setFechaFabricacion(req.getFechaFabricacion());
        entity.setFechaVencimiento(req.getFechaVencimiento());
        entity.setCantidadStock(req.getCantidadStock());
        entity.setValorUnitario(req.getValorUnitario());

        entity = medicamentoRepository.save(entity);
        return MedicamentoMapper.toResponse(entity);
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        if (!medicamentoRepository.existsById(id)) {
            throw new NotFoundException("Medicamento no encontrado");
        }
        medicamentoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public MedicamentoResponse obtener(Long id) {
        return medicamentoRepository.findById(id)
                .map(MedicamentoMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Medicamento no encontrado"));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MedicamentoResponse> listar(String nombre, Pageable pageable) {
        return medicamentoRepository.buscar(nombre, pageable).map(MedicamentoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public CotizacionResponse cotizar(Long medicamentoId, long cantidad) {
        Medicamento m = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() -> new NotFoundException("Medicamento no encontrado"));
        Validators.check(cantidad > 0, "La cantidad debe ser mayor que cero");
        return CotizacionMapper.toResponse(m, cantidad);
    }

    @Transactional
    @Override
    public void descontarStock(Long medicamentoId, long cantidad) {
        Validators.check(cantidad > 0, "La cantidad debe ser mayor que cero");

        Medicamento m = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() -> new NotFoundException("Medicamento no encontrado"));

        Validators.check(m.getCantidadStock() >= cantidad, "Stock insuficiente para la venta");

        m.setCantidadStock(m.getCantidadStock() - cantidad);
        medicamentoRepository.save(m);
    }
}
