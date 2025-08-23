package com.konex.Konex.service;

import com.konex.Konex.dto.VentaCreateRequest;
import com.konex.Konex.dto.VentaResponse;
import com.konex.Konex.exception.BusinessException;
import com.konex.Konex.exception.NotFoundException;
import com.konex.Konex.model.DetalleVenta;
import com.konex.Konex.model.Medicamento;
import com.konex.Konex.model.Venta;
import com.konex.Konex.repository.MedicamentoRepository;
import com.konex.Konex.repository.VentaRepository;
import com.konex.Konex.service.impl.VentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class VentaServiceImplTest {

    VentaRepository ventaRepository;
    MedicamentoRepository medicamentoRepository;
    VentaServiceImpl service;

    @BeforeEach
    void setUp() {
        ventaRepository = mock(VentaRepository.class);
        medicamentoRepository = mock(MedicamentoRepository.class);
        service = new VentaServiceImpl(ventaRepository, medicamentoRepository);
    }

    @Test
    void crearVenta_ok() {
        // arrange
        Medicamento med = Medicamento.builder()
                .id(1L).nombre("Ibu")
                .cantidadStock(10L)
                .valorUnitario(new BigDecimal("1000"))
                .build();
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(med));

        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> {
            Venta v = inv.getArgument(0);
            v.setId(100L);
            // simular que JPA setea backrefs correctamente (ya viene armado en service)
            return v;
        });

        VentaCreateRequest req = new VentaCreateRequest();
        req.setMedicamentoId(1L);
        req.setCantidad(3L);

        // act
        VentaResponse resp = service.crearVenta(req);

        // assert
        assertThat(resp.getId()).isEqualTo(100L);
        assertThat(resp.getValorTotal()).isEqualByComparingTo("3000");
        assertThat(resp.getItems()).hasSize(1);
        assertThat(med.getCantidadStock()).isEqualTo(7L); // se descontó

        verify(ventaRepository, times(1)).save(any(Venta.class));
        verify(medicamentoRepository, times(1)).save(med);
    }

    @Test
    void crearVenta_stockInsuficiente() {
        Medicamento med = Medicamento.builder()
                .id(1L).nombre("Ibu")
                .cantidadStock(2L)
                .valorUnitario(new BigDecimal("1000"))
                .build();
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(med));

        VentaCreateRequest req = new VentaCreateRequest();
        req.setMedicamentoId(1L);
        req.setCantidad(3L);

        assertThatThrownBy(() -> service.crearVenta(req))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Stock insuficiente");
    }

    @Test
    void crearVenta_medicamentoNoExiste() {
        when(medicamentoRepository.findById(99L)).thenReturn(Optional.empty());
        VentaCreateRequest req = new VentaCreateRequest();
        req.setMedicamentoId(99L);
        req.setCantidad(1L);

        assertThatThrownBy(() -> service.crearVenta(req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Medicamento");
    }

    @Test
    void listarPorRango_ok() {
        Venta v = new Venta();
        v.setId(1L);
        v.setFechaHora(LocalDateTime.now());
        v.setValorTotal(new BigDecimal("123"));

        when(ventaRepository.findByFechaHoraBetween(any(), any()))
                .thenReturn(List.of(v));

        List<VentaResponse> list = service.listarPorRango(LocalDate.now().minusDays(1), LocalDate.now());
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getId()).isEqualTo(1L);
    }
}
