package com.crossculture.canvas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crossculture.canvas.model.Artist;
import com.crossculture.canvas.model.ArtistCategory;
import com.crossculture.canvas.model.User;
import com.crossculture.canvas.repository.ArtistRepository;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    public Page<Artist> getAllArtistsPaginated(Pageable pageable) {
        return artistRepository.findAll(pageable);
    }

    public Optional<Artist> getArtistById(Long id) {
        return artistRepository.findById(id);
    }

    @Transactional
    public Artist createArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    @Transactional
    public Artist updateArtist(Artist artist) {
        if (!artistRepository.existsById(artist.getId())) {
            throw new RuntimeException("Artist not found with id: " + artist.getId());
        }
        return artistRepository.save(artist);
    }

    @Transactional
    public void deleteArtist(Long id) {
        if (!artistRepository.existsById(id)) {
            throw new RuntimeException("Artist not found with id: " + id);
        }
        artistRepository.deleteById(id);
    }

    public Optional<Artist> getArtistByUser(User user) {
        return artistRepository.findByUserId(user.getId());
    }

    public Artist findByUser(User user) {
        return artistRepository.findByUser(user).orElse(null);
    }

    public Artist saveArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    public List<Artist> getArtistsByCategory(ArtistCategory category) {
        return artistRepository.findByCategory(category);
    }

    public List<Artist> searchArtists(String searchTerm) {
        return artistRepository.searchArtists(searchTerm);
    }

    public Page<Artist> searchArtists(String query, Pageable pageable) {
        return artistRepository.findByArtistNameContainingIgnoreCaseOrBioContainingIgnoreCase(query, query, pageable);
    }
}