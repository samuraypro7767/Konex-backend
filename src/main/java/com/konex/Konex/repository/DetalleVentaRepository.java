package com.konex.Konex.repository;

import com.konex.Konex.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    // Métodos adicionales si luego necesitas reportes por medicamento, etc.
}
