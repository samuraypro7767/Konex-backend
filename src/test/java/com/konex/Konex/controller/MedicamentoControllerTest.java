package com.konex.Konex.controller;

import com.konex.Konex.dto.CotizacionResponse;
import com.konex.Konex.dto.MedicamentoRequest;
import com.konex.Konex.dto.MedicamentoResponse;
import com.konex.Konex.service.MedicamentoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicamentoController.class)
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=none"
})
class MedicamentoControllerTest {

    @TestConfiguration
    static class Config {
        @Bean
        public MedicamentoService medicamentoService() {
            return Mockito.mock(MedicamentoService.class);
        }
    }

    @Autowired
    MockMvc mvc;

    @Autowired
    MedicamentoService service;

    @Test
    void crear_ok() throws Exception {
        MedicamentoResponse resp = MedicamentoResponse.builder()
                .id(1L).nombre("Ibu").laboratorioId(1L).laboratorioNombre("Acme")
                .fechaFabricacion(LocalDate.of(2024,1,1))
                .fechaVencimiento(LocalDate.of(2026,1,1))
                .cantidadStock(100L)
                .valorUnitario(new BigDecimal("2500"))
                .build();
        Mockito.when(service.crear(any(MedicamentoRequest.class))).thenReturn(resp);

        String body = """
            {
              "nombre":"Ibu",
              "laboratorioId":1,
              "fechaFabricacion":"2024-01-01",
              "fechaVencimiento":"2026-01-01",
              "cantidadStock":100,
              "valorUnitario":2500.00
            }
            """;

        mvc.perform(post("/api/medicamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void listar_ok() throws Exception {
        var page = new PageImpl<>(List.of(
                MedicamentoResponse.builder().id(1L).nombre("Ibu").build()
        ), PageRequest.of(0,10), 1);

        Mockito.when(service.listar(anyString(), any())).thenReturn(page);

        mvc.perform(get("/api/medicamentos?page=0&size=10&nombre=ibu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    void cotizar_ok() throws Exception {
        CotizacionResponse c = CotizacionResponse.builder()
                .medicamentoId(1L)
                .medicamentoNombre("Ibu")
                .cantidadSolicitada(3L)
                .stockDisponible(10L)
                .valorUnitario(new BigDecimal("2000"))
                .valorTotal(new BigDecimal("6000"))
                .puedeVender(true)
                .build();

        Mockito.when(service.cotizar(1L, 3L)).thenReturn(c);

        mvc.perform(get("/api/medicamentos/1/cotizar?cantidad=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorTotal").value(6000));
    }
}
