package com.konex.Konex.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "LABORATORIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Laboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LABORATORIO", nullable = false)
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 160)
    private String nombre;

    @Column(name = "NIT", nullable = false, length = 30)
    private String nit;
}
