package com.konex.Konex.repository;

import com.konex.Konex.model.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {
    // Aquí se agrega búsquedas por nombre/NIT si las necesitas más adelante.
}
