package com.crossculture.canvas.service;

import com.crossculture.canvas.model.User;
import com.crossculture.canvas.model.Venue;
import com.crossculture.canvas.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VenueService {
    @Autowired
    private VenueRepository venueRepository;

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Optional<Venue> getVenueById(Long id) {
        return venueRepository.findById(id);
    }

    public Venue createVenue(Venue venue) {
        if (venue.getCreatedAt() == null) {
            venue.setCreatedAt(LocalDateTime.now());
        }
        return venueRepository.save(venue);
    }

    public Venue updateVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    public void deleteVenue(Long id) {
        venueRepository.deleteById(id);
    }

    public List<Venue> getVenuesByOwner(User owner) {
        return venueRepository.findByOwnerId(owner.getId());
    }

    public List<Venue> getVenuesByCity(String city) {
        return venueRepository.findByCity(city);
    }

    public List<Venue> getVenuesByState(String state) {
        return venueRepository.findByState(state);
    }
    
    public List<Venue> searchVenues(String searchTerm) {
        return venueRepository.searchVenues(searchTerm);
    }

    /* PAGED SEARCH */
    public Page<Venue> searchVenues(String query, Pageable pageable) {
        return venueRepository.findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(query, query, pageable);
    }
}