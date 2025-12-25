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

import com.crossculture.canvas.model.User;
import com.crossculture.canvas.model.Venue;
import com.crossculture.canvas.service.UserService;
import com.crossculture.canvas.service.VenueService;

@Controller
@RequestMapping("/venues")
public class VenueController {

    @Autowired
    private VenueService venueService;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public String listVenues(Model model) {
        List<Venue> venues = venueService.getAllVenues();
        model.addAttribute("venues", venues);
        return "venue-list";
    }

    @GetMapping("/{id}")
    public String viewVenue(@PathVariable Long id, Model model) {
        return venueService.getVenueById(id)
                .map(venue -> {
                    model.addAttribute("venue", venue);
                    return "venue-details";
                })
                .orElse("redirect:/venues");
    }

    @GetMapping("/my-venues")
    @PreAuthorize("hasRole('VENUE_OWNER')")
    public String myVenues(Model model, Principal principal) {
        User owner = userService.findByUsername(principal.getName());
        List<Venue> venues = venueService.getVenuesByOwner(owner);
        model.addAttribute("venues", venues);
        return "my-venues";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('VENUE_OWNER')")
    public String createVenueForm(Model model) {
        model.addAttribute("venue", new Venue());
        return "venue-form";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('VENUE_OWNER')")
    public String saveVenue(@ModelAttribute Venue venue, Principal principal) {
        User owner = userService.findByUsername(principal.getName());
        venue.setOwner(owner);
        venueService.createVenue(venue);
        return "redirect:/venues/my-venues";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('VENUE_OWNER')")
    public String editVenueForm(@PathVariable Long id, Model model, Principal principal) {
        User owner = userService.findByUsername(principal.getName());
        Optional<Venue> venue = venueService.getVenueById(id);
        
        if (venue.isPresent() && venue.get().getOwner().getId().equals(owner.getId())) {
            model.addAttribute("venue", venue.get());
            return "venue-form";
        } else {
            return "redirect:/venues/my-venues";
        }
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('VENUE_OWNER')")
    public String deleteVenue(@PathVariable Long id, Principal principal) {
        User owner = userService.findByUsername(principal.getName());
        Optional<Venue> venue = venueService.getVenueById(id);
        
        if (venue.isPresent() && venue.get().getOwner().getId().equals(owner.getId())) {
            venueService.deleteVenue(id);
        }
        
        return "redirect:/venues/my-venues";
    }
}