package com.crossculture.canvas.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crossculture.canvas.model.Venue;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findByOwnerId(Long ownerId);
    List<Venue> findByCity(String city);
    List<Venue> findByState(String state);
    
    @Query("SELECT v FROM Venue v WHERE " +
           "LOWER(v.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(v.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(v.address) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(v.city) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(v.state) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Venue> searchVenues(@Param("searchTerm") String searchTerm);

    /* PAGED SEARCH */
    Page<Venue> findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(String name, String city, Pageable pageable);
}