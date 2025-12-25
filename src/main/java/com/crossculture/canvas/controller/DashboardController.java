package com.crossculture.canvas.controller;

import com.crossculture.canvas.model.*;
import com.crossculture.canvas.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
public class DashboardController {

    @Autowired private UserService userService;
    @Autowired private ArtistService artistService;
    @Autowired private VenueService venueService;
    @Autowired private EventService eventService;
    @Autowired private BookingService bookingService;
    @Autowired private MediaService mediaService;
    @Autowired private NotificationService notificationService;

    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    public String dashboard(Model model, Principal principal) {
        try {
            User user = userService.findByUsername(principal.getName());
            if (user == null) {
                log.warn("User not found: {}", principal.getName());
                return "error/403";
            }
            model.addAttribute("user", user);

            // Notifications
            long unreadCount = notificationService.getUnreadCount(user.getId());
            model.addAttribute("unreadCount", unreadCount);

            List<Notification> recentNotifications = notificationService.getUserNotifications(user.getId())
                    .stream().limit(5).toList();
            model.addAttribute("recentNotifications", recentNotifications);

            // Role-based dashboard
            if (user.getRole() == User.Role.ARTIST) {
                setupArtistDashboard(model, user);
            } else if (user.getRole() == User.Role.VENUE_OWNER) {
                setupVenueOwnerDashboard(model, user);
            }

            return "dashboard";
        } catch (Exception e) {
            log.error("Dashboard error for user: {}", principal.getName(), e);
            return "error";
        }
    }

    private void setupArtistDashboard(Model model, User user) {
        artistService.getArtistByUser(user).ifPresentOrElse(artist -> {
            model.addAttribute("artist", artist);

            List<Media> mediaList = mediaService.getMediaByArtist(artist);
            model.addAttribute("mediaCount", mediaList.size());

            List<Booking> bookings = bookingService.getBookingsByArtist(user);
            model.addAttribute("bookings", bookings);

            long pending = bookings.stream()
                    .filter(b -> b.getStatus() == Booking.BookingStatus.PENDING).count();
            long approved = bookings.stream()
                    .filter(b -> b.getStatus() == Booking.BookingStatus.APPROVED).count();
            model.addAttribute("pendingBookings", pending);
            model.addAttribute("approvedBookings", approved);

            List<Event> upcoming = eventService.getUpcomingEvents().stream()
                    .filter(e -> e.getArtists() != null && e.getArtists().contains(user))
                    .limit(3).toList();
            model.addAttribute("upcomingEvents", upcoming);
        }, () -> {
            log.warn("Artist profile missing for user: {}", user.getUsername());
            model.addAttribute("artist", null);
            model.addAttribute("mediaCount", 0);
            model.addAttribute("bookings", List.of());
            model.addAttribute("pendingBookings", 0);
            model.addAttribute("approvedBookings", 0);
            model.addAttribute("upcomingEvents", List.of());
        });
    }

    private void setupVenueOwnerDashboard(Model model, User user) {
        List<Venue> venues = venueService.getVenuesByOwner(user);
        model.addAttribute("venues", venues);
        model.addAttribute("venueCount", venues.size());

        List<Booking> allBookings = venues.stream()
                .flatMap(v -> bookingService.getBookingsByVenue(v).stream()).toList();
        model.addAttribute("allBookings", allBookings);

        long pending = allBookings.stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.PENDING).count();
        long approved = allBookings.stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.APPROVED).count();
        model.addAttribute("pendingBookings", pending);
        model.addAttribute("approvedBookings", approved);

        List<Event> upcoming = eventService.getUpcomingEvents().stream()
                .filter(e -> venues.contains(e.getVenue()))
                .limit(3).toList();
        model.addAttribute("upcomingEvents", upcoming);
    }
}