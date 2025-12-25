package com.crossculture.canvas.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "artists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String artistName;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String instagramUrl;
    private String youtubeUrl;
    private String soundcloudUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArtistCategory category;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Media> media;

    @Column
    private String profileImage;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @AssertTrue(message = "At least one social URL must be provided")
    public boolean isSocialValid() {
        return (instagramUrl != null && !instagramUrl.isBlank()) ||
            (youtubeUrl != null && !youtubeUrl.isBlank()) ||
            (soundcloudUrl != null && !soundcloudUrl.isBlank());
    }
}