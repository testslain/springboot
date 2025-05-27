package com.stockmanagement.controller;

import com.stockmanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private ProductService productService;
    
    @GetMapping
    public String dashboard(Model model) {
        try {
            logger.info("Loading dashboard data");
            
            int totalProducts = productService.findAllProducts().size();
            int lowStockProducts = productService.findLowStockProducts(10).size();
            
            model.addAttribute("totalProducts", totalProducts);
            model.addAttribute("lowStockProducts", lowStockProducts);
            model.addAttribute("recentProducts", productService.findRecentProducts(5));
            
            logger.info("Dashboard data loaded successfully: totalProducts={}, lowStockProducts={}", 
                    totalProducts, lowStockProducts);
            
            return "dashboard";
        } catch (Exception e) {
            logger.error("Error loading dashboard: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            return "dashboard";
        }
    }
}
