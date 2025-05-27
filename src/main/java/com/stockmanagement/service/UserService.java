package com.stockmanagement.service;

import com.stockmanagement.entity.User;
import com.stockmanagement.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @PostConstruct
    public void init() {
        // Create default admin user if not exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            logger.info("Creating default admin user");
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setFullName("Administrator");
            admin.setRole("ADMIN");
            admin.setActive(true);
            userRepository.save(admin);
            logger.info("Default admin user created successfully");
        }
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    
    @Transactional
    public User registerNewUser(User user) {
        logger.info("Registering new user: {}", user.getUsername());
        
        // Check if username already exists
        if (existsByUsername(user.getUsername())) {
            logger.warn("Username already exists: {}", user.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Check if email already exists
        if (existsByEmail(user.getEmail())) {
            logger.warn("Email already exists: {}", user.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default role and active status
        user.setRole("USER");
        user.setActive(true);
        
        // Save user
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully: {}", savedUser.getUsername());
        
        return savedUser;
    }
}
