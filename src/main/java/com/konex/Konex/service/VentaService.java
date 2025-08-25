package com.konex.Konex.service;

import com.konex.Konex.dto.VentaCreateRequest;
import com.konex.Konex.dto.VentaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrato de la capa de servicio para operaciones de ventas.
 * <p>
 * Define los casos de uso principales: crear una venta, obtener una venta por id,
 * listar ventas por rango de fechas (paginado) y listar todas las ventas.
 * </p>
 *
 * <p><b>Convenciones:</b></p>
 * <ul>
 *   <li>Los montos monetarios se devuelven en {@link VentaResponse} como suma de los detalles.</li>
 *   <li>Para el rango de fechas se recomienda interpretar:
 *       <code>[desde 00:00:00, hasta 23:59:59.999999999]</code> (inclusive en ambos días).</li>
 *   <li>Las validaciones de stock y existencia de entidades deben realizarse en la implementación.</li>
 * </ul>
 */
public interface VentaService {
    /**
     * Crea una venta a partir de una solicitud con medicamento y cantidad.
     * <p>
     * La implementación debe:
     * </p>
     * <ul>
     *   <li>Validar existencia del medicamento y el stock disponible.</li>
     *   <li>Descontar la cantidad del inventario.</li>
     *   <li>Calcular el valor total y persistir la venta con sus detalles.</li>
     * </ul>
     *
     * @param req datos de creación (medicamento y cantidad), no {@code null}
     * @return representación de la venta creada
     * @throws IllegalArgumentException si los datos son inválidos
     #* @throws javax.persistence.EntityNotFoundException si el medicamento no existe
     */
    VentaResponse crearVenta(VentaCreateRequest req);
    /**
     * Obtiene una venta por su identificador.
     *
     * @param id identificador de la venta, no {@code null}
     * @return la venta encontrada
     #* @throws javax.persistence.EntityNotFoundException si no existe la venta
     */
    VentaResponse obtenerVenta(Long id);
    /**
     * Lista ventas dentro de un rango de fechas (por día calendario), de forma paginada.
     * <p>
     * Convención recomendada: transformar internamente a un rango de tiempo
     * <code>[desde.atStartOfDay(), hasta.plusDays(1).atStartOfDay())</code>.
     * </p>
     *
     * @param desde    día inicial (inclusive), no {@code null}
     * @param hasta    día final (inclusive), no {@code null} y ≥ {@code desde}
     * @param pageable información de paginación y ordenamiento
     * @return página de ventas en el rango
     * @throws IllegalArgumentException si {@code hasta} es anterior a {@code desde}
     */
    Page<VentaResponse> listarPorRango(LocalDate desde, LocalDate hasta, Pageable pageable);
    /**
     * Lista todas las ventas  paginación.
     *
     * @return lista completa de ventas
     */
    Page<VentaResponse> listarTodas(Pageable pageable);
}
