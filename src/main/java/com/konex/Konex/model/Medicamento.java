package com.konex.Konex.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad JPA que representa un medicamento gestionado en el inventario.
 * <p>
 * Se mapea a la tabla <code>MEDICAMENTO</code> y contiene información como
 * nombre, laboratorio fabricante, fechas de fabricación y vencimiento, stock,
 * precio unitario y estado (activo/inactivo).
 * </p>
 * <p><b>Notas de mapeo:</b></p>
 * <ul>
 *   <li><strong>ID_MEDICAMENTO</strong>: clave primaria autogenerada.</li>
 *   <li>Relación muchos-a-uno con {@link Laboratorio} (columna <strong>ID_LABORATORIO</strong>, carga perezosa).</li>
 *   <li>Importes monetarios con {@link BigDecimal} (precisión 14, escala 2).</li>
 *   <li>Campo <code>ACTIVO</code> modelado como entero: 1 = activo, 0 = inactivo.</li>
 * </ul>
 *
 * @see Laboratorio
 */
@Entity
@Table(name = "MEDICAMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicamento {

    /**
     * Identificador único del medicamento (PK).
     * <p>Columna {@code ID_MEDICAMENTO}. Se genera con {@link GenerationType#IDENTITY}.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICAMENTO", nullable = false)
    private Long id;


    /**
     * Nombre comercial o genérico del medicamento.
     * <p>Columna {@code NOMBRE}, no nula, longitud máxima 160.</p>
     */

    @Column(name = "NOMBRE", nullable = false, length = 160)
    private String nombre;

    /**
     * Laboratorio fabricante del medicamento.
     * <p>
     * Relación muchos-a-uno, carga perezosa ({@link FetchType#LAZY}).
     * Mapeada por la columna {@code ID_LABORATORIO} (no nula),
     * con clave foránea {@code FK_MED_LAB}.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_LABORATORIO",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_MED_LAB"))
    private Laboratorio laboratorio;

    /**
     * Fecha de fabricación del lote del medicamento.
     * <p>Columna {@code FECHA_FABRICACION}, no nula.</p>
     * <p><i>Sugerencia de validación:</i> debería ser anterior a {@link #fechaVencimiento}.</p>
     */
    @Column(name = "FECHA_FABRICACION", nullable = false)
    private LocalDate fechaFabricacion;
    /**
     * Fecha de vencimiento del medicamento.
     * <p>Columna {@code FECHA_VENCIMIENTO}, no nula.</p>
     */
    @Column(name = "FECHA_VENCIMIENTO", nullable = false)
    private LocalDate fechaVencimiento;

    /**
     * Cantidad disponible en inventario (stock).
     * <p>Columna {@code CANTIDAD_STOCK}, no nula.</p>
     * <p><i>Sugerencia de validación:</i> valor no negativo.</p>
     */
    @Column(name = "CANTIDAD_STOCK", nullable = false)
    private Long cantidadStock;
    /**
     * Precio unitario vigente del medicamento.
     * <p>
     * Columna {@code VALOR_UNITARIO}, no nula, con precisión 14 y escala 2.
     * </p>
     */
    @Column(name = "VALOR_UNITARIO", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorUnitario;
    /**
     * Indicador de estado del medicamento.
     * <p>
     * Columna {@code ACTIVO}, no nula. Convención: 1 = activo, 0 = inactivo.
     * Inicializa por defecto en {@code 1}.
     * </p>
     */
    @Builder.Default
    @Column(name = "ACTIVO", nullable = false)
    private Integer activo = 1;
}
