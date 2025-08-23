package com.konex.Konex.controller;

import com.konex.Konex.dto.VentaCreateRequest;
import com.konex.Konex.dto.VentaItemResponse;
import com.konex.Konex.dto.VentaResponse;
import com.konex.Konex.service.VentaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=none"
})
class VentaControllerTest {

    @TestConfiguration
    static class Config {
        @Bean
        public VentaService ventaService() {
            return Mockito.mock(VentaService.class);
        }
    }

    @Autowired
    MockMvc mvc;

    @Autowired
    VentaService service;

    @Test
    void crear_ok() throws Exception {
        VentaResponse resp = VentaResponse.builder()
                .id(10L)
                .fechaHora(LocalDateTime.now())
                .valorTotal(new BigDecimal("7500"))
                .items(List.of(
                        VentaItemResponse.builder()
                                .medicamentoId(1L)
                                .medicamentoNombre("Ibu")
                                .cantidad(3L)
                                .valorUnitario(new BigDecimal("2500"))
                                .valorLinea(new BigDecimal("7500"))
                                .build()
                ))
                .build();

        Mockito.when(service.crearVenta(any(VentaCreateRequest.class))).thenReturn(resp);

        String body = """
            { "medicamentoId": 1, "cantidad": 3 }
            """;

        mvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.valorTotal").value(7500));
    }

    @Test
    void listarPorRango_ok() throws Exception {
        Mockito.when(service.listarPorRango(any(), any())).thenReturn(List.of());
        mvc.perform(get("/api/ventas?desde=2025-08-01&hasta=2025-08-31"))
                .andExpect(status().isOk());
    }
}
