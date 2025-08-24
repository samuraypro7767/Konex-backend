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
/**
 * Entidad JPA que representa una venta realizada.
 * <p>
 * Se mapea a la tabla <code>VENTA</code> e incluye la fecha/hora de la operación,
 * el valor total y la lista de {@link DetalleVenta} asociados.
 * </p>
 *
 * <p><b>Notas de mapeo:</b></p>
 * <ul>
 *   <li><strong>ID_VENTA</strong>: clave primaria autogenerada.</li>
 *   <li>Relación uno-a-muchos con {@link DetalleVenta} mediante el atributo inverso {@code venta}.</li>
 *   <li>{@code cascade = ALL} y {@code orphanRemoval = true}: los detalles se persisten/actualizan junto
 *       con la venta y se eliminan cuando dejan de pertenecer a ella.</li>
 *   <li>Importes monetarios con {@link BigDecimal} (precisión 14, escala 2).</li>
 * </ul>
 *
 * <p><b>Sugerencias:</b></p>
 * <ul>
 *   <li>Si usas el patrón <em>builder</em> con Lombok y deseas un valor por defecto para {@link #detalles},
 *       considera anotar el campo con {@code @Builder.Default} para evitar listas nulas al construir.</li>
 *   <li>El cálculo de {@link #valorTotal} puede centralizarse en un método de negocio o en
 *       callbacks JPA (@PrePersist/@PreUpdate) para mantener integridad.</li>
 * </ul>
 *
 * @see DetalleVenta
 */
@Entity
@Table(name = "VENTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {
    /**
     * Identificador único de la venta (PK).
     * <p>Columna {@code ID_VENTA}. Se genera con {@link GenerationType#IDENTITY}.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VENTA", nullable = false)
    private Long id;
    /**
     * Fecha y hora en que se registra la venta.
     * <p>Columna {@code FECHA_HORA}, no nula.</p>
     */
    @Column(name = "FECHA_HORA", nullable = false)
    private LocalDateTime fechaHora;
    /**
     * Monto total de la venta.
     * <p>
     * Columna {@code VALOR_TOTAL}, no nula, con precisión 14 y escala 2.
     * Normalmente es la suma de {@code valorLinea} de todos los {@link DetalleVenta}.
     * </p>
     */
    @Column(name = "VALOR_TOTAL", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorTotal;
    /**
     * Detalles (líneas) que componen la venta.
     * <p>
     * Relación uno-a-muchos con {@link DetalleVenta}, mapeada por el atributo {@code venta}
     * en la entidad detalle. Se habilita cascada total y eliminación de huérfanos.
     * </p>
     */
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();
    /**
     * Callback de ciclo de vida JPA ejecutado antes de persistir.
     * <p>
     * Inicializa {@link #fechaHora} con el instante actual y {@link #valorTotal} en {@code 0}
     * cuando no han sido establecidos explícitamente.
     * </p>
     */
    @PrePersist
    public void prePersist() {
        if (fechaHora == null) fechaHora = LocalDateTime.now();
        if (valorTotal == null) valorTotal = BigDecimal.ZERO;
    }

    /**
     * Agrega un detalle a la venta asegurando la consistencia de la relación bidireccional.
     * <p>Establece la referencia inversa {@code detalle.setVenta(this)} y luego lo añade a la lista.</p>
     *
     * @param detalle detalle de venta a agregar; no debe ser {@code null}
     */
    public void addDetalle(DetalleVenta detalle) {
        detalle.setVenta(this);
        this.detalles.add(detalle);
    }
}
