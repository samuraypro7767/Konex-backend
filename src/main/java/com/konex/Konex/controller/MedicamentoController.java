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

@RestController
@RequestMapping("/api/medicamentos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService service;

    // Crear
    @PostMapping
    public MedicamentoResponse crear(@Valid @RequestBody MedicamentoRequest req) {
        return service.crear(req);
    }

    // Actualizar
    @PutMapping("/{id}")
    public MedicamentoResponse actualizar(@PathVariable Long id,
                                          @Valid @RequestBody MedicamentoRequest req) {
        return service.actualizar(id, req);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    // Obtener por id
    @GetMapping("/{id}")
    public MedicamentoResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    // Listar paginado + filtro por nombre
    @GetMapping
    public Page<MedicamentoResponse> listar(@RequestParam(required = false) String nombre,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return service.listar(nombre, PageRequest.of(page, size));
    }

    // Cotizar para el modal (valor total y verificación de stock)
    @GetMapping("/{id}/cotizar")
    public CotizacionResponse cotizar(@PathVariable Long id,
                                      @RequestParam long cantidad) {
        return service.cotizar(id, cantidad);
    }

    // Descontar stock (usado internamente al confirmar venta)
    @PatchMapping("/{id}/descontar")
    public void descontar(@PathVariable Long id,
                          @RequestParam long cantidad) {
        service.descontarStock(id, cantidad);
    }
}
