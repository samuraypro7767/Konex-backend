package com.konex.Konex.service;

import com.konex.Konex.dto.CotizacionResponse;
import com.konex.Konex.dto.MedicamentoRequest;
import com.konex.Konex.dto.MedicamentoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicamentoService {

    MedicamentoResponse crear(MedicamentoRequest req);

    MedicamentoResponse actualizar(Long id, MedicamentoRequest req);

    void eliminar(Long id);

    MedicamentoResponse obtener(Long id);

    Page<MedicamentoResponse> listar(String nombre, Pageable pageable);

    CotizacionResponse cotizar(Long medicamentoId, long cantidad);

    void descontarStock(Long medicamentoId, long cantidad);
}
