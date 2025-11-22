package com.cinerama.backend.controller;

import com.cinerama.backend.dto.*;
import com.cinerama.backend.entity.*;
import com.cinerama.backend.exception.user.UserNotFoundException;
import com.cinerama.backend.repository.*;
import com.cinerama.backend.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST controller for handling user-related operations.
 *
 * <p>This controller manages user profile operations for authenticated users
 * in the Cinerama cinema booking system. All endpoints require valid JWT authentication
 * as configured in the WebSecurityConfig.</p>
 *
 * @author Cinerama Development Team
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final FavoriteRepository favoriteRepository;
    private final MovieRepository movieRepository;
    private final DeviceRepository deviceRepository;
    private final NotificationRepository notificationRepository;

    /**
     * Retrieves the profile information of the currently authenticated user.
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(Authentication authentication) {
        String email = (String) authentication.getPrincipal();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserProfileResponse response = new UserProfileResponse(
                user.getName(),
                user.getEmail(),
                user.getRole().getName()
        );

        return ResponseEntity.ok(response);
    }

    // ========== FAVORITES ENDPOINTS ==========

    /**
     * Gets all favorite movies for the authenticated user.
     *
     * @param authentication Spring Security authentication object
     * @return ResponseEntity with favorites wrapped in { favorites: [...] }
     */
    @GetMapping("/me/favorites")
    public ResponseEntity<Map<String, Object>> getUserFavorites(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        List<Favorite> favorites = favoriteRepository.findByUserId(user.getId());
        
        List<MovieResponse> movieResponses = favorites.stream()
                .map(favorite -> convertToMovieResponse(favorite.getMovie()))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(Map.of("favorites", movieResponses));
    }

    /**
     * Adds a movie to user's favorites.
     *
     * @param request containing movieId
     * @param authentication Spring Security authentication object
     * @return ResponseEntity with 201 status and success response
     */
    @PostMapping("/me/favorites")
    @Transactional
    public ResponseEntity<SuccessResponse> addFavorite(
            @Valid @RequestBody AddFavoriteRequest request,
            Authentication authentication) {
        
        String email = (String) authentication.getPrincipal();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        
        // Check if already favorited
        if (favoriteRepository.existsByUserIdAndMovieId(user.getId(), movie.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(SuccessResponse.of(false, "Movie already in favorites"));
        }
        
        Favorite favorite = Favorite.builder()
                .user(user)
                .movie(movie)
                .build();
        
        favoriteRepository.save(favorite);
        
        MovieResponse movieResponse = convertToMovieResponse(movie);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of(true, "Movie added to favorites", 
                        Map.of("favorite", movieResponse)));
    }

    /**
     * Removes a movie from user's favorites.
     *
     * @param movieId the movie ID to remove
     * @param authentication Spring Security authentication object
     * @return ResponseEntity with success response
     */
    @DeleteMapping("/me/favorites/{movieId}")
    @Transactional
    public ResponseEntity<SuccessResponse> removeFavorite(
            @PathVariable Long movieId,
            Authentication authentication) {
        
        String email = (String) authentication.getPrincipal();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        if (!favoriteRepository.existsByUserIdAndMovieId(user.getId(), movieId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(SuccessResponse.of(false, "Favorite not found"));
        }
        
        favoriteRepository.deleteByUserIdAndMovieId(user.getId(), movieId);
        
        return ResponseEntity.ok(SuccessResponse.of(true, "Movie removed from favorites"));
    }

    // ========== DEVICE/PUSH TOKEN ENDPOINTS ==========

    /**
     * Registers a push notification device token for the user.
     *
     * @param request containing provider, token, and platform
     * @param authentication Spring Security authentication object
     * @return ResponseEntity with 201 status and device data
     */
    @PostMapping("/me/devices")
    @Transactional
    public ResponseEntity<SuccessResponse> registerDevice(
            @Valid @RequestBody DeviceRequest request,
            Authentication authentication) {
        
        String email = (String) authentication.getPrincipal();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        // Check if device already registered
        if (deviceRepository.existsByUserIdAndToken(user.getId(), request.getToken())) {
            Device existingDevice = deviceRepository.findByUserIdAndToken(user.getId(), request.getToken())
                    .orElseThrow();
            
            Map<String, Object> deviceData = Map.of(
                    "id", existingDevice.getId(),
                    "provider", existingDevice.getProvider(),
                    "token", existingDevice.getToken(),
                    "platform", existingDevice.getPlatform()
            );
            
            return ResponseEntity.ok(SuccessResponse.of(true, "Device already registered", 
                    Map.of("device", deviceData)));
        }
        
        Device device = Device.builder()
                .user(user)
                .provider(request.getProvider())
                .token(request.getToken())
                .platform(request.getPlatform())
                .build();
        
        Device savedDevice = deviceRepository.save(device);
        
        Map<String, Object> deviceData = Map.of(
                "id", savedDevice.getId(),
                "provider", savedDevice.getProvider(),
                "token", savedDevice.getToken(),
                "platform", savedDevice.getPlatform()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of(true, "Device registered successfully", 
                        Map.of("device", deviceData)));
    }

    /**
     * Removes a device token from the user's registered devices.
     *
     * @param deviceId the device ID to remove
     * @param authentication Spring Security authentication object
     * @return ResponseEntity with success response
     */
    @DeleteMapping("/me/devices/{deviceId}")
    @Transactional
    public ResponseEntity<SuccessResponse> removeDevice(
            @PathVariable Long deviceId,
            Authentication authentication) {
        
        String email = (String) authentication.getPrincipal();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
        
        // Verify device belongs to user
        if (!device.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(SuccessResponse.of(false, "Access denied"));
        }
        
        deviceRepository.delete(device);
        
        return ResponseEntity.ok(SuccessResponse.of(true, "Device removed successfully"));
    }

    // ========== NOTIFICATIONS ENDPOINTS ==========

    /**
     * Gets all notifications for the authenticated user.
     *
     * @param authentication Spring Security authentication object
     * @return ResponseEntity with notifications wrapped in { notifications: [...] }
     */
    @GetMapping("/me/notifications")
    public ResponseEntity<Map<String, Object>> getUserNotifications(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        
        List<Map<String, Object>> notificationData = notifications.stream()
                .map(notification -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("id", notification.getId());
                    map.put("title", notification.getTitle());
                    map.put("message", notification.getMessage());
                    map.put("type", notification.getType());
                    map.put("isRead", notification.getIsRead());
                    map.put("createdAt", notification.getCreatedAt().toString());
                    map.put("data", notification.getData() != null ? notification.getData() : "");
                    return map;
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(Map.of("notifications", notificationData));
    }

    // ========== ADMIN ENDPOINTS ==========

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportUsersToExcel() {
        ByteArrayInputStream excelStream = userService.exportUsersToExcel();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=usuarios.xlsx")
                .body(new InputStreamResource(excelStream));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use.");
        }

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully.");
    }

    // ========== HELPER METHODS ==========

    private MovieResponse convertToMovieResponse(Movie movie) {
        MovieResponse response = new MovieResponse();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setDescriptionShowtimes(movie.getDescriptionShowtimes());
        response.setDescriptionMovie(movie.getDescriptionMovie());
        response.setDuration(movie.getDuration());
        response.setImageUrl(movie.getPosterUrl());
        response.setTrailerUrl(movie.getTrailerUrl());
        response.setReleaseDate(movie.getReleaseDate());
        response.setStatus(movie.getStatus());
        response.setRating(movie.getRating());
        response.setDirector(movie.getDirector());
        response.setCast(movie.getCast());
        response.setGenreName(movie.getGenre() != null ? movie.getGenre().getName() : null);
        response.setGenreId(movie.getGenre() != null ? movie.getGenre().getId() : null);
        response.setCreatedAt(movie.getCreatedAt());
        response.setUpdatedAt(movie.getUpdatedAt());
        return response;
    }
}