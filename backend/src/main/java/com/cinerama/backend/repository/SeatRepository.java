package com.cinerama.backend.repository;

import com.cinerama.backend.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAllByIdInAndAvailableTrue(List<Long> ids);
}
