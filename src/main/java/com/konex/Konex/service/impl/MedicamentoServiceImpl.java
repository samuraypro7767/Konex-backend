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
/**
 * Implementación de {@link MedicamentoService} que gestiona el ciclo de vida
 * de los medicamentos y operaciones de negocio asociadas (cotización y
 * descuento de stock).
 *
 * <p><b>Responsabilidades:</b></p>
 * <ul>
 *   <li>Crear y actualizar medicamentos validando la existencia del laboratorio.</li>
 *   <li>Eliminación lógica mediante el campo {@code activo} (1 = activo, 0 = inactivo).</li>
 *   <li>Listar y obtener medicamentos (omitirá inactivos en {@link #obtener(Long)}).</li>
 *   <li>Cotizar y descontar stock (bloquea operaciones cuando el medicamento está inactivo).</li>
 * </ul>
 *
 * <p><b>Transaccionalidad:</b></p>
 * <ul>
 *   <li>Métodos de modificación marcados con {@link Transactional} (por defecto lectura/escritura).</li>
 *   <li>Métodos de consulta marcados con {@code readOnly = true} para optimizar rendimiento.</li>
 * </ul>
 *
 * <p><b>Concurrencia:</b> En escenarios de alta concurrencia, considera control
 * de versiones optimista (campo {@code @Version}) o bloqueos pesimistas para
 * evitar inconsistencias al descontar stock.</p>
 */
@Service
@RequiredArgsConstructor
public class MedicamentoServiceImpl implements MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;
    private final LaboratorioRepository laboratorioRepository;
    /**
     * Crea un nuevo medicamento.
     * <p>Valida que el laboratorio indicado exista y luego persiste el medicamento.</p>
     *
     * @param req datos del medicamento (nombre, laboratorioId, fechas, stock, valor), no {@code null}
     * @return DTO con la información del medicamento creado
     * @throws NotFoundException si el laboratorio indicado no existe
     */
    @Transactional
    @Override
    public MedicamentoResponse crear(MedicamentoRequest req) {
        Laboratorio lab = laboratorioRepository.findById(req.getLaboratorioId())
                .orElseThrow(() -> new NotFoundException("Laboratorio no encontrado"));

        Medicamento entity = MedicamentoMapper.toEntity(req, lab);
        entity = medicamentoRepository.save(entity);
        return MedicamentoMapper.toResponse(entity);
    }
    /**
     * Actualiza un medicamento existente.
     * <p>Permite cambiar el laboratorio siempre que exista, así como los demás campos.</p>
     *
     * @param id  identificador del medicamento a actualizar, no {@code null}
     * @param req datos a aplicar (nombre, laboratorioId, fechas, stock, valor), no {@code null}
     * @return DTO del medicamento actualizado
     * @throws NotFoundException si el medicamento no existe o si se indica un laboratorio inexistente
     */
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
    /**
     * Elimina lógicamente un medicamento (marca {@code activo = 0}).
     *
     * @param id identificador del medicamento, no {@code null}
     * @throws NotFoundException si el medicamento no existe
     */
    @Transactional
    @Override
    public void eliminar(Long id) {
        Medicamento med = medicamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Medicamento no encontrado"));

        med.setActivo(0);
        medicamentoRepository.save(med);
    }

    /**
     * Lista medicamentos activos con filtro opcional por nombre (paginado).
     * <p>Delegado al repositorio {@link MedicamentoRepository#buscar(String, Pageable)}.</p>
     *
     * @param nombre   texto a buscar en el nombre (opcional; {@code null} o vacío = sin filtro)
     * @param pageable configuración de paginación y ordenamiento
     * @return página de medicamentos que cumplen el filtro
     */
    @Transactional(readOnly = true)
    @Override
    public Page<MedicamentoResponse> listar(String nombre, Pageable pageable) {
        return medicamentoRepository.buscar(nombre, pageable).map(MedicamentoMapper::toResponse);
    }

    /**
     * Obtiene un medicamento por su id, solo si está activo.
     *
     * @param id identificador del medicamento, no {@code null}
     * @return DTO del medicamento
     * @throws NotFoundException si el medicamento no existe o está inactivo
     */
    @Transactional(readOnly = true)
    @Override
    public MedicamentoResponse obtener(Long id) {
        Medicamento med = medicamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Medicamento no encontrado"));
        if (med.getActivo() != 1) throw new NotFoundException("Medicamento no encontrado");
        return MedicamentoMapper.toResponse(med);
    }

    /**
     * Genera una cotización para un medicamento y una cantidad dada.
     * <p>Bloquea la operación si el medicamento está inactivo o la cantidad es inválida.</p>
     *
     * @param medicamentoId identificador del medicamento, no {@code null}
     * @param cantidad      unidades solicitadas; debe ser &gt; 0
     * @return DTO con stock disponible, precio unitario, total y bandera de posibilidad de venta
     * @throws NotFoundException si el medicamento no existe
     * @throws IllegalArgumentException o com.konex.Konex.exception.BusinessException
     *         si el medicamento está inactivo o la cantidad ≤ 0 (según la implementación de {@link Validators})
     */
    @Transactional(readOnly = true)
    @Override
    public CotizacionResponse cotizar(Long medicamentoId, long cantidad) {
        Medicamento m = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() -> new NotFoundException("Medicamento no encontrado"));
        Validators.check(m.getActivo() == 1, "El medicamento está inactivo");
        Validators.check(cantidad > 0, "La cantidad debe ser mayor que cero");
        return CotizacionMapper.toResponse(m, cantidad);
    }


    /**
     * Descuenta unidades del stock de un medicamento.
     * <p>Valida que esté activo y que exista stock suficiente.</p>
     *
     * @param medicamentoId identificador del medicamento, no {@code null}
     * @param cantidad      unidades a descontar; debe ser &gt; 0
     * @throws NotFoundException si el medicamento no existe
     * @throws IllegalArgumentException o com.konex.Konex.exception.BusinessException
     *         si el medicamento está inactivo, la cantidad ≤ 0 o no hay stock suficiente
     */
    @Transactional
    @Override
    public void descontarStock(Long medicamentoId, long cantidad) {
        Validators.check(cantidad > 0, "La cantidad debe ser mayor que cero");
        Medicamento m = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() -> new NotFoundException("Medicamento no encontrado"));
        Validators.check(m.getActivo() == 1, "El medicamento está inactivo");
        Validators.check(m.getCantidadStock() >= cantidad, "Stock insuficiente para la venta");
        m.setCantidadStock(m.getCantidadStock() - cantidad);
        medicamentoRepository.save(m);
    }

}
