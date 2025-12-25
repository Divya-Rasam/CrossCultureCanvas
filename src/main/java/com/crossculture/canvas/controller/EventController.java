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

import com.crossculture.canvas.model.Event;
import com.crossculture.canvas.model.User;
import com.crossculture.canvas.model.Venue;
import com.crossculture.canvas.service.EventService;
import com.crossculture.canvas.service.UserService;
import com.crossculture.canvas.service.VenueService;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;
    
    @Autowired
    private VenueService venueService;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public String listEvents(Model model) {
        List<Event> events = eventService.getUpcomingEvents();
        model.addAttribute("events", events);
        return "event-list";
    }

    @GetMapping("/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        return eventService.getEventById(id)
                .map(event -> {
                    model.addAttribute("event", event);
                    return "event-details";
                })
                .orElse("redirect:/events");
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ARTIST', 'VENUE_OWNER')")
    public String createEventForm(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        
        if (user.getRole() == User.Role.VENUE_OWNER) {
            List<Venue> venues = venueService.getVenuesByOwner(user);
            model.addAttribute("venues", venues);
        }
        
        model.addAttribute("event", new Event());
        return "event-form";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ARTIST', 'VENUE_OWNER')")
    public String saveEvent(@ModelAttribute Event event, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        
        // Set default status if not set
        if (event.getStatus() == null) {
            event.setStatus(Event.EventStatus.UPCOMING);
        }
        
        eventService.createEvent(event);
        return "redirect:/events";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ARTIST', 'VENUE_OWNER')")
    public String editEventForm(@PathVariable Long id, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Optional<Event> event = eventService.getEventById(id);
        
        if (event.isPresent()) {
            // Check if user is authorized to edit this event
            boolean isAuthorized = false;
            
            if (user.getRole() == User.Role.VENUE_OWNER && 
                event.get().getVenue().getOwner().getId().equals(user.getId())) {
                isAuthorized = true;
            } else if (user.getRole() == User.Role.ARTIST && 
                      event.get().getArtists().contains(user)) {
                isAuthorized = true;
            }
            
            if (isAuthorized) {
                model.addAttribute("event", event.get());
                
                if (user.getRole() == User.Role.VENUE_OWNER) {
                    List<Venue> venues = venueService.getVenuesByOwner(user);
                    model.addAttribute("venues", venues);
                }
                
                return "event-form";
            }
        }
        
        return "redirect:/events";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ARTIST', 'VENUE_OWNER')")
    public String deleteEvent(@PathVariable Long id, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Optional<Event> event = eventService.getEventById(id);
        
        if (event.isPresent()) {
            // Check if user is authorized to delete this event
            boolean isAuthorized = false;
            
            if (user.getRole() == User.Role.VENUE_OWNER && 
                event.get().getVenue().getOwner().getId().equals(user.getId())) {
                isAuthorized = true;
            } else if (user.getRole() == User.Role.ARTIST && 
                      event.get().getArtists().contains(user)) {
                isAuthorized = true;
            }
            
            if (isAuthorized) {
                eventService.deleteEvent(id);
            }
        }
        
        return "redirect:/events";
    }
}