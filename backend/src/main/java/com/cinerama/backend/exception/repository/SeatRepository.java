package com.cinerama.backend.exception.repository;

import com.cinerama.backend.entity.Seat;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowIdAndAvailableTrue(Long showId);
    List<Seat> findByIdIn(List<Long> ids);
    List<Seat> findAllByIdInAndAvailableTrue(@NotEmpty(message = "At least one seat must be selected") List<Long> seatIds);
}
