package com.crossculture.canvas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crossculture.canvas.model.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByArtistId(Long artistId);
    List<Media> findByType(Media.MediaType type);
}