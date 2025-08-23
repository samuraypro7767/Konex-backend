package com.konex.Konex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Entity
@Table(name = "DETALLE_VENTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_VENTA",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_DV_VENTA"))
    @JsonIgnore
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_MEDICAMENTO",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_DV_MEDICAMENTO"))
    private Medicamento medicamento;

    @Column(name = "CANTIDAD", nullable = false)
    private Long cantidad;

    @Column(name = "VALOR_UNITARIO", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "VALOR_LINEA", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorLinea;
}
