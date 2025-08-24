package com.konex.Konex.repository;

import com.konex.Konex.model.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Repositorio Spring Data JPA para la entidad {@link Venta}.
 * <p>
 * Proporciona operaciones CRUD y consultas por rango de fechas sobre el campo {@code fechaHora}.
 * </p>
 *
 * <p><b>Rendimiento (sugerencias):</b></p>
 * <ul>
 *   <li>Crear un índice sobre la columna <code>FECHA_HORA</code> para acelerar búsquedas por rango.</li>
 *   <li>Si las consultas por fecha son muy frecuentes junto con otro criterio (p. ej., por usuario/sucursal),
 *       considerar índices compuestos.</li>
 * </ul>
 *
 * <p><b>Nota sobre semántica de BETWEEN:</b> Los métodos <code>...Between</code> de Spring Data
 * generan una comparación inclusiva en ambos extremos (equivalente a SQL
 * <code>BETWEEN</code>). Si necesitas un intervalo semiabierto [desde, hasta),
 * usa en su lugar dos comparadores explícitos:
 * <pre>{@code
 * List<Venta> findByFechaHoraGreaterThanEqualAndFechaHoraLessThan(LocalDateTime desde, LocalDateTime hastaExclusivo);
 * Page<Venta> findByFechaHoraGreaterThanEqualAndFechaHoraLessThan(LocalDateTime desde, LocalDateTime hastaExclusivo, Pageable pageable);
 * }</pre>
 * </p>
 */
public interface VentaRepository extends JpaRepository<Venta, Long> {


    /**
     * Devuelve ventas cuyo {@code fechaHora} está entre {@code desde} y {@code hasta},
     * <b>incluyendo</b> ambos límites.
     *
     * @param desde límite inferior (inclusive)
     * @param hasta límite superior (inclusive)
     * @return lista de ventas en el rango
     */

    List<Venta> findByFechaHoraBetween(LocalDateTime desde, LocalDateTime hasta);
    /**
     * Versión paginada de {@link #findByFechaHoraBetween(LocalDateTime, LocalDateTime)}.
     *
     * @param desde    límite inferior (inclusive)
     * @param hasta    límite superior (inclusive)
     * @param pageable información de paginación y orden
     * @return página de ventas en el rango
     */
    Page<Venta> findByFechaHoraBetween(LocalDateTime desde, LocalDateTime hasta, Pageable pageable);
}