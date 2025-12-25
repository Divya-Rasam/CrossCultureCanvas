package com.crossculture.canvas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.crossculture.canvas.model.User;
import com.crossculture.canvas.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // @GetMapping("/register")
    // public String showRegistrationForm(Model model) {
    //     model.addAttribute("registrationDto", new RegistrationDto());
    //     return "register";
    // }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                            BindingResult result,
                            @RequestParam("confirmPassword") String confirmPassword,
                            Model model) {

        if (!user.getPassword().equals(confirmPassword)) {
            result.rejectValue("password", "error.user", "Passwords do not match");
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            result.rejectValue("email", "error.user", "Email already registered");
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            result.rejectValue("username", "error.user", "Username already taken");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

        userService.createUser(user);

        return "redirect:/login?registered";
    }
}