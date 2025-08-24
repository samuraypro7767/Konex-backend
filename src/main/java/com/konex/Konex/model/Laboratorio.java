package com.konex.Konex.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
/**
 * Entidad JPA que representa un laboratorio farmacéutico.
 * <p>
 * Se mapea a la tabla <code>LABORATORIO</code> y almacena datos básicos como
 * el nombre y el NIT del laboratorio.
 * </p>
 * <p><b>Notas de mapeo:</b></p>
 * <ul>
 *   <li><strong>ID_LABORATORIO</strong>: clave primaria autogenerada.</li>
 *   <li>El campo <code>NIT</code> suele ser único a nivel de negocio (se recomienda
 *       una restricción única en BD).</li>
 *   <li>Es entidad referenciada por {@link Medicamento} mediante la FK <code>ID_LABORATORIO</code>.</li>
 * </ul>
 *
 * @see Medicamento
 */
@Entity
@Table(name = "LABORATORIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Laboratorio {

    /**
     * Identificador único del laboratorio (PK).
     * <p>Columna {@code ID_LABORATORIO}. Se genera con {@link GenerationType#IDENTITY}.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LABORATORIO", nullable = false)
    private Long id;
    /**
     * Nombre del laboratorio.
     * <p>Columna {@code NOMBRE}, no nula, longitud máxima 160.</p>
     */
    @Column(name = "NOMBRE", nullable = false, length = 160)
    private String nombre;
    /**
     * NIT del laboratorio (identificación tributaria).
     * <p>
     * Columna {@code NIT}, no nula, longitud máxima 30.
     * <br><i>Sugerencia:</i> aplicar índice/único en BD para evitar duplicados.
     * </p>
     */
    @Column(name = "NIT", nullable = false, length = 30)
    private String nit;
}
