package com.konex.Konex.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DateRange(LocalDateTime desde, LocalDateTime hasta) {

    public static DateRange ofLocalDates(LocalDate desde, LocalDate hasta) {
        // desde = inicio del día (00:00:00)
        LocalDateTime start = desde.atStartOfDay();
        // hasta = final del día (23:59:59.999)
        LocalDateTime end = hasta.atTime(23, 59, 59, 999_999_999);

        return new DateRange(start, end);
    }
}
