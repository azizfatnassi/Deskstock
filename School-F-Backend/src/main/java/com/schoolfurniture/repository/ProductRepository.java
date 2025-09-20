package com.schoolfurniture.repository;

import com.schoolfurniture.entity.Product;
import com.schoolfurniture.entity.OrderStatus;
// Removed enum imports - now using String fields
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    /**
     * Find active products
     */
    List<Product> findByIsActiveTrue();
    
    /**
     * Find active products with pagination
     */
    Page<Product> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find products by category
     */
    List<Product> findByCategoryAndIsActiveTrue(String category);
    
    /**
     * Find products by category with pagination
     */
    Page<Product> findByCategoryAndIsActiveTrue(String category, Pageable pageable);
    
    /**
     * Find products by color
     */
    List<Product> findByColorAndIsActiveTrue(String color);
    
    /**
     * Find products by color with pagination
     */
    Page<Product> findByColorAndIsActiveTrue(String color, Pageable pageable);
    
    /**
     * Find products by category and color
     */
    List<Product> findByCategoryAndColorAndIsActiveTrue(String category, String color);
    
    /**
     * Find products by category and color with pagination
     */
    Page<Product> findByCategoryAndColorAndIsActiveTrue(String category, String color, Pageable pageable);
    
    /**
     * Find products by price range
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.isActive = true")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    /**
     * Find products by price range with pagination
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.isActive = true")
    Page<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, Pageable pageable);
    
    /**
     * Search products by name or description (case-insensitive)
     */
    @Query("SELECT p FROM Product p WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND p.isActive = true")
    List<Product> searchProducts(@Param("searchTerm") String searchTerm);
    
    /**
     * Search products by name or description with pagination
     */
    @Query("SELECT p FROM Product p WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND p.isActive = true")
    Page<Product> searchProducts(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * Advanced search with multiple filters
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(:searchTerm IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "(:category IS NULL OR p.category = :category) AND " +
           "(:color IS NULL OR p.color = :color) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "p.isActive = true")
    Page<Product> findProductsWithFilters(
            @Param("searchTerm") String searchTerm,
            @Param("category") String category,
            @Param("color") String color,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);
    
    /**
     * Find products with low stock
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.lowStockThreshold AND p.isActive = true")
    List<Product> findLowStockProducts();
    
    /**
     * Find out of stock products
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity = 0 AND p.isActive = true")
    List<Product> findOutOfStockProducts();
    
    /**
     * Find products by stock quantity range
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity BETWEEN :minStock AND :maxStock AND p.isActive = true")
    List<Product> findByStockQuantityRange(@Param("minStock") Integer minStock, @Param("maxStock") Integer maxStock);
    
    /**
     * Find most popular products based on order quantity
     */
    @Query("SELECT p, COALESCE(SUM(oi.quantity), 0) as orderCount " +
           "FROM Product p " +
           "LEFT JOIN p.orderItems oi ON oi.order.status = 'CONFIRMED' " +
           "GROUP BY p " +
           "ORDER BY orderCount DESC")
    List<Object[]> findMostPopularProducts();
    
    /**
     * Find products by category name
     */
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.isActive = true")
    List<Product> findByCategory(@Param("category") String category);
    
    /**
     * Find products by color name
     */
    @Query("SELECT p FROM Product p WHERE p.color = :color AND p.isActive = true")
    List<Product> findByColor(@Param("color") String color);
    
    /**
     * Find product by code article
     */
    @Query("SELECT p FROM Product p WHERE p.codeArticle = :codeArticle")
    Optional<Product> findByCodeArticle(@Param("codeArticle") String codeArticle);
    
    /**
     * Get product statistics for admin dashboard
     */
    @Query("SELECT COUNT(p), AVG(p.price), SUM(p.stockQuantity) FROM Product p WHERE p.isActive = true")
    Object[] getProductStatistics();
    
    /**
     * Find recently added products
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.createdAt DESC")
    List<Product> findRecentlyAddedProducts(Pageable pageable);
    
    /**
     * Get distinct categories from active products
     */
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.isActive = true AND p.category IS NOT NULL ORDER BY p.category")
    List<String> findDistinctCategories();
    
    /**
     * Get distinct colors from active products
     */
    @Query("SELECT DISTINCT p.color FROM Product p WHERE p.isActive = true AND p.color IS NOT NULL ORDER BY p.color")
    List<String> findDistinctColors();
}