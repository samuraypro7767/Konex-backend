package com.konex.Konex.service;
import com.konex.Konex.dto.VentaCreateRequest;
import com.konex.Konex.dto.VentaResponse;
import com.konex.Konex.exception.BusinessException;
import com.konex.Konex.exception.NotFoundException;
import com.konex.Konex.model.Medicamento;
import com.konex.Konex.model.Venta;
import com.konex.Konex.repository.MedicamentoRepository;
import com.konex.Konex.repository.VentaRepository;
import com.konex.Konex.service.impl.VentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        // Arrange
        Medicamento med = Medicamento.builder()
                .id(1L).nombre("Ibu")
                .cantidadStock(10L)
                .valorUnitario(new BigDecimal("1000"))
                .build();

        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(med));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> {
            Venta v = inv.getArgument(0);
            v.setId(100L);
            return v;
        });

        VentaCreateRequest req = new VentaCreateRequest();
        req.setMedicamentoId(1L);
        req.setCantidad(3L);

        // Act
        VentaResponse resp = service.crearVenta(req);

        // Assert
        assertThat(resp.getId()).isEqualTo(100L);
        assertThat(resp.getValorTotal()).isEqualByComparingTo("3000");
        assertThat(resp.getItems()).hasSize(1);
        assertThat(med.getCantidadStock()).isEqualTo(7L); // se descontó

        verify(ventaRepository, times(1)).save(any(Venta.class));
        verify(medicamentoRepository, times(1)).save(med);
    }

    @Test
    void crearVenta_cantidadInvalida() {
        VentaCreateRequest req = new VentaCreateRequest();
        req.setMedicamentoId(1L);
        req.setCantidad(0L); // inválida

        assertThatThrownBy(() -> service.crearVenta(req))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("mayor que cero");
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
    void listarPorRango_paginado_ok() {
        // Arrange
        Venta v = new Venta();
        v.setId(1L);
        v.setFechaHora(LocalDateTime.now());
        v.setValorTotal(new BigDecimal("123"));

        Pageable pageable = PageRequest.of(0, 2, Sort.by("fechaHora").descending());
        Page<Venta> page = new PageImpl<>(List.of(v), pageable, 1);

        when(ventaRepository.findByFechaHoraBetween(any(), any(), any(Pageable.class)))
                .thenReturn(page);

        // Act
        var result = service.listarPorRango(LocalDate.now().minusDays(1), LocalDate.now(), pageable);

        // Assert
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
        verify(ventaRepository).findByFechaHoraBetween(any(), any(), eq(pageable));
    }

    @Test
    void listarTodas_paginado_ok_service() {
        Venta v1 = new Venta(); v1.setId(1L);
        Venta v2 = new Venta(); v2.setId(2L);

        var pageable = PageRequest.of(0, 2);
        Page<Venta> page = new PageImpl<>(List.of(v1, v2), pageable, 10);

        when(ventaRepository.findAll(any(Pageable.class))).thenReturn(page); // ← clave

        Page<VentaResponse> result = service.listarTodas(pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
        verify(ventaRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void obtenerVenta_ok() {
        Venta v = new Venta();
        v.setId(5L);
        v.setFechaHora(LocalDateTime.now());
        v.setValorTotal(new BigDecimal("555"));

        when(ventaRepository.findById(5L)).thenReturn(Optional.of(v));

        var resp = service.obtenerVenta(5L);

        assertThat(resp.getId()).isEqualTo(5L);
        assertThat(resp.getValorTotal()).isEqualByComparingTo("555");
        verify(ventaRepository).findById(5L);
    }
}
