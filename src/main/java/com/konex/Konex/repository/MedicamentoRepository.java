package com.konex.Konex.repository;

import com.konex.Konex.model.Medicamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
/**
 * Repositorio Spring Data JPA para la entidad {@link Medicamento}.
 * <p>
 * Expone operaciones CRUD y una consulta paginada con filtrado por nombre
 * para medicamentos activos. Útil para listados en UI con búsqueda y paginación.
 * </p>
 *
 * <p><b>Rendimiento (sugerencias):</b></p>
 * <ul>
 *   <li>Crear índice (o índice por función) sobre <code>NOMBRE</code> si se usa filtrado
 *       case-insensitive de forma frecuente (por ejemplo, <code>UPPER(NOMBRE)</code> en Oracle/PostgreSQL).</li>
 *   <li>Crear índice sobre <code>ACTIVO</code> si la mayoría de consultas filtran por este campo.</li>
 * </ul>
 */
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {


    /**
     * Obtiene una página de medicamentos activos, filtrando por nombre de forma
     * insensible a mayúsculas/minúsculas cuando se provee un texto de búsqueda.
     * <p>
     * Si {@code nombre} es {@code null} o vacío, trae todos los activos.
     * </p>
     *
     * @param nombre   texto a buscar dentro del nombre del medicamento (opcional).
     * @param pageable información de paginación y ordenamiento.
     * @return página con los medicamentos que cumplen el filtro.
     */
    @Query("""
           SELECT m
           FROM Medicamento m
           WHERE m.activo = 1
             AND (:nombre IS NULL OR :nombre = '' OR
                  UPPER(m.nombre) LIKE CONCAT('%', UPPER(:nombre), '%'))
           """)
    Page<Medicamento> buscar(@Param("nombre") String nombre, Pageable pageable);
}
