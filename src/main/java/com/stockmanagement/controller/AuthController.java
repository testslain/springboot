package com.stockmanagement.controller;

import com.stockmanagement.entity.User;
import com.stockmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, 
                              BindingResult result, 
                              Model model,
                              RedirectAttributes redirectAttributes) {
        logger.info("Processing user registration: {}", user.getUsername());
        
        // Check for validation errors
        if (result.hasErrors()) {
            logger.warn("Validation errors in registration form: {}", result.getAllErrors());
            return "register";
        }
        
        try {
            // Register the new user
            userService.registerNewUser(user);
            
            // Add success message
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Registration successful! You can now log in with your credentials.");
            
            logger.info("User registered successfully: {}", user.getUsername());
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            // Handle specific registration errors
            logger.error("Registration error: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        } catch (Exception e) {
            // Handle unexpected errors
            logger.error("Unexpected error during registration: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            return "register";
        }
    }
}
