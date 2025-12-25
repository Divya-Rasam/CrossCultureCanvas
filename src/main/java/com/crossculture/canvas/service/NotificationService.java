package com.crossculture.canvas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crossculture.canvas.model.Notification;
import com.crossculture.canvas.model.User;
import com.crossculture.canvas.repository.NotificationRepository; // Add this import

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    public Notification createNotification(Notification notification) {
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }
        return notificationRepository.save(notification);
    }

    public Notification updateNotification(Notification notification) {
        return notificationRepository.save(notification);
    }



    public Notification createPaymentReceivedNotification(User venueOwner, Double amount) {
    Notification n = new Notification();
    n.setUser(venueOwner);
    n.setMessage("₹ " + amount + " received for a booking.");
    n.setType(Notification.NotificationType.SYSTEM);
    return createNotification(n);
}


    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    public Notification markAsRead(Long id) {
        Optional<Notification> notification = getNotificationById(id);
        if (notification.isPresent()) {
            notification.get().setRead(true);
            return updateNotification(notification.get());
        }
        return null;
    }

    public Notification markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = getUnreadNotifications(userId);
        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
            updateNotification(notification);
        }
        return null; // Return value not important for this method
    }

    // Helper methods to create notifications
    public Notification createBookingApprovedNotification(User user, String venueName, String eventTitle) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage("Your booking request for '" + eventTitle + "' at " + venueName + " has been approved!");
        notification.setType(Notification.NotificationType.BOOKING_APPROVED);
        return createNotification(notification);
    }

    public Notification createBookingRejectedNotification(User user, String venueName, String eventTitle) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage("Your booking request for '" + eventTitle + "' at " + venueName + " has been rejected.");
        notification.setType(Notification.NotificationType.BOOKING_REJECTED);
        return createNotification(notification);
    }

    public Notification createNewBookingNotification(User venueOwner, String artistName, String eventTitle) {
        Notification notification = new Notification();
        notification.setUser(venueOwner);
        notification.setMessage("New booking request from " + artistName + " for '" + eventTitle + "'");
        notification.setType(Notification.NotificationType.NEW_BOOKING);
        return createNotification(notification);
    }
}