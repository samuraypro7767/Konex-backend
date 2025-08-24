package com.konex.Konex.repository;

import com.konex.Konex.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repositorio Spring Data JPA para la entidad {@link DetalleVenta}.
 * <p>
 * Expone las operaciones CRUD básicas, así como paginación y ordenación,
 * heredadas de {@link JpaRepository}. Puede ampliarse con consultas derivadas
 * por nombre de método o con consultas JPQL nativas mediante {@code @Query}
 * para soportar reportes y agregaciones (por medicamento, por venta, por rango
 * de fechas, etc.).
 * </p>
 *
 * <p><b>Ejemplos de extensiones futuras:</b></p>
 * <pre>{@code
 * // Listar detalles por venta
 * List<DetalleVenta> findByVentaId(Long ventaId);
 *
 * // Listar detalles por medicamento
 * List<DetalleVenta> findByMedicamentoId(Long medicamentoId);
 *
 * // Total vendido por medicamento (ejemplo JPQL)
 * @Query("select coalesce(sum(d.valorLinea), 0) from DetalleVenta d where d.medicamento.id = :id")
 * BigDecimal totalVendidoPorMedicamento(@Param("id") Long medicamentoId);
 * }</pre>
 */
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    // Métodos adicionales si luego necesitas reportes por medicamento, etc.
}
