package com.crossculture.canvas.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crossculture.canvas.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b JOIN b.artist a WHERE a.user.id = :userId")
List<Booking> findByArtistUserId(@Param("userId") Long userId);
    List<Booking> findByVenueId(Long venueId);
    List<Booking> findByEventDateAfter(LocalDateTime date);
    List<Booking> findByStatus(Booking.BookingStatus status);
}