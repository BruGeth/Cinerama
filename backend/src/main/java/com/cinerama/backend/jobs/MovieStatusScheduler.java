package com.cinerama.backend.jobs;

import com.cinerama.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler for automatically updating movie statuses based on release dates.
 * 
 * <p>This component runs scheduled tasks to keep movie statuses synchronized
 * with their release dates, updating movies to COMING_SOON or NOW_PLAYING
 * as appropriate.</p>
 *
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-17
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MovieStatusScheduler {

    private final MovieService movieService;

    /**
     * Updates movie statuses every hour.
     * Movies with future release dates are set to COMING_SOON,
     * while movies with past or current release dates are set to NOW_PLAYING.
     */
    @Scheduled(cron = "0 0 * * * *") // Executes every hour at minute 0
    public void actualizarEstadoPeliculas() {
        log.info("⏰ Iniciando tarea programada de actualización de estados de películas");
        
        try {
            movieService.updateMovieStatuses();
            log.info("✅ Actualización de estados completada exitosamente");
        } catch (Exception e) {
            log.error("❌ Error durante la actualización de estados de películas: {}", e.getMessage(), e);
        }
    }
}
