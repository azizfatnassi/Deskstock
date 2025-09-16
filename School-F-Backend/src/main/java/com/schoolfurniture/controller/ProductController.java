package com.schoolfurniture.controller;

import com.schoolfurniture.entity.Product;
import com.schoolfurniture.enums.Category;
import com.schoolfurniture.enums.Color;
import com.schoolfurniture.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "http://localhost:4202"})
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    /**
     * Get all active products with pagination
     */
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productService.getAllActiveProducts(pageable);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Optional<Product> product = productService.getProductById(id);
        
        if (product.isPresent() && product.get().getIsActive()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Search products
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productService.searchProducts(query, pageable);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<Product>> getProductsByCategory(
            @PathVariable Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productService.getProductsByCategory(category, pageable);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by color
     */
    @GetMapping("/color/{color}")
    public ResponseEntity<Page<Product>> getProductsByColor(
            @PathVariable Color color,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productService.getProductsByColor(color, pageable);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by price range
     */
    @GetMapping("/price-range")
    public ResponseEntity<Page<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * Advanced search with filters
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<Product>> searchProductsWithFilters(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Color color,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productService.searchProductsWithFilters(
                query, category, color, minPrice, maxPrice, pageable);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get recently added products
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Product>> getRecentlyAddedProducts(
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(0, limit);
        List<Product> products = productService.getRecentlyAddedProducts(pageable);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get most popular products
     */
    @GetMapping("/popular")
    public ResponseEntity<List<Object[]>> getMostPopularProducts(
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> products = productService.getMostPopularProducts(pageable);
        
        return ResponseEntity.ok(products);
    }
    
    /**
     * Create new product (Admin only)
     */
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error creating product: " + e.getMessage());
        }
    }
    
    /**
     * Update product (Admin only)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @Valid @RequestBody Product productDetails) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error updating product: " + e.getMessage());
        }
    }
    
    /**
     * Delete product (Admin only - soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().body("Product deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error deleting product: " + e.getMessage());
        }
    }
    
    /**
     * Update product stock (Admin only)
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateProductStock(
            @PathVariable Integer id, 
            @RequestParam Integer stockQuantity) {
        try {
            Product updatedProduct = productService.updateProductStock(id, stockQuantity);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error updating stock: " + e.getMessage());
        }
    }
    
    /**
     * Get low stock products (Admin only)
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        List<Product> products = productService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get out of stock products (Admin only)
     */
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<Product>> getOutOfStockProducts() {
        List<Product> products = productService.getOutOfStockProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get product statistics (Admin only)
     */
    @GetMapping("/statistics")
    public ResponseEntity<Object[]> getProductStatistics() {
        Object[] statistics = productService.getProductStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * Check product availability
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<?> checkProductAvailability(
            @PathVariable Integer id,
            @RequestParam Integer quantity) {
        try {
            Optional<Product> productOpt = productService.getProductById(id);
            if (!productOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Product product = productOpt.get();
            if (!product.getIsActive()) {
                java.util.Map<String, Object> response = new HashMap<>();
                response.put("available", false);
                response.put("reason", "Product is not available");
                return ResponseEntity.ok().body(response);
            }
            
            boolean available = product.getStockQuantity() >= quantity;
            java.util.Map<String, Object> response = new HashMap<>();
            response.put("available", available);
            response.put("stockQuantity", product.getStockQuantity());
            response.put("requestedQuantity", quantity);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error checking availability: " + e.getMessage());
        }
    }
    

}