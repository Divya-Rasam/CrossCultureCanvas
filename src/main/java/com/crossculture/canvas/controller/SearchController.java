package com.crossculture.canvas.controller;

import com.crossculture.canvas.model.Artist;
import com.crossculture.canvas.model.Event;
import com.crossculture.canvas.model.Venue;
import com.crossculture.canvas.service.ArtistService;
import com.crossculture.canvas.service.EventService;
import com.crossculture.canvas.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class SearchController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private VenueService venueService;

    @Autowired
    private EventService eventService;

    @GetMapping("/search")
    public String search(@RequestParam("query") String query,
                        @RequestParam(value = "type", defaultValue = "all") String type,
                        @RequestParam(value = "category", required = false) String category,
                        @RequestParam(value = "city", required = false) String city,
                        @RequestParam(value = "date", required = false) 
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                        @RequestParam(defaultValue = "0") int page,
                        Model model) {

        String q = query.trim().toLowerCase();
        Pageable pageable = PageRequest.of(page, 12);

        if ("all".equals(type) || "artists".equals(type)) {
            Page<Artist> artists = artistService.searchArtists(q, pageable);
            model.addAttribute("artists", artists);
        }

        if ("all".equals(type) || "venues".equals(type)) {
            Page<Venue> venues = venueService.searchVenues(q, pageable);
            model.addAttribute("venues", venues);
        }

        if ("all".equals(type) || "events".equals(type)) {
            Page<Event> events = eventService.searchEvents(q, pageable);
            model.addAttribute("events", events);
        }

        model.addAttribute("query", query);
        model.addAttribute("type", type);
        model.addAttribute("category", category);
        model.addAttribute("city", city);
        model.addAttribute("date", date);

        return "search-results";
    }
}