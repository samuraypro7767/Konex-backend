package com.konex.Konex.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VENTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VENTA", nullable = false)
    private Long id;

    @Column(name = "FECHA_HORA", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "VALOR_TOTAL", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorTotal;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (fechaHora == null) fechaHora = LocalDateTime.now();
        if (valorTotal == null) valorTotal = BigDecimal.ZERO;
    }

    public void addDetalle(DetalleVenta detalle) {
        detalle.setVenta(this);
        this.detalles.add(detalle);
    }
}
