package com.konex.Konex.repository;

import com.konex.Konex.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    /**
     * Ventas entre fechas (inclusive en inicio, exclusivo en fin si lo manejas así).
     * Úsalo con rangos construidos en el servicio.
     */
    List<Venta> findByFechaHoraBetween(LocalDateTime desde, LocalDateTime hasta);
}
