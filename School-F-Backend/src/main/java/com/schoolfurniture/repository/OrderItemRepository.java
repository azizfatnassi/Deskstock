package com.schoolfurniture.repository;

import com.schoolfurniture.entity.Order;
import com.schoolfurniture.entity.OrderItem;
import com.schoolfurniture.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    
    /**
     * Find order items by order
     */
    List<OrderItem> findByOrder(Order order);
    
    /**
     * Find order items by product
     */
    List<OrderItem> findByProduct(Product product);
    
    /**
     * Find order items by order ID
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId")
    List<OrderItem> findByOrderId(@Param("orderId") Integer orderId);
    
    /**
     * Find order items by product ID
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.product.productId = :productId")
    List<OrderItem> findByProductId(@Param("productId") Integer productId);
    
    /**
     * Get most sold products
     */
    @Query("SELECT oi.product, SUM(oi.quantity) as totalSold " +
           "FROM OrderItem oi " +
           "JOIN oi.order o " +
           "WHERE o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "GROUP BY oi.product " +
           "ORDER BY totalSold DESC")
    List<Object[]> findMostSoldProducts(Pageable pageable);
    
    /**
     * Get most sold products in date range
     */
    @Query("SELECT oi.product, SUM(oi.quantity) as totalSold " +
           "FROM OrderItem oi " +
           "JOIN oi.order o " +
           "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
           "AND o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "GROUP BY oi.product " +
           "ORDER BY totalSold DESC")
    List<Object[]> findMostSoldProductsInDateRange(
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate, 
            Pageable pageable);
    
    /**
     * Get total quantity sold for a product
     */
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi " +
           "JOIN oi.order o " +
           "WHERE oi.product = :product " +
           "AND o.status NOT IN ('CANCELLED', 'REFUNDED')")
    Long getTotalQuantitySoldForProduct(@Param("product") Product product);
    
    /**
     * Get revenue by product
     */
    @Query("SELECT oi.product, SUM(oi.quantity * oi.unitPrice) as revenue " +
           "FROM OrderItem oi " +
           "JOIN oi.order o " +
           "WHERE o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "GROUP BY oi.product " +
           "ORDER BY revenue DESC")
    List<Object[]> getRevenueByProduct(Pageable pageable);
    
    /**
     * Get sales data by category
     */
    @Query("SELECT p.category, SUM(oi.quantity) as totalSold, SUM(oi.quantity * oi.unitPrice) as revenue " +
           "FROM OrderItem oi " +
           "JOIN oi.product p " +
           "JOIN oi.order o " +
           "WHERE o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "GROUP BY p.category " +
           "ORDER BY revenue DESC")
    List<Object[]> getSalesDataByCategory();
    
    /**
     * Get sales data by color
     */
    @Query("SELECT p.color, SUM(oi.quantity) as totalSold, SUM(oi.quantity * oi.unitPrice) as revenue " +
           "FROM OrderItem oi " +
           "JOIN oi.product p " +
           "JOIN oi.order o " +
           "WHERE o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "AND p.color IS NOT NULL " +
           "GROUP BY p.color " +
           "ORDER BY revenue DESC")
    List<Object[]> getSalesDataByColor();
    
    /**
     * Find order items by user ID
     */
    @Query("SELECT oi FROM OrderItem oi " +
           "JOIN oi.order o " +
           "WHERE o.user.userId = :userId " +
           "ORDER BY o.orderDate DESC")
    List<OrderItem> findByUserId(@Param("userId") Integer userId);
    
    /**
     * Get product performance metrics
     */
    @Query("SELECT oi.product, " +
           "COUNT(DISTINCT oi.order) as orderCount, " +
           "SUM(oi.quantity) as totalQuantity, " +
           "AVG(oi.quantity) as avgQuantityPerOrder, " +
           "SUM(oi.quantity * oi.unitPrice) as totalRevenue " +
           "FROM OrderItem oi " +
           "JOIN oi.order o " +
           "WHERE o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "GROUP BY oi.product")
    List<Object[]> getProductPerformanceMetrics();
}