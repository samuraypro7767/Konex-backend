package com.konex.Konex.controller;
import org.springframework.data.domain.*;
import com.konex.Konex.dto.VentaCreateRequest;
import com.konex.Konex.dto.VentaResponse;
import com.konex.Konex.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
/**
 * Controlador REST para gestionar operaciones relacionadas con ventas.
 * <p>
 * Expone endpoints para crear ventas, obtener por id, listar todas
 * y listar por rango de fechas con paginación.
 * </p>
 *
 * <p><b>Notas:</b></p>
 * <ul>
 *   <li>Habilita CORS para cualquier origen ({@code @CrossOrigin(origins="*")}).</li>
 *   <li>Las validaciones de entrada usan Jakarta Bean Validation ({@code @Valid}).</li>
 *   <li>Los rangos de fechas se interpretan por día calendario (ver servicio).</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService service;

    /**
     * Crea/confirmar una venta a partir de un medicamento y su cantidad.
     *
     * @param req payload con {@code medicamentoId} y {@code cantidad}; validado con {@link Valid}
     * @return DTO de la venta creada
     */
    @PostMapping
    public VentaResponse crear(@Valid @RequestBody VentaCreateRequest req) {
        return service.crearVenta(req);
    }
    /**
     * Lista todas las ventas sin paginación.
     *
     * @return lista completa de ventas
     */
    @GetMapping("/all")
    public Page<VentaResponse> listarTodas(
            @PageableDefault(size = 20, sort = "fecha", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return service.listarTodas(pageable);
    }


    /**
     * Obtiene una venta por su identificador.
     *
     * @param id identificador de la venta
     * @return DTO de la venta encontrada
     */
    @GetMapping("/{id}")
    public VentaResponse obtener(@PathVariable Long id) {
        return service.obtenerVenta(id);
    }
    /**
     * Lista ventas dentro de un rango de fechas (por día calendario), de forma paginada.
     * <p>
     * Los parámetros {@code desde} y {@code hasta} se reciben en formato ISO (yyyy-MM-dd).
     * La implementación del servicio expande el rango a:
     * {@code [desde 00:00:00, hasta 23:59:59.999999999]}.
     * </p>
     *
     * @param desde fecha inicial (inclusive), en formato ISO (yyyy-MM-dd)
     * @param hasta fecha final (inclusive), en formato ISO (yyyy-MM-dd)
     * @param page  número de página (0-indexed); por defecto 0
     * @param size  tamaño de página; por defecto 10
     * @return página de ventas dentro del rango solicitado
     */
    @GetMapping
    public Page<VentaResponse> listarPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.listarPorRango(desde, hasta, PageRequest.of(page, size));
    }

}
