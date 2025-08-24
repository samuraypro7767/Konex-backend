package com.konex.Konex.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controlador de verificación de salud de la aplicación.
 * <p>
 * Expone un endpoint simple para comprobaciones de <i>liveness/readiness</i>
 * por parte de balanceadores, sondas (p. ej. Kubernetes) y herramientas de monitoreo.
 * Devuelve <code>200 OK</code> con el cuerpo <code>"OK"</code> cuando la aplicación está respondiendo.
 * </p>
 */
@RestController
public class HealthController {

    /**
     * Endpoint de salud de la aplicación.
     *
     * @return la cadena literal {@code "OK"} indicando que el servicio está operativo
     * @implNote Este endpoint no realiza verificaciones profundas (DB, colas, etc.).
     *           Para <i>deep health checks</i>, considera un endpoint adicional.
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
