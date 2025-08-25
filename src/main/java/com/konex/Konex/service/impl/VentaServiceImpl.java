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
/**
 * Implementación de {@link VentaService} que orquesta el ciclo de vida de una venta.
 * <p>
 * Responsabilidades principales:
 * </p>
 * <ul>
 *   <li>Validar la solicitud de creación de venta (cantidad &gt; 0).</li>
 *   <li>Verificar existencia del medicamento y disponibilidad de stock.</li>
 *   <li>Descontar inventario de manera atómica dentro de la transacción.</li>
 *   <li>Construir entidad {@link Venta} y su único {@link DetalleVenta} asociado, calcular totales y persistir.</li>
 *   <li>Exponer resultados como DTO mediante {@link VentaMapper}.</li>
 * </ul>
 *
 * <p><b>Transaccionalidad:</b></p>
 * <ul>
 *   <li>{@link #crearVenta(VentaCreateRequest)}: transacción de escritura.</li>
 *   <li>{@link #listarTodas()}, {@link #obtenerVenta(Long)} y {@link #listarPorRango(LocalDate, LocalDate, Pageable)}: solo lectura.</li>
 * </ul>
 *
 * <p><b>Concurrencia / Integridad:</b> El descuento de stock se hace con una lectura y escritura simples.
 * En escenarios de alta concurrencia considera emplear bloqueo pesimista/optimista o una columna
 * de versión para evitar sobreventa.</p>
 */
@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final MedicamentoRepository medicamentoRepository;
    /**
     * Crea una venta para un medicamento y cantidad indicados.
     * <p>
     * Flujo:
     * </p>
     * <ol>
     *   <li>Valida que la cantidad sea &gt; 0.</li>
     *   <li>Obtiene el medicamento; si no existe, lanza {@link NotFoundException}.</li>
     *   <li>Valida stock suficiente; si no hay, lanza excepción de negocio.</li>
     *   <li>Descuenta stock y persiste el medicamento.</li>
     *   <li>Crea la venta con un detalle, calcula el total y la persiste.</li>
     *   <li>Devuelve la venta mapeada a {@link VentaResponse}.</li>
     * </ol>
     *
     * @param req datos de creación (medicamentoId y cantidad), no {@code null}
     * @return representación de la venta creada
     * @throws NotFoundException   si el medicamento no existe
     * @throws BusinessException   o {@link IllegalArgumentException} si la cantidad es inválida o no hay stock suficiente
     */
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
    /**
     * Lista todas las ventas  paginación.
     *
     * @return lista completa de ventas mapeadas a {@link VentaResponse}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<VentaResponse> listarTodas(Pageable pageable) {
        return ventaRepository.findAll(pageable)
                .map(VentaMapper::toResponse);
    }

    /**
     * Obtiene una venta por su identificador.
     *
     * @param id identificador de la venta, no {@code null}
     * @return la venta encontrada mapeada a {@link VentaResponse}
     * @throws NotFoundException si no existe una venta con el id indicado
     */
    @Transactional(readOnly = true)
    @Override
    public VentaResponse obtenerVenta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada"));
        return VentaMapper.toResponse(venta);
    }
    /**
     * Lista ventas dentro de un rango de fechas por día calendario, de forma paginada.
     * <p>
     * Convención utilizada para el rango: {@code [desde 00:00:00, hasta 23:59:59.999999999]}.
     * Internamente se transforma a:
     * <pre>
     * start = desde.atStartOfDay()
     * end   = hasta.plusDays(1).atStartOfDay().minusNanos(1)
     * </pre>
     * y se delega a {@code findByFechaHoraBetween(start, end, pageable)}.
     * </p>
     *
     * @param desde    día inicial (inclusive), no {@code null}
     * @param hasta    día final (inclusive), no {@code null} y ≥ {@code desde}
     * @param pageable información de paginación y orden
     * @return página de ventas en el rango mapeadas a {@link VentaResponse}
     * @throws IllegalArgumentException si {@code hasta} es anterior a {@code desde} (validación recomendada en capa superior)
     */
    @Transactional(readOnly = true)
    public Page<VentaResponse> listarPorRango(LocalDate desde, LocalDate hasta, Pageable pageable) {
        var start = desde.atStartOfDay();
        var end   = hasta.plusDays(1).atStartOfDay().minusNanos(1);
        return ventaRepository.findByFechaHoraBetween(start, end, pageable)
                .map(VentaMapper::toResponse);
    }

}
