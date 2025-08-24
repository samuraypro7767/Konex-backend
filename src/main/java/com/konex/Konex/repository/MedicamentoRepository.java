package com.konex.Konex.repository;

import com.konex.Konex.model.Medicamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    /**
     * Lista paginada con filtro por nombre (case-insensitive).
     * Si 'nombre' es null, trae todos.
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
