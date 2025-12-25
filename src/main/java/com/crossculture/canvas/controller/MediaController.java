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

import com.crossculture.canvas.model.Artist;
import com.crossculture.canvas.model.Media;
import com.crossculture.canvas.model.User;
import com.crossculture.canvas.service.ArtistService;
import com.crossculture.canvas.service.MediaService;
import com.crossculture.canvas.service.UserService;

@Controller
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ArtistService artistService;

    @GetMapping("/test")
    public String test() {
        return "MediaController is working!";
    }

    @GetMapping("/my-media")
    @PreAuthorize("hasRole('ARTIST')")
    public String myMedia(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Optional<Artist> artist = artistService.getArtistByUser(user);
        
        if (artist.isPresent()) {
            List<Media> mediaList = mediaService.getMediaByArtist(artist.get());
            model.addAttribute("mediaList", mediaList);
        } else {
            model.addAttribute("mediaList", List.of());
        }
        
        return "my-media";
    }

    @GetMapping("/upload")
    @PreAuthorize("hasRole('ARTIST')")
    public String uploadMediaForm(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Optional<Artist> artist = Optional.ofNullable(artistService.findByUser(user));

        if (artist.isEmpty() || artist.get() == null) {
            return "redirect:/artists/new";
        }

        Media media = new Media();
        media.setArtist(artist.get());
        model.addAttribute("media", media);
        return "media-form";
    }

@PostMapping("/save")
public String saveMedia(@ModelAttribute Media media, Principal principal) {
    User user   = userService.findByUsername(principal.getName());
    Artist artist = artistService.findByUser(user);
    if (artist == null) return "redirect:/artists/new";

    media.setArtist(artist);
    mediaService.createMedia(media);

    // ✅ AFTER saving, go back to the profile page (not /media/my-media)
    return "redirect:/artists/profile";
}

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ARTIST')")
    public String editMediaForm(@PathVariable Long id, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Optional<Artist> artist = artistService.getArtistByUser(user);
        Media media = mediaService.getMediaById(id).orElse(null);
        
        if (media != null && artist.isPresent() && media.getArtist().getId().equals(artist.get().getId())) {
            model.addAttribute("media", media);
            return "media-form";
        }
        
        return "redirect:/media/my-media";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ARTIST')")
    public String deleteMedia(@PathVariable Long id, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Optional<Artist> artist = artistService.getArtistByUser(user);
        Media media = mediaService.getMediaById(id).orElse(null);
        
        if (media != null && artist.isPresent() && media.getArtist().getId().equals(artist.get().getId())) {
            mediaService.deleteMedia(id);
        }
        
        return "redirect:/media/my-media";
    }
}