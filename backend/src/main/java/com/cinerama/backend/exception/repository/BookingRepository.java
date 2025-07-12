package com.cinerama.backend.exception.repository;

import com.cinerama.backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

  }
