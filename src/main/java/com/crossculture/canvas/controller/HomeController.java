package com.crossculture.canvas.controller;

import com.crossculture.canvas.model.User;
import com.crossculture.canvas.service.NotificationService;
import com.crossculture.canvas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            // Redirect authenticated users to dashboard
            return "redirect:/dashboard";
        }
        return "index";
    }
    
    @GetMapping("/about")
public String about() {
    return "about";
}
}