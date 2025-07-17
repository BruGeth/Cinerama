package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.CinemaRequest;
import com.cinerama.backend.dto.CinemaResponse;
import com.cinerama.backend.dto.RoomSummaryResponse;
import com.cinerama.backend.entity.Cinema;
import com.cinerama.backend.enums.CinemaStatus;
import com.cinerama.backend.repository.CinemaRepository;
import com.cinerama.backend.service.CinemaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the CinemaService interface.
 * 
 * <p>This class provides the business logic for managing
 * Cinema entities and their operations.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CinemaServiceImpl implements CinemaService {
    
    private final CinemaRepository cinemaRepository;
    
    @Override
    public List<CinemaResponse> getAllCinemas() {
        log.debug("Retrieving all cinemas");
        return cinemaRepository.findAll()
                .stream()
                .map(this::toCinemaResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<CinemaResponse> getCinemaById(Long id) {
        log.debug("Retrieving cinema with ID: {}", id);
        return cinemaRepository.findById(id)
                .map(this::toCinemaResponse);
    }
    
    @Override
    public List<CinemaResponse> getCinemasByStatus(CinemaStatus status) {
        log.debug("Retrieving cinemas with status: {}", status);
        return cinemaRepository.findByStatus(status)
                .stream()
                .map(this::toCinemaResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CinemaResponse> getCinemasByCity(String city) {
        log.debug("Retrieving cinemas in city: {}", city);
        return cinemaRepository.findByCity(city)
                .stream()
                .map(this::toCinemaResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public CinemaResponse createCinema(CinemaRequest cinemaRequest) {
        log.debug("Creating new cinema: {}", cinemaRequest.getName());
        Cinema cinema = toCinemaEntity(cinemaRequest);
        Cinema savedCinema = cinemaRepository.save(cinema);
        log.info("Cinema created successfully with ID: {}", savedCinema.getId());
        return toCinemaResponse(savedCinema);
    }
    
    @Override
    @Transactional
    public CinemaResponse updateCinema(Long id, CinemaRequest cinemaRequest) {
        log.debug("Updating cinema with ID: {}", id);
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinema not found with ID: " + id));
        
        updateCinemaFields(cinema, cinemaRequest);
        Cinema updatedCinema = cinemaRepository.save(cinema);
        log.info("Cinema updated successfully with ID: {}", updatedCinema.getId());
        return toCinemaResponse(updatedCinema);
    }
    
    @Override
    @Transactional
    public void deleteCinema(Long id) {
        log.debug("Deleting cinema with ID: {}", id);
        if (!cinemaRepository.existsById(id)) {
            throw new RuntimeException("Cinema not found with ID: " + id);
        }
        cinemaRepository.deleteById(id);
        log.info("Cinema deleted successfully with ID: {}", id);
    }
    
    @Override
    public List<CinemaResponse> searchCinemasByName(String name) {
        log.debug("Searching cinemas by name: {}", name);
        return cinemaRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toCinemaResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Converts a Cinema entity to a CinemaResponse DTO.
     * 
     * @param cinema The cinema entity
     * @return The cinema response DTO
     */
    private CinemaResponse toCinemaResponse(Cinema cinema) {
        List<RoomSummaryResponse> roomSummaries = cinema.getRooms() != null ? 
                cinema.getRooms().stream()
                        .map(room -> RoomSummaryResponse.builder()
                                .id(room.getId())
                                .name(room.getName())
                                .capacity(room.getCapacity())
                                .type(room.getType())
                                .status(room.getStatus())
                                .build())
                        .collect(Collectors.toList()) : List.of();
        
        return CinemaResponse.builder()
                .id(cinema.getId())
                .name(cinema.getName())
                .address(cinema.getAddress())
                .city(cinema.getCity())
                .phone(cinema.getPhone())
                .email(cinema.getEmail())
                .status(cinema.getStatus())
                .rooms(roomSummaries)
                .createdAt(cinema.getCreatedAt())
                .updatedAt(cinema.getUpdatedAt())
                .build();
    }
    
    /**
     * Converts a CinemaRequest DTO to a Cinema entity.
     * 
     * @param cinemaRequest The cinema request DTO
     * @return The cinema entity
     */
    private Cinema toCinemaEntity(CinemaRequest cinemaRequest) {
        return Cinema.builder()
                .name(cinemaRequest.getName())
                .address(cinemaRequest.getAddress())
                .city(cinemaRequest.getCity())
                .phone(cinemaRequest.getPhone())
                .email(cinemaRequest.getEmail())
                .status(cinemaRequest.getStatus())
                .build();
    }
    
    /**
     * Updates cinema entity fields with request data.
     * 
     * @param cinema The cinema entity to update
     * @param cinemaRequest The request data
     */
    private void updateCinemaFields(Cinema cinema, CinemaRequest cinemaRequest) {
        cinema.setName(cinemaRequest.getName());
        cinema.setAddress(cinemaRequest.getAddress());
        cinema.setCity(cinemaRequest.getCity());
        cinema.setPhone(cinemaRequest.getPhone());
        cinema.setEmail(cinemaRequest.getEmail());
        cinema.setStatus(cinemaRequest.getStatus());
    }
}
