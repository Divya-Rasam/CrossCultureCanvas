package com.crossculture.canvas.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crossculture.canvas.model.User;
import com.crossculture.canvas.service.NotificationService;
import com.crossculture.canvas.service.UserService;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/notifications")
    @PreAuthorize("isAuthenticated()")
    public String notifications(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("notifications", notificationService.getUserNotifications(user.getId()));
        return "notifications";
    }

    @PostMapping("/notifications/mark-as-read/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public String markAsRead(@PathVariable Long id, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        notificationService.markAsRead(id);
        return "success";
    }

    @PostMapping("/notifications/mark-all-as-read")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public String markAllAsRead(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        notificationService.markAllAsRead(user.getId());
        return "success";
    }

    @GetMapping("/notifications/unread-count")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public Map<String, Long> getUnreadCount(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        long count = notificationService.getUnreadCount(user.getId());
        return Map.of("count", count);
    }
}