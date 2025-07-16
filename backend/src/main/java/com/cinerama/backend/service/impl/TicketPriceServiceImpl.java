package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.TicketPriceRequest;
import com.cinerama.backend.dto.TicketPriceResponse;
import com.cinerama.backend.entity.TicketPrice;
import com.cinerama.backend.entity.Showtime;
import com.cinerama.backend.enums.TicketType;
import com.cinerama.backend.repository.TicketPriceRepository;
import com.cinerama.backend.repository.ShowtimeRepository;
import com.cinerama.backend.service.TicketPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the TicketPriceService interface.
 * 
 * <p>This class provides the business logic for managing
 * TicketPrice entities and their operations.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketPriceServiceImpl implements TicketPriceService {
    
    private final TicketPriceRepository ticketPriceRepository;
    private final ShowtimeRepository showtimeRepository;
    
    @Override
    public List<TicketPriceResponse> getAllTicketPrices() {
        log.debug("Retrieving all ticket prices");
        return ticketPriceRepository.findAll()
                .stream()
                .map(this::toTicketPriceResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<TicketPriceResponse> getTicketPriceById(Long id) {
        log.debug("Retrieving ticket price by ID: {}", id);
        return ticketPriceRepository.findById(id)
                .map(this::toTicketPriceResponse);
    }
    
    @Override
    public List<TicketPriceResponse> getTicketPricesByShowtimeId(Long showtimeId) {
        log.debug("Retrieving ticket prices by showtime ID: {}", showtimeId);
        return ticketPriceRepository.findByShowtimeId(showtimeId)
                .stream()
                .map(this::toTicketPriceResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TicketPriceResponse> getTicketPricesByType(TicketType type) {
        log.debug("Retrieving ticket prices by type: {}", type);
        return ticketPriceRepository.findByType(type)
                .stream()
                .map(this::toTicketPriceResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public TicketPriceResponse createTicketPrice(TicketPriceRequest ticketPriceRequest) {
        log.debug("Creating new ticket price for showtime ID: {} with type: {}", 
                ticketPriceRequest.getShowtimeId(), ticketPriceRequest.getType());
        
        Showtime showtime = showtimeRepository.findById(ticketPriceRequest.getShowtimeId())
                .orElseThrow(() -> new IllegalArgumentException("Showtime not found with ID: " + ticketPriceRequest.getShowtimeId()));
        
        TicketPrice ticketPrice = TicketPrice.builder()
                .showtime(showtime)
                .type(ticketPriceRequest.getType())
                .price(ticketPriceRequest.getPrice())
                .build();
        
        TicketPrice savedTicketPrice = ticketPriceRepository.save(ticketPrice);
        log.info("Ticket price created successfully with ID: {}", savedTicketPrice.getId());
        
        return toTicketPriceResponse(savedTicketPrice);
    }
    
    @Override
    @Transactional
    public TicketPriceResponse updateTicketPrice(Long id, TicketPriceRequest ticketPriceRequest) {
        log.debug("Updating ticket price with ID: {}", id);
        
        TicketPrice existingTicketPrice = ticketPriceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket price not found with ID: " + id));
        
        Showtime showtime = showtimeRepository.findById(ticketPriceRequest.getShowtimeId())
                .orElseThrow(() -> new IllegalArgumentException("Showtime not found with ID: " + ticketPriceRequest.getShowtimeId()));
        
        existingTicketPrice.setShowtime(showtime);
        existingTicketPrice.setType(ticketPriceRequest.getType());
        existingTicketPrice.setPrice(ticketPriceRequest.getPrice());
        
        TicketPrice updatedTicketPrice = ticketPriceRepository.save(existingTicketPrice);
        log.info("Ticket price updated successfully with ID: {}", updatedTicketPrice.getId());
        
        return toTicketPriceResponse(updatedTicketPrice);
    }
    
    @Override
    @Transactional
    public void deleteTicketPrice(Long id) {
        log.debug("Deleting ticket price with ID: {}", id);
        
        if (!ticketPriceRepository.existsById(id)) {
            throw new IllegalArgumentException("Ticket price not found with ID: " + id);
        }
        
        ticketPriceRepository.deleteById(id);
        log.info("Ticket price deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional
    public List<TicketPriceResponse> createTicketPricesForShowtime(List<TicketPriceRequest> ticketPriceRequests) {
        log.debug("Creating multiple ticket prices for showtime, count: {}", ticketPriceRequests.size());
        
        return ticketPriceRequests.stream()
                .map(this::createTicketPrice)
                .collect(Collectors.toList());
    }
    
    /**
     * Converts a TicketPrice entity to TicketPriceResponse DTO.
     * 
     * @param ticketPrice The TicketPrice entity to convert
     * @return The converted TicketPriceResponse DTO
     */
    private TicketPriceResponse toTicketPriceResponse(TicketPrice ticketPrice) {
        return TicketPriceResponse.builder()
                .id(ticketPrice.getId())
                .showtimeId(ticketPrice.getShowtime().getId())
                .type(ticketPrice.getType())
                .price(ticketPrice.getPrice())
                .createdAt(ticketPrice.getCreatedAt())
                .updatedAt(ticketPrice.getUpdatedAt())
                .build();
    }
}
