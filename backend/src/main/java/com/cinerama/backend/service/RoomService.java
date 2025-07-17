package com.cinerama.backend.service;

import com.cinerama.backend.dto.RoomRequest;
import com.cinerama.backend.dto.RoomResponse;
import com.cinerama.backend.enums.RoomStatus;
import com.cinerama.backend.enums.RoomType;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing Room operations.
 * 
 * <p>This interface defines the business logic operations
 * for Room entities.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
public interface RoomService {
    
    /**
     * Retrieves all rooms.
     * 
     * @return List of all room responses
     */
    List<RoomResponse> getAllRooms();
    
    /**
     * Retrieves a room by its ID.
     * 
     * @param id The room ID
     * @return Optional containing the room response if found
     */
    Optional<RoomResponse> getRoomById(Long id);
    
    /**
     * Retrieves rooms by cinema ID.
     * 
     * @param cinemaId The cinema ID
     * @return List of rooms in the specified cinema
     */
    List<RoomResponse> getRoomsByCinemaId(Long cinemaId);
    
    /**
     * Retrieves rooms by status.
     * 
     * @param status The room status
     * @return List of rooms with the specified status
     */
    List<RoomResponse> getRoomsByStatus(RoomStatus status);
    
    /**
     * Retrieves rooms by type.
     * 
     * @param type The room type
     * @return List of rooms with the specified type
     */
    List<RoomResponse> getRoomsByType(RoomType type);
    
    /**
     * Creates a new room.
     * 
     * @param roomRequest The room creation request
     * @return The created room response
     */
    RoomResponse createRoom(RoomRequest roomRequest);
    
    /**
     * Updates an existing room.
     * 
     * @param id The room ID
     * @param roomRequest The room update request
     * @return The updated room response
     */
    RoomResponse updateRoom(Long id, RoomRequest roomRequest);
    
    /**
     * Deletes a room by its ID.
     * 
     * @param id The room ID
     */
    void deleteRoom(Long id);
    
    /**
     * Retrieves active rooms by cinema ID.
     * 
     * @param cinemaId The cinema ID
     * @return List of active rooms in the specified cinema
     */
    List<RoomResponse> getActiveRoomsByCinemaId(Long cinemaId);
}
