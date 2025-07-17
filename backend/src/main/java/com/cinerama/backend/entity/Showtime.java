package com.cinerama.backend.entity;

import com.cinerama.backend.enums.ShowtimeFormat;
import com.cinerama.backend.enums.ShowtimeLanguage;
import com.cinerama.backend.enums.ShowtimeStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "showtimes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private LocalDate showDate;

    @Column(nullable = false)
    private LocalTime showTime;

    @Enumerated(EnumType.STRING)
    private ShowtimeFormat format;

    @Enumerated(EnumType.STRING)
    private ShowtimeLanguage language;

    @Column(name = "available_seats")
    private Integer availableSeats;

    @OneToMany(mappedBy = "showtime", cascade = CascadeType.ALL)
    private List<TicketPrice> ticketPrices;

    @OneToMany(mappedBy = "showtime", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats;

    @Enumerated(EnumType.STRING)
    private ShowtimeStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}