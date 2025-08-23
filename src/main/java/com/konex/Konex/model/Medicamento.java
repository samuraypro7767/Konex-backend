package com.konex.Konex.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "MEDICAMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICAMENTO", nullable = false)
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 160)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_LABORATORIO",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_MED_LAB"))
    private Laboratorio laboratorio;

    @Column(name = "FECHA_FABRICACION", nullable = false)
    private LocalDate fechaFabricacion;

    @Column(name = "FECHA_VENCIMIENTO", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "CANTIDAD_STOCK", nullable = false)
    private Long cantidadStock;

    @Column(name = "VALOR_UNITARIO", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorUnitario;
}
