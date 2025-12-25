package com.crossculture.canvas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crossculture.canvas.model.Booking;
import com.crossculture.canvas.model.Booking.BookingStatus;
import com.crossculture.canvas.model.User;
import com.crossculture.canvas.model.Venue;
import com.crossculture.canvas.repository.BookingRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private NotificationService notificationService;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking createBooking(Booking booking) {
        if (booking.getCreatedAt() == null) {
            booking.setCreatedAt(LocalDateTime.now());
        }
        if (booking.getStatus() == null) {
            booking.setStatus(BookingStatus.PENDING);
        }

        Booking saved = bookingRepository.save(booking);

        if (saved.getVenue() != null && saved.getVenue().getOwner() != null &&
            saved.getArtist() != null && saved.getArtist().getArtistName() != null) {

            notificationService.createNewBookingNotification(
                saved.getVenue().getOwner(),
                saved.getArtist().getArtistName(),
                saved.getEventTitle()
            );
            log.info("🔔 New booking notification sent to venue owner: {}", saved.getVenue().getOwner().getUsername());
        } else {
            log.warn("⚠️ Skipped notification: venue owner or artist missing for booking: {}", saved.getId());
        }

        return saved;
    }

    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

public List<Booking> getBookingsByArtist(User artist) {
    return bookingRepository.findByArtistUserId(artist.getId());
}

    public List<Booking> getBookingsByVenue(Venue venue) {
        return bookingRepository.findByVenueId(venue.getId());
    }

    public List<Booking> getUpcomingBookings() {
        return bookingRepository.findByEventDateAfter(LocalDateTime.now());
    }

    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    public Booking approveBooking(Long id) {
        Optional<Booking> opt = getBookingById(id);
        if (opt.isEmpty()) {
            log.warn("❌ Approve failed: booking not found: {}", id);
            return null;
        }

        Booking booking = opt.get();
        booking.setStatus(BookingStatus.APPROVED);
        Booking updated = updateBooking(booking);

        if (updated.getArtist() != null && updated.getArtist().getUser() != null) {
            notificationService.createBookingApprovedNotification(
                updated.getArtist().getUser(),
                updated.getVenue().getName(),
                updated.getEventTitle()
            );
            log.info("🔔 Approval notification sent to artist: {}", updated.getArtist().getUser().getUsername());
        } else {
            log.warn("⚠️ Skipped approval notification: artist or user missing for booking: {}", updated.getId());
        }

        return updated;
    }

    public Booking rejectBooking(Long id) {
        Optional<Booking> opt = getBookingById(id);
        if (opt.isEmpty()) {
            log.warn("❌ Reject failed: booking not found: {}", id);
            return null;
        }

        Booking booking = opt.get();
        booking.setStatus(BookingStatus.REJECTED);
        Booking updated = updateBooking(booking);

        if (updated.getArtist() != null && updated.getArtist().getUser() != null) {
            notificationService.createBookingRejectedNotification(
                updated.getArtist().getUser(),
                updated.getVenue().getName(),
                updated.getEventTitle()
            );
            log.info("🔔 Rejection notification sent to artist: {}", updated.getArtist().getUser().getUsername());
        } else {
            log.warn("⚠️ Skipped rejection notification: artist or user missing for booking: {}", updated.getId());
        }

        return updated;
    }

    public Booking cancelBooking(Long id) {
        Optional<Booking> opt = getBookingById(id);
        if (opt.isEmpty()) {
            log.warn("❌ Cancel failed: booking not found: {}", id);
            return null;
        }

        Booking booking = opt.get();
        booking.setStatus(BookingStatus.CANCELLED);
        return updateBooking(booking);
    }
}