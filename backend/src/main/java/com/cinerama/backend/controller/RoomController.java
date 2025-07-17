package com.cinerama.backend.controller;

import com.cinerama.backend.dto.RoomRequest;
import com.cinerama.backend.dto.RoomResponse;
import com.cinerama.backend.enums.RoomStatus;
import com.cinerama.backend.enums.RoomType;
import com.cinerama.backend.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Room operations.
 * 
 * <p>This controller provides endpoints for CRUD operations
 * on Room entities and room-related queries.</p>
 * 
 * @author Cinerama Development Team
 * @version 1.0
 * @since 2025-07-15
 */
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    
    private final RoomService roomService;
    
    /**
     * Retrieves all rooms.
     * 
     * @return List of all room responses
     */
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<RoomResponse> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }
    
    /**
     * Retrieves a room by its ID.
     * 
     * @param id The room ID
     * @return The room response if found, 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Retrieves rooms by cinema ID.
     * 
     * @param cinemaId The cinema ID
     * @return List of rooms in the specified cinema
     */
    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<List<RoomResponse>> getRoomsByCinemaId(@PathVariable Long cinemaId) {
        List<RoomResponse> rooms = roomService.getRoomsByCinemaId(cinemaId);
        return ResponseEntity.ok(rooms);
    }
    
    /**
     * Retrieves active rooms by cinema ID.
     * 
     * @param cinemaId The cinema ID
     * @return List of active rooms in the specified cinema
     */
    @GetMapping("/cinema/{cinemaId}/active")
    public ResponseEntity<List<RoomResponse>> getActiveRoomsByCinemaId(@PathVariable Long cinemaId) {
        List<RoomResponse> rooms = roomService.getActiveRoomsByCinemaId(cinemaId);
        return ResponseEntity.ok(rooms);
    }
    
    /**
     * Retrieves rooms by status.
     * 
     * @param status The room status (ACTIVE, INACTIVE, MAINTENANCE)
     * @return List of rooms with the specified status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RoomResponse>> getRoomsByStatus(@PathVariable RoomStatus status) {
        List<RoomResponse> rooms = roomService.getRoomsByStatus(status);
        return ResponseEntity.ok(rooms);
    }
    
    /**
     * Retrieves rooms by type.
     * 
     * @param type The room type (STANDARD, PREMIUM, VIP, IMAX)
     * @return List of rooms with the specified type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<RoomResponse>> getRoomsByType(@PathVariable RoomType type) {
        List<RoomResponse> rooms = roomService.getRoomsByType(type);
        return ResponseEntity.ok(rooms);
    }
    
    /**
     * Creates a new room.
     * 
     * @param roomRequest The room creation request
     * @return The created room response with 201 status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest roomRequest) {
        RoomResponse createdRoom = roomService.createRoom(roomRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }
    
    /**
     * Updates an existing room.
     * 
     * @param id The room ID
     * @param roomRequest The room update request
     * @return The updated room response
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long id, 
            @Valid @RequestBody RoomRequest roomRequest) {
        RoomResponse updatedRoom = roomService.updateRoom(id, roomRequest);
        return ResponseEntity.ok(updatedRoom);
    }
    
    /**
     * Deletes a room by its ID.
     * 
     * @param id The room ID
     * @return 204 No Content if successful
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
