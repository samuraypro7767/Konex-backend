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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void listarPorRango_paginado_ok() throws Exception {
        // Página 2 (index=1), tamaño 5, total 12
        PageRequest pr = PageRequest.of(1, 5);
        Page<VentaResponse> page = new PageImpl<>(
                List.of(
                        VentaResponse.builder().id(1L).valorTotal(new BigDecimal("1000")).build(),
                        VentaResponse.builder().id(2L).valorTotal(new BigDecimal("2000")).build()
                ),
                pr,
                12
        );

        // Stub con TRES argumentos: LocalDate, LocalDate y Pageable (de Spring)
        Mockito.when(service.listarPorRango(
                any(LocalDate.class),
                any(LocalDate.class),
                any(org.springframework.data.domain.Pageable.class)
        )).thenReturn(page);

        mvc.perform(get("/api/ventas")
                        .param("desde", "2025-08-01")
                        .param("hasta", "2025-08-31")
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalElements").value(12));

        // Verify: usa matchers para TODOS los argumentos (evita mezclar crudos con matchers)
        Mockito.verify(service).listarPorRango(
                eq(LocalDate.parse("2025-08-01")),
                eq(LocalDate.parse("2025-08-31")),
                any(org.springframework.data.domain.Pageable.class)
        );
    }

    @Test
    void listarTodas_ok() throws Exception {
        VentaResponse r1 = VentaResponse.builder().id(1L).valorTotal(new BigDecimal("1000")).build();
        VentaResponse r2 = VentaResponse.builder().id(2L).valorTotal(new BigDecimal("2000")).build();

        Mockito.when(service.listarTodas()).thenReturn(List.of(r1, r2));

        mvc.perform(get("/api/ventas/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
}
