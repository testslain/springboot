package com.stockmanagement.service;

import com.stockmanagement.entity.Product;
import com.stockmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;
    
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
    
    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }
    
    @Transactional
    public Product saveProduct(Product product) {
        logger.info("Saving product: {}", product);
        
        // Ensure non-null values before saving
        if (product.getQuantity() == null) {
            product.setQuantity(0);
            logger.debug("Set default quantity to 0");
        }
        
        if (product.getPrice() == null) {
            product.setPrice(new BigDecimal("0.00"));
            logger.debug("Set default price to 0.00");
        }
        
        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        
        if (product.getDescription() == null) {
            product.setDescription("");
            logger.debug("Set default description to empty string");
        }
        
        try {
            Product savedProduct = productRepository.save(product);
            logger.info("Product saved successfully with ID: {}", savedProduct.getId());
            return savedProduct;
        } catch (Exception e) {
            logger.error("Error saving product: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save product: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);
        productRepository.deleteById(id);
        logger.info("Product deleted successfully");
    }
    
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    public List<Product> findByCategory(String category) {
        return productRepository.findByCategoryContainingIgnoreCase(category);
    }
    
    public List<Product> findLowStockProducts(int threshold) {
        return productRepository.findByQuantityLessThan(threshold);
    }
    
    public List<Product> findRecentProducts(int limit) {
        logger.info("Finding {} most recent products", limit);
        List<Product> allProducts = productRepository.findAll();
        
        // Sort by createdAt in descending order and limit the results
        List<Product> recentProducts = allProducts.stream()
                .sorted(Comparator.comparing(Product::getCreatedAt).reversed())
                .limit(limit)
                .collect(Collectors.toList());
        
        logger.info("Found {} recent products", recentProducts.size());
        return recentProducts;
    }
}
