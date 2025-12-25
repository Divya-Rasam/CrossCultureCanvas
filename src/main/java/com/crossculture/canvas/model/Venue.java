package com.crossculture.canvas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Entity
@Table(name = "venues")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
private Double bookingPrice; // INR

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String address;

    private String city;
    private String state;
    private String zipCode;

    private Integer capacity;
    private Double hourlyRate;

    @Column(columnDefinition = "TEXT")
    private String amenities;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private String contactEmail;
    private String contactPhone;
    private String website;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    // ✅ ADD THIS LINE
    @Column
    private String profileImage;
}