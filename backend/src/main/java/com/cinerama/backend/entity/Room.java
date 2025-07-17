package com.cinerama.backend.entity;

import com.cinerama.backend.enums.RoomStatus;
import com.cinerama.backend.enums.RoomType;
import com.cinerama.backend.enums.TechnologyType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @Column(nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats;

    @Enumerated(EnumType.STRING)
    private RoomType type; // STANDARD, PREMIUM, VIP, IMAX

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<TechnologyType> technology; // TWO_D, THREE_D, IMAX, FOUR_DX

    @Column(name = "audio_system")
    private String audioSystem;

    @Enumerated(EnumType.STRING)
    private RoomStatus status; // ACTIVE, INACTIVE, MAINTENANCE

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    private List<Showtime> showtimes;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}


