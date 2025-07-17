package com.cinerama.backend.service.impl;

import com.cinerama.backend.dto.RoomRequest;
import com.cinerama.backend.dto.RoomResponse;
import com.cinerama.backend.dto.CinemaSummaryResponse;
import com.cinerama.backend.entity.Room;
import com.cinerama.backend.entity.Cinema;
import com.cinerama.backend.enums.RoomStatus;
import com.cinerama.backend.enums.RoomType;
import com.cinerama.backend.repository.RoomRepository;
import com.cinerama.backend.repository.CinemaRepository;
import com.cinerama.backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the RoomService interface.
 * 
 * <p>This class provides the business logic for managing
 * Room entities and their operations.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService {
    
    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;
    
    @Override
    public List<RoomResponse> getAllRooms() {
        log.debug("Retrieving all rooms");
        return roomRepository.findAll()
                .stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<RoomResponse> getRoomById(Long id) {
        log.debug("Retrieving room by ID: {}", id);
        return roomRepository.findById(id)
                .map(this::toRoomResponse);
    }
    
    @Override
    public List<RoomResponse> getRoomsByCinemaId(Long cinemaId) {
        log.debug("Retrieving rooms by cinema ID: {}", cinemaId);
        return roomRepository.findByCinemaId(cinemaId)
                .stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<RoomResponse> getActiveRoomsByCinemaId(Long cinemaId) {
        log.debug("Retrieving active rooms by cinema ID: {}", cinemaId);
        return roomRepository.findByCinemaIdAndStatus(cinemaId, RoomStatus.ACTIVE)
                .stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<RoomResponse> getRoomsByStatus(RoomStatus status) {
        log.debug("Retrieving rooms by status: {}", status);
        return roomRepository.findByStatus(status)
                .stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<RoomResponse> getRoomsByType(RoomType type) {
        log.debug("Retrieving rooms by type: {}", type);
        return roomRepository.findByType(type)
                .stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public RoomResponse createRoom(RoomRequest roomRequest) {
        log.debug("Creating new room with name: {}", roomRequest.getName());
        
        Cinema cinema = cinemaRepository.findById(roomRequest.getCinemaId())
                .orElseThrow(() -> new IllegalArgumentException("Cinema not found with ID: " + roomRequest.getCinemaId()));
        
        Room room = Room.builder()
                .name(roomRequest.getName())
                .cinema(cinema)
                .capacity(roomRequest.getCapacity())
                .type(roomRequest.getType())
                .technology(roomRequest.getTechnology())
                .audioSystem(roomRequest.getAudioSystem())
                .status(roomRequest.getStatus())
                .build();
        
        Room savedRoom = roomRepository.save(room);
        log.info("Room created successfully with ID: {}", savedRoom.getId());
        
        return toRoomResponse(savedRoom);
    }
    
    @Override
    @Transactional
    public RoomResponse updateRoom(Long id, RoomRequest roomRequest) {
        log.debug("Updating room with ID: {}", id);
        
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + id));
        
        Cinema cinema = cinemaRepository.findById(roomRequest.getCinemaId())
                .orElseThrow(() -> new IllegalArgumentException("Cinema not found with ID: " + roomRequest.getCinemaId()));
        
        existingRoom.setName(roomRequest.getName());
        existingRoom.setCinema(cinema);
        existingRoom.setCapacity(roomRequest.getCapacity());
        existingRoom.setType(roomRequest.getType());
        existingRoom.setTechnology(roomRequest.getTechnology());
        existingRoom.setAudioSystem(roomRequest.getAudioSystem());
        existingRoom.setStatus(roomRequest.getStatus());
        
        Room updatedRoom = roomRepository.save(existingRoom);
        log.info("Room updated successfully with ID: {}", updatedRoom.getId());
        
        return toRoomResponse(updatedRoom);
    }
    
    @Override
    @Transactional
    public void deleteRoom(Long id) {
        log.debug("Deleting room with ID: {}", id);
        
        if (!roomRepository.existsById(id)) {
            throw new IllegalArgumentException("Room not found with ID: " + id);
        }
        
        roomRepository.deleteById(id);
        log.info("Room deleted successfully with ID: {}", id);
    }
    
    /**
     * Converts a Room entity to RoomResponse DTO.
     * 
     * @param room The Room entity to convert
     * @return The converted RoomResponse DTO
     */
    private RoomResponse toRoomResponse(Room room) {
        CinemaSummaryResponse cinemaSummary = CinemaSummaryResponse.builder()
                .id(room.getCinema().getId())
                .name(room.getCinema().getName())
                .city(room.getCinema().getCity())
                .status(room.getCinema().getStatus())
                .build();
        
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .cinema(cinemaSummary)
                .capacity(room.getCapacity())
                .type(room.getType())
                .technology(room.getTechnology())
                .audioSystem(room.getAudioSystem())
                .status(room.getStatus())
                .build();
    }
}
