package com.crossculture.canvas.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.crossculture.canvas.model.Artist;
import com.crossculture.canvas.model.ArtistCategory;
import com.crossculture.canvas.model.Media;
import com.crossculture.canvas.model.User;
import com.crossculture.canvas.service.ArtistService;
import com.crossculture.canvas.service.MediaService;
import com.crossculture.canvas.service.UserService;

@Controller
@RequestMapping("/artists")
public class ArtistController {

    @Autowired private ArtistService artistService;
    @Autowired private UserService    userService;
    @Autowired private MediaService   mediaService;

    /* ----------  PUBLIC PAGES  ---------- */
    @GetMapping
    public String listArtists(@RequestParam(defaultValue = "0")  int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "artistName") String sort,
                              @RequestParam(defaultValue = "asc") String direction, Model model) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Page<Artist> artistPage = artistService.getAllArtistsPaginated(
                PageRequest.of(page, size, Sort.by(sortDirection, sort)));

        model.addAttribute("artists", artistPage.getContent());
        model.addAttribute("artistPage",   artistPage);
        model.addAttribute("currentPage",  page);
        model.addAttribute("totalPages",   artistPage.getTotalPages());
        model.addAttribute("totalItems",   artistPage.getTotalElements());
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        return "artist-list";
    }

    @GetMapping("/{id}")
    public String viewArtist(@PathVariable Long id, Model model) {
        return artistService.getArtistById(id)
                .map(artist -> {
                    model.addAttribute("artist", artist);
                    return "artist-profile";
                })
                .orElse("redirect:/artists");
    }

    @GetMapping("/category/{category}")
    public String listArtistsByCategory(@PathVariable ArtistCategory category, Model model) {
        List<Artist> artists = artistService.getArtistsByCategory(category);
        model.addAttribute("artists", artists);
        model.addAttribute("category", category);
        return "artist-list";
    }

    @GetMapping("/search")
    public String searchArtists(@RequestParam String query, Model model) {
        model.addAttribute("artists", artistService.searchArtists(query));
        model.addAttribute("query", query);
        return "search-results";
    }

    /* ----------  AUTHENTICATED ARTIST PAGES  ---------- */
    @GetMapping("/new")
    public String showArtistForm(Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        if (artistService.findByUser(user) != null) {
            return "redirect:/artists/profile";
        }
        model.addAttribute("artist", new Artist());
        return "artist-form";
    }

    @GetMapping("/profile")
    public String viewMyProfile(Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        Artist artist = artistService.findByUser(user);
        if (artist == null) return "redirect:/artists/new";

        model.addAttribute("artist", artist);

        // empty object for the modal form
        Media media = new Media();
        media.setArtist(artist);
        model.addAttribute("media", media);

        // real media list for the gallery
        List<Media> mediaList = mediaService.getMediaByArtist(artist);
        model.addAttribute("mediaList", mediaList);

        return "artist-profile";
    }

    @PostMapping("/save")
    public String saveArtist(@ModelAttribute Artist artist,
                             @RequestParam(value = "profileImage", required = false) MultipartFile file,
                             Authentication auth) throws IOException {

        User user = userService.findByUsername(auth.getName());
        artist.setUser(user);

        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("uploads/");
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            artist.setProfileImage("/uploads/" + fileName);
        }
        artistService.saveArtist(artist);
        return "redirect:/artists/profile";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ARTIST')")
    public String editArtistForm(@PathVariable Long id, Model model, Authentication auth) {
        User user   = userService.findByUsername(auth.getName());
        Artist artist = artistService.getArtistById(id).orElse(null);
        if (artist == null || !artist.getUser().getId().equals(user.getId())) {
            return "redirect:/artists/profile";
        }
        model.addAttribute("artist", artist);
        return "artist-edit";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ARTIST')")
    public String updateArtist(@PathVariable Long id,
                               @RequestParam("artistName") String artistName,
                               @RequestParam("bio") String bio,
                               @RequestParam("category") String category,
                               @RequestParam(value = "instagramUrl", required = false) String instagramUrl,
                               @RequestParam(value = "youtubeUrl",  required = false) String youtubeUrl,
                               @RequestParam(value = "soundcloudUrl", required = false) String soundcloudUrl,
                               @RequestParam(value = "profileImage", required = false) MultipartFile file,
                               Authentication auth) throws IOException {

        User user     = userService.findByUsername(auth.getName());
        Artist existing = artistService.getArtistById(id).orElse(null);
        if (existing == null || !existing.getUser().getId().equals(user.getId())) {
            return "redirect:/artists/profile";
        }

        existing.setArtistName(artistName);
        existing.setBio(bio);
        existing.setCategory(ArtistCategory.valueOf(category));
        existing.setInstagramUrl(instagramUrl);
        existing.setYoutubeUrl(youtubeUrl);
        existing.setSoundcloudUrl(soundcloudUrl);

        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("uploads/");
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            existing.setProfileImage("/uploads/" + fileName);
        }
        artistService.saveArtist(existing);
        return "redirect:/artists/profile";
    }
}