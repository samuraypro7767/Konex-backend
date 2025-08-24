package com.konex.Konex.repository;

import com.konex.Konex.model.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repositorio Spring Data JPA para la entidad {@link Laboratorio}.
 * <p>
 * Proporciona operaciones CRUD, paginación y ordenación heredadas de {@link JpaRepository}.
 * Puede ampliarse con consultas derivadas por nombre de método o con {@code @Query}
 * para soportar validaciones (p.ej., NIT único) y búsquedas por nombre.
 * </p>
 *
 * <p><b>Sugerencias:</b></p>
 * <ul>
 *   <li>Aplicar índice/único en la columna <code>NIT</code> a nivel de BD.</li>
 *   <li>Usar métodos derivados como {@code existsByNit} para validaciones rápidas.</li>
 * </ul>
 *
 * <p><b>Ejemplos de extensiones futuras:</b></p>
 * <pre>{@code
 * // Buscar por NIT exacto
 * Optional<Laboratorio> findByNit(String nit);
 *
 * // Verificar existencia por NIT (útil para validaciones)
 * boolean existsByNit(String nit);
 *
 * // Búsqueda por nombre con coincidencia parcial e insensible a mayúsculas
 * List<Laboratorio> findByNombreContainingIgnoreCase(String nombre);
 * }</pre>
 */
public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {
    // Aquí se agrega búsquedas por nombre/NIT si las necesitas más adelante.
}
