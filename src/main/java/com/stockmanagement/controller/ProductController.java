package com.stockmanagement.controller;

import com.stockmanagement.entity.Product;
import com.stockmanagement.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;
    
    @GetMapping
    public String listProducts(Model model, @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("products", productService.searchProducts(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("products", productService.findAllProducts());
        }
        return "products/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Initialize a new Product with default values to prevent null pointer exceptions
        Product product = new Product();
        product.setQuantity(0);
        product.setPrice(new BigDecimal("0.00"));
        product.setCategory("");
        product.setDescription("");
        model.addAttribute("product", product);
        return "products/form";
    }
    
    @PostMapping
    public String saveProduct(@Valid @ModelAttribute("product") Product product, 
                              BindingResult result, 
                              Model model,
                              RedirectAttributes redirectAttributes) {
        logger.info("Attempting to save product: {}", product);
        
        if (result.hasErrors()) {
            logger.error("Validation errors: {}", result.getAllErrors());
            return "products/form";
        }
        
        try {
            Product savedProduct = productService.saveProduct(product);
            logger.info("Product saved successfully: {}", savedProduct);
            redirectAttributes.addFlashAttribute("successMessage", "Product saved successfully!");
            return "redirect:/products";
        } catch (Exception e) {
            logger.error("Error saving product: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Error saving product: " + e.getMessage());
            return "products/form";
        }
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        logger.info("Attempting to edit product with ID: {}", id);
        
        try {
            Optional<Product> productOpt = productService.findProductById(id);
            
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                logger.info("Found product to edit: {}", product);
                model.addAttribute("product", product);
                return "products/form";
            } else {
                logger.warn("Product not found with ID: {}", id);
                redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
                return "redirect:/products";
            }
        } catch (Exception e) {
            logger.error("Error retrieving product for edit: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error retrieving product: " + e.getMessage());
            return "redirect:/products";
        }
    }
    
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> product = productService.findProductById(id);
        
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "products/view";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
            return "redirect:/products";
        }
    }
    
    @GetMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception e) {
            logger.error("Error deleting product: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete product: " + e.getMessage());
        }
        return "redirect:/products";
    }
}
