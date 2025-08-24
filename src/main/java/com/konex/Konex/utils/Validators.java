package com.konex.Konex.utils;

import com.konex.Konex.exception.BusinessException;

/**
 * Utilidad de validaciones de reglas de negocio.
 * <p>
 * Provee métodos estáticos para verificar condiciones y lanzar una
 * {@link BusinessException} con un mensaje descriptivo cuando la validación falla.
 * Útil para mantener el código de servicio/controladores más legible y coherente.
 * </p>
 *
 * <p><b>Convenciones:</b></p>
 * <ul>
 *   <li>Usa mensajes claros y orientados al usuario/negocio.</li>
 *   <li>Evita lógica pesada dentro de los métodos de validación; limítate a evaluar condiciones.</li>
 * </ul>
 */
public class Validators {
    /**
     * Verifica una condición booleana y, si no se cumple, lanza una
     * {@link BusinessException} con el mensaje proporcionado.
     *
     * @param condition condición que debe cumplirse ({@code true} = válido)
     * @param message   mensaje de error a incluir en la excepción si la condición es {@code false}
     * @throws BusinessException si {@code condition} es {@code false}
     */
    public static void check(boolean condition, String message) {
        if (!condition) {
            throw new BusinessException(message);
        }
    }
}
