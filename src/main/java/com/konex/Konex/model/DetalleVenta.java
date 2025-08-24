package com.konex.Konex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * Entidad JPA que representa una línea (detalle) dentro de una {@link Venta}.
 * <p>
 * Cada detalle referencia el {@link Medicamento} vendido, la cantidad, el valor unitario
 * y el valor total de la línea. Se mapea a la tabla <code>DETALLE_VENTA</code>.
 * </p>
 * <p><b>Notas de mapeo:</b></p>
 * <ul>
 *   <li><strong>ID_DETALLE</strong>: clave primaria autogenerada.</li>
 *   <li>Relación muchos-a-uno con {@link Venta} (columna <strong>ID_VENTA</strong>, carga perezosa).</li>
 *   <li>Relación muchos-a-uno con {@link Medicamento} (columna <strong>ID_MEDICAMENTO</strong>, carga perezosa).</li>
 *   <li>Los importes monetarios usan {@link BigDecimal} con precisión 14 y escala 2.</li>
 * </ul>
 *
 * @see Venta
 * @see Medicamento
 */

@Entity
@Table(name = "DETALLE_VENTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleVenta {


    /**
     * Identificador único del detalle (PK).
     * <p>Corresponde a la columna {@code ID_DETALLE} y se genera con {@link GenerationType#IDENTITY}.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE", nullable = false)
    private Long id;

    /**
     * Venta a la que pertenece este detalle.
     * <p>
     * Mapeada por la columna {@code ID_VENTA} (no nula) con clave foránea {@code FK_DV_VENTA}.
     * Se usa carga perezosa ({@link FetchType#LAZY}) y {@link JsonIgnore} para evitar
     * recursión/serialización circular en respuestas JSON.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_VENTA",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_DV_VENTA"))
    @JsonIgnore
    private Venta venta;

    /**
     * Medicamento vendido en esta línea.
     * <p>
     * Mapeado por la columna {@code ID_MEDICAMENTO} (no nula) con clave foránea {@code FK_DV_MEDICAMENTO}.
     * Carga perezosa ({@link FetchType#LAZY}).
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_MEDICAMENTO",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_DV_MEDICAMENTO"))
    private Medicamento medicamento;

    /**
     * Cantidad vendida del medicamento en esta línea.
     * <p>Columna {@code CANTIDAD}, no nula.</p>
     */
    @Column(name = "CANTIDAD", nullable = false)
    private Long cantidad;

    /**
     * Importe total de la línea.
     * <p>
     * Usualmente corresponde a <em>cantidad × valorUnitario</em>.
     * Columna {@code VALOR_LINEA}, no nula, con precisión 14 y escala 2.
     * </p>
     */
    @Column(name = "VALOR_UNITARIO", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "VALOR_LINEA", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorLinea;
}
