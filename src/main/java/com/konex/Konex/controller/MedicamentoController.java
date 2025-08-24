package com.konex.Konex.controller;

import com.konex.Konex.dto.CotizacionResponse;
import com.konex.Konex.dto.MedicamentoRequest;
import com.konex.Konex.dto.MedicamentoResponse;
import com.konex.Konex.service.MedicamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
/**
 * Controlador REST para gestionar operaciones sobre medicamentos.
 * <p>
 * Expone endpoints para crear, actualizar, inactivar (eliminación lógica),
 * obtener, listar de forma paginada, cotizar y descontar stock.
 * </p>
 *
 * <p><b>Notas:</b></p>
 * <ul>
 *   <li>Habilita CORS para cualquier origen ({@code @CrossOrigin(origins="*")}).</li>
 *   <li>Las validaciones de entrada usan Jakarta Bean Validation ({@code @Valid}).</li>
 *   <li>La eliminación se maneja de forma lógica (campo {@code activo = 0}).</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/medicamentos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService service;

    /**
     * Crea un nuevo medicamento.
     *
     * @param req payload con los datos del medicamento; validado con {@link Valid}
     * @return DTO del medicamento creado
     */
    @PostMapping
    public MedicamentoResponse crear(@Valid @RequestBody MedicamentoRequest req) {
        return service.crear(req);
    }


    /**
     * Actualiza un medicamento existente.
     *
     * @param id  identificador del medicamento a actualizar
     * @param req payload con los datos a modificar; validado con {@link Valid}
     * @return DTO del medicamento actualizado
     */
    @PutMapping("/{id}")
    public MedicamentoResponse actualizar(@PathVariable Long id,
                                          @Valid @RequestBody MedicamentoRequest req) {
        return service.actualizar(id, req);
    }
    /**
     * Elimina lógicamente un medicamento (marca {@code activo = 0}).
     *
     * @param id identificador del medicamento
     */
    @DeleteMapping("/{id}")
    // Obtener por id
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
    /**
     * Obtiene un medicamento por su identificador (solo si está activo).
     *
     * @param id identificador del medicamento
     * @return DTO del medicamento
     */
    @GetMapping("/{id}")
    public MedicamentoResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    /**
     * Inactiva un medicamento (equivalente a eliminación lógica).
     *
     * @param id identificador del medicamento
     */
    @PatchMapping("/{id}/inactivar")
    public void inactivar(@PathVariable Long id) { service.eliminar(id); }
    /**
     * Lista medicamentos activos con filtro opcional por nombre y paginación.
     *
     * @param nombre texto a buscar en el nombre (opcional; {@code null} o vacío = sin filtro)
     * @param page   número de página (0-indexed), por defecto 0
     * @param size   tamaño de página, por defecto 10
     * @return página de medicamentos que cumplen el filtro
     */    @GetMapping
    public Page<MedicamentoResponse> listar(@RequestParam(required = false) String nombre,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return service.listar(nombre, PageRequest.of(page, size));
    }


    /**
     * Cotiza un medicamento para una cantidad dada (no modifica inventario).
     *
     * @param id       identificador del medicamento
     * @param cantidad unidades solicitadas
     * @return DTO de cotización con precio unitario, total, stock y bandera de posibilidad de venta
     */    @GetMapping("/{id}/cotizar")
    public CotizacionResponse cotizar(@PathVariable Long id,
                                      @RequestParam long cantidad) {
        return service.cotizar(id, cantidad);
    }


    /**
     * Descuenta unidades del stock de un medicamento.
     *
     * @param id       identificador del medicamento
     * @param cantidad unidades a descontar
     */    @PatchMapping("/{id}/descontar")
    public void descontar(@PathVariable Long id,
                          @RequestParam long cantidad) {
        service.descontarStock(id, cantidad);
    }
}
