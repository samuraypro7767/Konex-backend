package com.konex.Konex.service;

import com.konex.Konex.dto.CotizacionResponse;
import com.konex.Konex.dto.MedicamentoRequest;
import com.konex.Konex.dto.MedicamentoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
/**
 * Contrato de la capa de servicio para operaciones sobre medicamentos.
 * <p>
 * Expone casos de uso para crear/actualizar/obtener/listar y eliminar (lógico o físico,
 * según la implementación) medicamentos, así como utilidades de negocio como cotizar
 * y descontar stock.
 * </p>
 *
 * <p><b>Convenciones y consideraciones:</b></p>
 * <ul>
 *   <li>Los importes monetarios se modelan con {@code BigDecimal} en los DTOs.</li>
 *   <li>Las validaciones de reglas de negocio (fechas, stock no negativo, estado activo)
 *       deben ejercerse en la implementación.</li>
 *   <li>Para listados, el filtrado por nombre debe ser insensible a mayúsculas/minúsculas
 *       y paginado.</li>
 * </ul>
 */
public interface MedicamentoService {
    /**
     * Crea un nuevo medicamento a partir de un {@link MedicamentoRequest}.
     *
     * @param req datos de entrada del medicamento; no {@code null}
     * @return DTO con la información del medicamento creado
     * @throws IllegalArgumentException si el request es inválido (p. ej., fechas inconsistentes)
    # * @throws javax.persistence.EntityNotFoundException si el laboratorio indicado no existe
     */
    MedicamentoResponse crear(MedicamentoRequest req);

    /**
     * Actualiza un medicamento existente.
     *
     * @param id  identificador del medicamento a actualizar; no {@code null}
     * @param req datos a aplicar; no {@code null}
     * @return DTO con la información del medicamento actualizado
     #* @throws javax.persistence.EntityNotFoundException si el medicamento (o laboratorio) no existe
     * @throws IllegalArgumentException si el request es inválido
     */
    MedicamentoResponse actualizar(Long id, MedicamentoRequest req);

    /**
     * Elimina un medicamento.
     * <p>
     * La implementación puede optar por eliminación lógica (marcar inactivo) o física,
     * según la política del sistema.
     * </p>
     *
     * @param id identificador del medicamento; no {@code null}
     #* @throws javax.persistence.EntityNotFoundException si el medicamento no existe
     * @throws IllegalStateException si la política impide eliminar (p. ej., referencias activas)
     */
    void eliminar(Long id);

    /**
     * Obtiene un medicamento por su identificador.
     *
     * @param id identificador del medicamento; no {@code null}
     * @return DTO del medicamento
     #* @throws javax.persistence.EntityNotFoundException si no existe
     */
    MedicamentoResponse obtener(Long id);

    /**
     * Lista medicamentos con filtro opcional por nombre (case-insensitive) y paginación.
     *
     * @param nombre   texto a buscar en el nombre (opcional; {@code null} o vacío = sin filtro)
     * @param pageable configuración de paginación y ordenamiento
     * @return página de medicamentos que cumplen el filtro
     */
    Page<MedicamentoResponse> listar(String nombre, Pageable pageable);
    /**
     * Genera una cotización para un medicamento y una cantidad solicitada.
     * <p>
     * No modifica el inventario; únicamente calcula precios y disponibilidad.
     * </p>
     *
     * @param medicamentoId identificador del medicamento; no {@code null}
     * @param cantidad      unidades solicitadas; {@code ≥ 0}
     * @return DTO con precio unitario, total, stock disponible y bandera de posibilidad de venta
     #* @throws javax.persistence.EntityNotFoundException si el medicamento no existe
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    CotizacionResponse cotizar(Long medicamentoId, long cantidad);
    /**
     * Descuenta unidades de stock para un medicamento.
     * <p>
     * Debe validar disponibilidad suficiente y evitar valores negativos.
     * </p>
     *
     * @param medicamentoId identificador del medicamento; no {@code null}
     * @param cantidad      unidades a descontar; {@code > 0}
     #* @throws javax.persistence.EntityNotFoundException si el medicamento no existe
     * @throws IllegalArgumentException si {@code cantidad ≤ 0}
     * @throws IllegalStateException si no hay stock suficiente
     */
    void descontarStock(Long medicamentoId, long cantidad);
}
