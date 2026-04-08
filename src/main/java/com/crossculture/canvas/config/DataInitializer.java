package com.crossculture.canvas.config;

import com.crossculture.canvas.model.*;
import com.crossculture.canvas.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Autowired
    private UserService userService;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private VenueService venueService;
    @Autowired
    private EventService eventService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // === ARTISTS ===
            // Rappers
            User rapper1 = createUserIfNotExists("mc_flow", "password", "mcflow@palava.com", User.Role.ARTIST);
            User rapper2 = createUserIfNotExists("lil_canvas", "password", "lil@palava.com", User.Role.ARTIST);

            // Skateboarders
            User skater1 = createUserIfNotExists("tony_palava", "password", "tony@palava.com", User.Role.ARTIST);
            User skater2 = createUserIfNotExists("sk8_goddess", "password", "sk8@palava.com", User.Role.ARTIST);

            // Graffiti artists
            User graffiti1 = createUserIfNotExists("zephyr_palava", "password", "zephyr@palava.com", User.Role.ARTIST);
            User graffiti2 = createUserIfNotExists("spray_king", "password", "spray@palava.com", User.Role.ARTIST);

            // DJs
            User dj1 = createUserIfNotExists("dj_pulse", "password", "pulse@palava.com", User.Role.ARTIST);
            User dj2 = createUserIfNotExists("beat_bender", "password", "beat@palava.com", User.Role.ARTIST);

            // === VENUE OWNERS ===
            User owner1 = createUserIfNotExists("palava_owner", "password", "owner@palava.com", User.Role.VENUE_OWNER);
            User owner2 = createUserIfNotExists("basement_boss", "password", "basement@palava.com", User.Role.VENUE_OWNER);
            User owner3 = createUserIfNotExists("urban_curator", "password", "urban@palava.com", User.Role.VENUE_OWNER);

            // === ARTIST PROFILES ===
            Artist mcFlow = createArtistIfNotExists(rapper1, "MC Flow", "Raw lyrics from the streets of Palava", ArtistCategory.RAPPER, "https://instagram.com/mcflow", "https://youtube.com/mcflow", null, "https://via.placeholder.com/400x300?text=MC+Flow");
            Artist lilCanvas = createArtistIfNotExists(rapper2, "Lil Canvas", "Young voice, big dreams", ArtistCategory.RAPPER, "https://instagram.com/lilcanvas", null, "https://soundcloud.com/lilcanvas", "https://via.placeholder.com/400x300?text=Lil+Canvas");
            Artist tonyP = createArtistIfNotExists(skater1, "Tony Palava", "Master of urban terrain", ArtistCategory.SKATEBOARDER, "https://instagram.com/tonypalava", "https://youtube.com/tonypalava", null, "https://via.placeholder.com/400x300?text=Tony+Palava");
            Artist sk8G = createArtistIfNotExists(skater2, "Sk8 Goddess", "Shredding stereotypes", ArtistCategory.SKATEBOARDER, "https://instagram.com/sk8goddess", null, null, "https://via.placeholder.com/400x300?text=Sk8+Goddess");
            Artist zephyrP = createArtistIfNotExists(graffiti1, "Zephyr Palava", "Turning walls into stories", ArtistCategory.GRAFFITI_ARTIST, "https://instagram.com/zephyrp", "https://youtube.com/zephyrp", null, "https://via.placeholder.com/400x300?text=Zephyr+Palava");
            Artist sprayK = createArtistIfNotExists(graffiti2, "Spray King", "Coloring the underground", ArtistCategory.GRAFFITI_ARTIST, "https://instagram.com/sprayking", null, null, "https://via.placeholder.com/400x300?text=Spray+King");
            Artist pulse = createArtistIfNotExists(dj1, "DJ Pulse", "Beats that move the crowd", ArtistCategory.DJ, "https://instagram.com/djpulse", null, "https://soundcloud.com/djpulse", "https://via.placeholder.com/400x300?text=DJ+Pulse");
            Artist beatBender = createArtistIfNotExists(dj2, "Beat Bender", "Twisting sounds", ArtistCategory.DJ, null, "https://youtube.com/beatbender", null, "https://via.placeholder.com/400x300?text=Beat+Bender");

            // === VENUES ===
            // Added 0.0 for bookingPrice in each call below
            Venue v1 = createVenueIfNotExists(owner1, "Palava Ground", "Outdoor amphitheatre for 500+ crowd", "Palava Ave, Mumbai", "Mumbai", "MH", "400001", 600, 100.0, 0.0, "PA System, Lights, Parking", "owner@palava.com", "+912233445566", "https://palavaground.com", "https://via.placeholder.com/400x300?text=Palava+Ground");
            Venue v2 = createVenueIfNotExists(owner2, "The Basement", "Underground club for 200 people", "Lower Parel, Mumbai", "Mumbai", "MH", "400013", 200, 80.0, 0.0, "Smoke, Lights, Bar", "basement@palava.com", "+912233445577", "https://basement.club", "https://via.placeholder.com/400x300?text=The+Basement");
            Venue v3 = createVenueIfNotExists(owner3, "Urban Canvas Gallery", "White-wall gallery for 150 guests", "Bandra, Mumbai", "Mumbai", "MH", "400050", 150, 120.0, 0.0, "Projector, AC, Security", "urban@palava.com", "+912233445588", "https://urbancanvas.in", "https://via.placeholder.com/400x300?text=Urban+Canvas");
            Venue v4 = createVenueIfNotExists(owner1, "Rooftop Vibes", "Open-air rooftop for 300 guests", "Andheri, Mumbai", "Mumbai", "MH", "400053", 300, 90.0, 0.0, "Stage, Lights, Bar, Restrooms", "rooftop@palava.com", "+912233445599", "https://rooftopvibes.in", "https://via.placeholder.com/400x300?text=Rooftop+Vibes");

            // === EVENTS ===
            Event e1 = createEventIfNotExists("Palava Hip-Hop Night", "Open-mic + headliner MC Flow", v1, LocalDateTime.now().plusDays(7), LocalDateTime.now().plusDays(7).plusHours(19), LocalDateTime.now().plusDays(7).plusHours(23), "₹200 at gate", "https://via.placeholder.com/800x400");
            Event e2 = createEventIfNotExists("Skate & Graffiti Jam", "Half-pipe demos + live wall", v2, LocalDateTime.now().plusDays(14), LocalDateTime.now().plusDays(14).plusHours(17), LocalDateTime.now().plusDays(14).plusHours(21), "Free entry", "https://via.placeholder.com/800x400");
            Event e3 = createEventIfNotExists("Canvas & Beats", "Beatbox + spray showcase", v3, LocalDateTime.now().plusDays(21), LocalDateTime.now().plusDays(21).plusHours(18), LocalDateTime.now().plusDays(21).plusHours(22), "₹150 online", "https://via.placeholder.com/800x400");

            // === BOOKINGS ===
            Booking b1 = createBookingIfNotExists(mcFlow, v1, "Headliner set", "45-min performance", LocalDateTime.now().plusDays(7), LocalDateTime.now().plusDays(7).plusHours(20), LocalDateTime.now().plusDays(7).plusHours(20).plusMinutes(45));
            bookingService.approveBooking(b1.getId());

            Booking b2 = createBookingIfNotExists(tonyP, v2, "Skate demo", "15-min half-pipe show", LocalDateTime.now().plusDays(14), LocalDateTime.now().plusDays(14).plusHours(18), LocalDateTime.now().plusDays(14).plusHours(18).plusMinutes(15));
            // leave pending

            Booking b3 = createBookingIfNotExists(zephyrP, v3, "Live wall", "Graffiti battle", LocalDateTime.now().plusDays(21), LocalDateTime.now().plusDays(21).plusHours(19), LocalDateTime.now().plusDays(21).plusHours(21));
            bookingService.approveBooking(b3.getId());

            // === MEDIA ===
            createMediaIfNotExists(mcFlow, "Palava Cypher", "Freestyle session", Media.MediaType.VIDEO, "https://www.youtube.com/embed/dQw4w9WgXcQ", null);
            createMediaIfNotExists(tonyP, "Half-Pipe Highlights", "Best tricks from Palava Ground", Media.MediaType.VIDEO, "https://www.youtube.com/embed/dQw4w9WgXcQ", null);
            createMediaIfNotExists(zephyrP, "Wall Transformation", "Time-lapse of mural", Media.MediaType.VIDEO, "https://www.youtube.com/embed/dQw4w9WgXcQ", null);
            createMediaIfNotExists(pulse, "Live DJ Set", "Underground beats", Media.MediaType.AUDIO, "https://soundcloud.com/djpulse/live-set", null);

            System.out.println("✅ Permanent dummy data seeded – 8 artists, 4 venues, 3 events, 3 bookings, 4 media");
        };
    }

    /* ---------- Helper Methods ---------- */
    private User createUserIfNotExists(String username, String password, String email, User.Role role) {
        if (userService.findByUsername(username) != null) return userService.findByUsername(username);
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        u.setEmail(email);
        u.setRole(role);
        u.setProfileImage("https://via.placeholder.com/150");
        return userService.createUser(u);
    }

    private Artist createArtistIfNotExists(User user, String name, String bio, ArtistCategory category,
                                           String ig, String yt, String sc, String imageUrl) {
        if (artistService.getArtistByUser(user).isPresent()) return artistService.getArtistByUser(user).get();
        Artist a = new Artist();
        a.setUser(user);
        a.setArtistName(name);
        a.setBio(bio);
        a.setCategory(category);
        a.setInstagramUrl(ig);
        a.setYoutubeUrl(yt);
        a.setSoundcloudUrl(sc);
        a.setProfileImage(imageUrl);
        return artistService.createArtist(a);
    }

    private Venue createVenueIfNotExists(User owner, String name, String desc, String addr, String city,
                                         String state, String zip, Integer cap, Double rate, Double bookingPrice, // Added bookingPrice param
                                         String amenities, String email, String phone, String web, String imageUrl) {
        if (!venueService.getVenuesByOwner(owner).isEmpty()) {
            return venueService.getVenuesByOwner(owner).get(0);
        }
        Venue v = new Venue();
        v.setOwner(owner);
        v.setName(name);
        v.setDescription(desc);
        v.setAddress(addr);
        v.setCity(city);
        v.setState(state);
        v.setZipCode(zip);
        v.setCapacity(cap);
        v.setHourlyRate(rate);
        v.setBookingPrice(bookingPrice); // ADDED THIS LINE
        v.setAmenities(amenities);
        v.setContactEmail(email);
        v.setContactPhone(phone);
        v.setWebsite(web);
        v.setCreatedAt(LocalDateTime.now());
        v.setProfileImage(imageUrl);
        return venueService.createVenue(v);
    }

    private Event createEventIfNotExists(String title, String desc, Venue venue, LocalDateTime date,
                                         LocalDateTime start, LocalDateTime end, String ticket, String poster) {
        Event e = new Event();
        e.setTitle(title);
        e.setDescription(desc);
        e.setVenue(venue);
        e.setEventDate(date);
        e.setStartTime(start);
        e.setEndTime(end);
        e.setTicketInfo(ticket);
        e.setPosterImage(poster);
        e.setStatus(Event.EventStatus.UPCOMING);
        e.setCreatedAt(LocalDateTime.now());
        return eventService.createEvent(e);
    }

    private Booking createBookingIfNotExists(Artist artist, Venue venue, String title, String desc,
                                             LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        Booking b = new Booking();
        b.setArtist(artist);
        b.setVenue(venue);
        b.setEventTitle(title);
        b.setEventDescription(desc);
        b.setEventDate(date);
        b.setStartTime(start);
        b.setEndTime(end);
        b.setStatus(Booking.BookingStatus.PENDING);
        b.setCreatedAt(LocalDateTime.now());
        return bookingService.createBooking(b);
    }

    private Media createMediaIfNotExists(Artist artist, String title, String desc, Media.MediaType type, String url, String thumb) {
        Media m = new Media();
        m.setArtist(artist);
        m.setTitle(title);
        m.setDescription(desc);
        m.setType(type);
        m.setUrl(url);
        m.setThumbnailUrl(thumb);
        m.setCreatedAt(LocalDateTime.now());
        return mediaService.createMedia(m);
    }
}