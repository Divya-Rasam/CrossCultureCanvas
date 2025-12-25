package com.crossculture.canvas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crossculture.canvas.model.Artist;
import com.crossculture.canvas.model.Media;
import com.crossculture.canvas.repository.MediaRepository;

@Service
public class MediaService {
    @Autowired
    private MediaRepository mediaRepository;

    public List<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    public Optional<Media> getMediaById(Long id) {
        return mediaRepository.findById(id);
    }

    public Media createMedia(Media media) {
        if (media.getCreatedAt() == null) {
            media.setCreatedAt(LocalDateTime.now());
        }
        return mediaRepository.save(media);
    }

    public Media updateMedia(Media media) {
        return mediaRepository.save(media);
    }

    public void deleteMedia(Long id) {
        mediaRepository.deleteById(id);
    }

    public List<Media> getMediaByArtist(Artist artist) {
        return mediaRepository.findByArtistId(artist.getId());
    }

    public List<Media> getMediaByType(Media.MediaType type) {
        return mediaRepository.findByType(type);
    }
}