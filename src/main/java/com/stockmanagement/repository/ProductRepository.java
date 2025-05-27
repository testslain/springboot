package com.stockmanagement.repository;

import com.stockmanagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategoryContainingIgnoreCase(String category);
    List<Product> findByQuantityLessThan(Integer quantity);
}
