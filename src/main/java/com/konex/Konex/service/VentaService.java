package com.konex.Konex.service;

import com.konex.Konex.dto.VentaCreateRequest;
import com.konex.Konex.dto.VentaResponse;

import java.time.LocalDate;
import java.util.List;

public interface VentaService {

    VentaResponse crearVenta(VentaCreateRequest req);

    VentaResponse obtenerVenta(Long id);

    List<VentaResponse> listarPorRango(LocalDate desde, LocalDate hasta);

    List<VentaResponse> listarTodas();

}
