package com.konex.Konex.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Representa un rango de fechas/horas de forma inmutable.
 * <p>
 * Este record modela un intervalo temporal usando dos instantes:
 * {@code desde} y {@code hasta}. La interpretación habitual en consultas
 * de base de datos es <b>inclusivo</b> en ambos extremos, salvo que se indique lo contrario.
 * </p>
 *
 * <p><b>Convenciones:</b></p>
 * <ul>
 *   <li>Los campos están en hora local (sin zona horaria). Si tu aplicación es multi–zona,
 *       considera usar {@code ZonedDateTime} o normalizar a UTC.</li>
 *   <li>Se recomienda garantizar que {@code hasta} no sea anterior a {@code desde}
 *       desde la capa de servicio/validación.</li>
 * </ul>
 */
public record DateRange(LocalDateTime desde, LocalDateTime hasta) {
    /**
     * Crea un {@link DateRange} a partir de dos fechas de calendario,
     * expandiéndolas al inicio y fin del día respectivamente.
     * <p>
     * Transformaciones aplicadas:
     * </p>
     * <ul>
     *   <li>{@code desde} ⟶ {@code desde.atStartOfDay()} → 00:00:00.000000000</li>
     *   <li>{@code hasta} ⟶ {@code hasta.atTime(23, 59, 59, 999_999_999)} → 23:59:59.999999999</li>
     * </ul>
     * Por lo tanto, el intervalo resultante cubre completamente ambos días
     * (interpretación inclusiva).
     *
     * @param desde fecha inicial (inclusive), no {@code null}
     * @param hasta fecha final (inclusive), no {@code null} y típicamente ≥ {@code desde}
     * @return rango de fecha/hora que abarca ambos días completos
     * @throws NullPointerException si alguno de los parámetros es {@code null}
     * @implNote Alternativa equivalente: {@code hasta.plusDays(1).atStartOfDay().minusNanos(1)}.
     */
    public static DateRange ofLocalDates(LocalDate desde, LocalDate hasta) {
        // desde = inicio del día (00:00:00)
        LocalDateTime start = desde.atStartOfDay();
        // hasta = final del día (23:59:59.999)
        LocalDateTime end = hasta.atTime(23, 59, 59, 999_999_999);

        return new DateRange(start, end);
    }
}
