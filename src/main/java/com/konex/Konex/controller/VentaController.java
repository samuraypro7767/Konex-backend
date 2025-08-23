package com.konex.Konex.controller;

import com.konex.Konex.dto.VentaCreateRequest;
import com.konex.Konex.dto.VentaResponse;
import com.konex.Konex.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService service;

    // Confirmar venta (desde el modal)
    @PostMapping
    public VentaResponse crear(@Valid @RequestBody VentaCreateRequest req) {
        return service.crearVenta(req);
    }

    // Obtener venta por id
    @GetMapping("/{id}")
    public VentaResponse obtener(@PathVariable Long id) {
        return service.obtenerVenta(id);
    }

    // Listar ventas por rango de fechas (YYYY-MM-DD)
    @GetMapping
    public List<VentaResponse> listarPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return service.listarPorRango(desde, hasta);
    }
}
