package com.konex.Konex.service.impl;

import org.springframework.data.domain.Pageable; // ✅
import com.konex.Konex.dto.VentaCreateRequest;
import com.konex.Konex.dto.VentaResponse;
import com.konex.Konex.exception.BusinessException;
import com.konex.Konex.exception.NotFoundException;
import com.konex.Konex.mapper.VentaMapper;
import com.konex.Konex.model.DetalleVenta;
import com.konex.Konex.model.Medicamento;
import com.konex.Konex.model.Venta;
import com.konex.Konex.repository.MedicamentoRepository;
import com.konex.Konex.repository.VentaRepository;
import com.konex.Konex.service.VentaService;
import com.konex.Konex.utils.DateRange;
import com.konex.Konex.utils.Validators;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final MedicamentoRepository medicamentoRepository;

    @Transactional
    @Override
    public VentaResponse crearVenta(VentaCreateRequest req) {
        Validators.check(req.getCantidad() != null && req.getCantidad() > 0, "La cantidad debe ser mayor que cero");

        // 1) Obtener medicamento y validar stock
        Medicamento med = medicamentoRepository.findById(req.getMedicamentoId())
                .orElseThrow(() -> new NotFoundException("Medicamento no encontrado"));

        Validators.check(med.getCantidadStock() >= req.getCantidad(), "Stock insuficiente para la venta");

        // 2) Descontar stock
        med.setCantidadStock(med.getCantidadStock() - req.getCantidad());
        medicamentoRepository.save(med);

        // 3) Crear venta y detalle
        Venta venta = new Venta();
        venta.setFechaHora(LocalDateTime.now());
        venta.setValorTotal(BigDecimal.ZERO);

        DetalleVenta det = new DetalleVenta();
        det.setVenta(venta);
        det.setMedicamento(med);
        det.setCantidad(req.getCantidad());
        det.setValorUnitario(med.getValorUnitario());
        det.setValorLinea(med.getValorUnitario().multiply(BigDecimal.valueOf(req.getCantidad())));

        venta.setDetalles(List.of(det));

        // 4) Calcular y setear total
        BigDecimal total = det.getValorLinea();
        venta.setValorTotal(total);

        // 5) Guardar y responder
        venta = ventaRepository.save(venta);
        return VentaMapper.toResponse(venta);
    }
    @Transactional(readOnly = true)
    @Override
    public List<VentaResponse> listarTodas() {
        return ventaRepository.findAll().stream()
                .map(VentaMapper::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    @Override
    public VentaResponse obtenerVenta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada"));
        return VentaMapper.toResponse(venta);
    }

    @Transactional(readOnly = true)
    public Page<VentaResponse> listarPorRango(LocalDate desde, LocalDate hasta, Pageable pageable) {
        var start = desde.atStartOfDay();
        var end   = hasta.plusDays(1).atStartOfDay().minusNanos(1);
        return ventaRepository.findByFechaHoraBetween(start, end, pageable)
                .map(VentaMapper::toResponse);
    }

}
