package com.konex.Konex.service;

import com.konex.Konex.dto.CotizacionResponse;
import com.konex.Konex.dto.MedicamentoRequest;
import com.konex.Konex.dto.MedicamentoResponse;
import com.konex.Konex.exception.BusinessException;
import com.konex.Konex.exception.NotFoundException;
import com.konex.Konex.model.Laboratorio;
import com.konex.Konex.model.Medicamento;
import com.konex.Konex.repository.LaboratorioRepository;
import com.konex.Konex.repository.MedicamentoRepository;
import com.konex.Konex.service.impl.MedicamentoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicamentoServiceImplTest {

    MedicamentoRepository medicamentoRepository;
    LaboratorioRepository laboratorioRepository;
    MedicamentoServiceImpl service;

    @BeforeEach
    void setUp() {
        medicamentoRepository = mock(MedicamentoRepository.class);
        laboratorioRepository = mock(LaboratorioRepository.class);
        service = new MedicamentoServiceImpl(medicamentoRepository, laboratorioRepository);
    }

    @Test
    void crear_ok() {
        // arrange
        MedicamentoRequest req = new MedicamentoRequest();
        req.setNombre("Ibuprofeno 400mg");
        req.setLaboratorioId(1L);
        req.setFechaFabricacion(LocalDate.of(2024,1,1));
        req.setFechaVencimiento(LocalDate.of(2026,1,1));
        req.setCantidadStock(100L);
        req.setValorUnitario(new BigDecimal("2500"));

        Laboratorio lab = Laboratorio.builder().id(1L).nombre("Acme").nit("123").build();
        when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(lab));

        when(medicamentoRepository.save(any(Medicamento.class)))
                .thenAnswer(inv -> {
                    Medicamento m = inv.getArgument(0);
                    m.setId(10L);
                    return m;
                });

        // act
        MedicamentoResponse res = service.crear(req);

        // assert
        assertThat(res.getId()).isEqualTo(10L);
        assertThat(res.getNombre()).isEqualTo("Ibuprofeno 400mg");
        assertThat(res.getLaboratorioNombre()).isEqualTo("Acme");

        // verificar lo que guardamos
        ArgumentCaptor<Medicamento> captor = ArgumentCaptor.forClass(Medicamento.class);
        verify(medicamentoRepository).save(captor.capture());
        assertThat(captor.getValue().getCantidadStock()).isEqualTo(100L);
    }

    @Test
    void crear_falla_laboratorioNoExiste() {
        MedicamentoRequest req = new MedicamentoRequest();
        req.setLaboratorioId(99L);
        when(laboratorioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.crear(req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Laboratorio");
    }

    @Test
    void cotizar_ok() {
        Medicamento med = Medicamento.builder()
                .id(5L).nombre("Ibu")
                .cantidadStock(10L)
                .valorUnitario(new BigDecimal("2000"))
                .build();
        when(medicamentoRepository.findById(5L)).thenReturn(Optional.of(med));

        CotizacionResponse c = service.cotizar(5L, 3);

        assertThat(c.isPuedeVender()).isTrue();
        assertThat(c.getValorTotal()).isEqualByComparingTo("6000");
    }

    @Test
    void descontarStock_insuficiente() {
        Medicamento med = Medicamento.builder()
                .id(5L).nombre("Ibu")
                .cantidadStock(2L)
                .valorUnitario(new BigDecimal("2000"))
                .build();
        when(medicamentoRepository.findById(5L)).thenReturn(Optional.of(med));

        assertThatThrownBy(() -> service.descontarStock(5L, 3))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Stock insuficiente");
    }
}
