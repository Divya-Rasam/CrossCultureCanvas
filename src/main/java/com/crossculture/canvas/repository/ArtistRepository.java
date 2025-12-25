package com.crossculture.canvas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crossculture.canvas.model.Artist;
import com.crossculture.canvas.model.ArtistCategory;
import com.crossculture.canvas.model.User;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    Optional<Artist> findByUser(User user);

    Optional<Artist> findByUserId(Long userId);

    List<Artist> findByCategory(ArtistCategory category);

    Page<Artist> findByArtistNameContainingIgnoreCaseOrBioContainingIgnoreCase(String name, String bio, Pageable pageable);

    @Query("SELECT a FROM Artist a WHERE " +
           "LOWER(a.artistName) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(a.bio) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Artist> searchArtists(@Param("term") String term);
}