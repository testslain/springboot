package com.stockmanagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model, RedirectAttributes redirectAttributes) {
        logger.error("Unhandled exception occurred: {}", e.getMessage(), e);
        
        // For AJAX requests or API endpoints, you might want to return a different response
        
        // Add error message to the model for display
        model.addAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
        
        // Return to error page
        return "error";
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        logger.error("Validation error: {}", e.getMessage(), e);
        
        model.addAttribute("errorMessage", e.getMessage());
        
        // Return to the form page to show the error
        return "products/form";
    }
}
