package com.crossculture.canvas.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.crossculture.canvas.model.Artist;
import com.crossculture.canvas.model.Booking;
import com.crossculture.canvas.model.User;
import com.crossculture.canvas.model.Venue; // Added import
import com.crossculture.canvas.repository.ArtistRepository;
import com.crossculture.canvas.service.BookingService;
import com.crossculture.canvas.service.UserService;
import com.crossculture.canvas.service.VenueService; // Added import



@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private VenueService venueService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ArtistRepository artistRepository; // Added

    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('ARTIST')")
    public String myBookings(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Booking> bookings = bookingService.getBookingsByArtist(user);
        model.addAttribute("bookings", bookings);
        return "my-bookings";
    }

    @GetMapping("/venue-bookings")
    @PreAuthorize("hasRole('VENUE_OWNER')")
    public String venueBookings(Model model, Principal principal) {
        User owner = userService.findByUsername(principal.getName());
        List<Venue> venues = venueService.getVenuesByOwner(owner);
        model.addAttribute("venues", venues);
        return "venue-bookings";
    }

    @GetMapping("/new/{venueId}")
    @PreAuthorize("hasRole('ARTIST')")
    public String createBookingForm(@PathVariable Long venueId, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Artist artist = artistRepository.findByUser(user).orElse(null);
        Optional<Venue> venue = venueService.getVenueById(venueId);
        
        if (venue.isPresent()) {
            Booking booking = new Booking();
            booking.setArtist(artist); // Set Artist object
            booking.setVenue(venue.get());
            model.addAttribute("booking", booking);
            return "booking-form";
        }
        
        return "redirect:/venues";
    }

    @GetMapping("/venue/{venueId}/bookings")
    @PreAuthorize("hasRole('VENUE_OWNER')")
    public String venueBookings(@PathVariable Long venueId, Model model, Principal principal) {
        User owner = userService.findByUsername(principal.getName());
        Optional<Venue> venue = venueService.getVenueById(venueId);

        if (venue.isEmpty() || !venue.get().getOwner().getId().equals(owner.getId())) {
            return "redirect:/venues/my-venues";
        }

        List<Booking> bookings = bookingService.getBookingsByVenue(venue.get());
        model.addAttribute("venue", venue.get());
        model.addAttribute("bookings", bookings);
        return "venue-bookings-list";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ARTIST')")
    public String saveBooking(@ModelAttribute Booking booking,
                            @RequestParam("venue.id") Long venueId,
                            Principal principal) {

        User user = userService.findByUsername(principal.getName());
        Artist artist = artistRepository.findByUser(user).orElse(null);
        if (artist == null) return "redirect:/artists/new";

        Venue venue = venueService.getVenueById(venueId).orElse(null);
        if (venue == null) return "redirect:/venues";

        booking.setArtist(artist);
        booking.setVenue(venue);
        bookingService.createBooking(booking); // ✅ saves + notifies

        return "redirect:/bookings/my-bookings";
    }

    @GetMapping("/approve/{id}")
    @PreAuthorize("hasRole('VENUE_OWNER')")
    public String approveBooking(@PathVariable Long id, Principal principal) {
        User owner = userService.findByUsername(principal.getName());
        Optional<Booking> booking = bookingService.getBookingById(id);
        
        if (booking.isPresent() && booking.get().getVenue().getOwner().getId().equals(owner.getId())) {
            bookingService.approveBooking(id);
        }
        
        return "redirect:/bookings/venue-bookings";
    }

    @GetMapping("/reject/{id}")
    @PreAuthorize("hasRole('VENUE_OWNER')")
    public String rejectBooking(@PathVariable Long id, Principal principal) {
        User owner = userService.findByUsername(principal.getName());
        Optional<Booking> booking = bookingService.getBookingById(id);
        
        if (booking.isPresent() && booking.get().getVenue().getOwner().getId().equals(owner.getId())) {
            bookingService.rejectBooking(id);
        }
        
        return "redirect:/bookings/venue-bookings";
    }

    @GetMapping("/cancel/{id}")
    @PreAuthorize("hasRole('ARTIST')")
    public String cancelBooking(@PathVariable Long id, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Artist artist = artistRepository.findByUser(user).orElse(null);
        Optional<Booking> booking = bookingService.getBookingById(id);
        
        if (booking.isPresent() && booking.get().getArtist().getId().equals(artist.getId())) {
            bookingService.cancelBooking(id);
        }
        
        return "redirect:/bookings/my-bookings";
    }
}