package com.schoolfurniture.service;

import com.schoolfurniture.entity.Product;
// Removed enum imports - now using String fields
import com.schoolfurniture.repository.ProductRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * Get all active products
     */
    @Transactional(readOnly = true)
    public List<Product> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }
    
    /**
     * Get all active products with pagination
     */
    @Transactional(readOnly = true)
    public Page<Product> getAllActiveProducts(Pageable pageable) {
        return productRepository.findByIsActiveTrue(pageable);
    }
    
    /**
     * Get all products (including inactive ones) with pagination - for admin use
     */
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    /**
     * Get product by ID
     */
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Integer productId) {
        return productRepository.findById(productId);
    }
    
    /**
     * Create new product
     */
    public Product createProduct(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }
    
    /**
     * Update existing product
     */
    public Product updateProduct(Integer productId, Product productDetails) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setLowStockThreshold(productDetails.getLowStockThreshold());
        product.setImageUrl(productDetails.getImageUrl());
        product.setIsActive(productDetails.getIsActive());
        product.setCategory(productDetails.getCategory());
        product.setColor(productDetails.getColor());
        
        validateProduct(product);
        return productRepository.save(product);
    }
    
    /**
     * Delete product (soft delete by setting isActive to false)
     */
    public void deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        product.setIsActive(false);
        productRepository.save(product);
    }
    
    /**
     * Search products by name or description
     */
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String searchTerm) {
        return productRepository.searchProducts(searchTerm);
    }
    
    /**
     * Search products with pagination
     */
    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String searchTerm, Pageable pageable) {
        return productRepository.searchProducts(searchTerm, pageable);
    }
    
    /**
     * Get products by category
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    /**
     * Get products by category with pagination
     */
    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategoryAndIsActiveTrue(category, pageable);
    }
    
    /**
     * Get products by color
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByColor(String color) {
        return productRepository.findByColor(color);
    }
    
    /**
     * Get products by color with pagination
     */
    @Transactional(readOnly = true)
    public Page<Product> getProductsByColor(String color, Pageable pageable) {
        return productRepository.findByColorAndIsActiveTrue(color, pageable);
    }
    
    /**
     * Get products by price range
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }
    
    /**
     * Get products by price range with pagination
     */
    @Transactional(readOnly = true)
    public Page<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceRange(minPrice, maxPrice, pageable);
    }
    
    /**
     * Advanced search with multiple filters
     */
    @Transactional(readOnly = true)
    public Page<Product> searchProductsWithFilters(String searchTerm, String category, String color, 
                                                   BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findProductsWithFilters(searchTerm, category, color, minPrice, maxPrice, pageable);
    }
    
    /**
     * Find products by category name
     */
    public List<Product> findByCategoryName(String categoryName) {
        return productRepository.findByCategory(categoryName);
    }
    
    /**
     * Find products by color name
     */
    public List<Product> findByColorName(String colorName) {
        return productRepository.findByColor(colorName);
    }
    
    /**
     * Find product by code article
     */
    public Optional<Product> findByCodeArticle(String codeArticle) {
        return productRepository.findByCodeArticle(codeArticle);
    }
    
    /**
     * Get low stock products
     */
    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }
    
    /**
     * Get out of stock products
     */
    @Transactional(readOnly = true)
    public List<Product> getOutOfStockProducts() {
        return productRepository.findOutOfStockProducts();
    }
    
    /**
     * Get most popular products
     */
    @Transactional(readOnly = true)
    public List<Object[]> getMostPopularProducts(Pageable pageable) {
        return productRepository.findMostPopularProducts();
    }
    
    /**
     * Get recently added products
     */
    @Transactional(readOnly = true)
    public List<Product> getRecentlyAddedProducts(Pageable pageable) {
        return productRepository.findRecentlyAddedProducts(pageable);
    }
    
    /**
     * Update product stock
     */
    public Product updateProductStock(Integer productId, Integer newStockQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        if (newStockQuantity < 0) {
            throw new RuntimeException("Stock quantity cannot be negative");
        }
        
        product.setStockQuantity(newStockQuantity);
        return productRepository.save(product);
    }
    
    /**
     * Reduce product stock (for order processing)
     */
    public Product reduceProductStock(Integer productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }
        
        product.setStockQuantity(product.getStockQuantity() - quantity);
        return productRepository.save(product);
    }
    
    /**
     * Increase product stock (for order cancellation or return)
     */
    public Product increaseProductStock(Integer productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        product.setStockQuantity(product.getStockQuantity() + quantity);
        return productRepository.save(product);
    }
    
    /**
     * Get product statistics for admin dashboard
     */
    @Transactional(readOnly = true)
    public Object[] getProductStatistics() {
        return productRepository.getProductStatistics();
    }
    
    /**
     * Validate product data
     */
    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new RuntimeException("Product name is required");
        }
        
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Product price must be greater than 0");
        }
        
        if (product.getStockQuantity() == null || product.getStockQuantity() < 0) {
            throw new RuntimeException("Stock quantity cannot be negative");
        }
        
        if (product.getCategory() == null) {
            throw new RuntimeException("Product category is required");
        }
        
        // Category and color validation is handled by enum constraints
    }
    
    /**
     * Get distinct categories from database
     */
    @Transactional(readOnly = true)
    public List<String> getDistinctCategories() {
        return productRepository.findDistinctCategories();
    }
    
    /**
     * Get distinct colors from database
     */
    @Transactional(readOnly = true)
    public List<String> getDistinctColors() {
        return productRepository.findDistinctColors();
    }
}