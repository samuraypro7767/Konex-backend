package com.konex.Konex.service;

import com.konex.Konex.dto.VentaCreateRequest;
import com.konex.Konex.dto.VentaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface VentaService {

    VentaResponse crearVenta(VentaCreateRequest req);

    VentaResponse obtenerVenta(Long id);

    Page<VentaResponse> listarPorRango(LocalDate desde, LocalDate hasta, Pageable pageable);

    List<VentaResponse> listarTodas();

}
