package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CotizacionVencimientoScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CotizacionVencimientoScheduler.class);

    private final CotizacionV1Service cotizacionService;

    public CotizacionVencimientoScheduler(CotizacionV1Service cotizacionService) {
        this.cotizacionService = cotizacionService;
    }

    @Scheduled(cron = "${app.cotizaciones.vencimiento-cron:0 0 * * * *}")
    public void procesarVencimientos() {
        int procesadas = cotizacionService.procesarCotizacionesVencidas("scheduler");
        if (procesadas > 0) {
            LOGGER.info("Cotizaciones vencidas procesadas: {}", procesadas);
        }
    }
}
