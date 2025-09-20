package com.schoolfurniture.repository;

import com.schoolfurniture.entity.Order;
import com.schoolfurniture.entity.OrderItem;
import com.schoolfurniture.entity.OrderStatus;
import com.schoolfurniture.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    
    // Find order items by order
    List<OrderItem> findByOrder(Order order);
    
    // Find order items by order ID
    List<OrderItem> findByOrderOrderId(Integer orderId);
    
    // Find order items by product
    List<OrderItem> findByProduct(Product product);
    
    // Find order items by product ID
    List<OrderItem> findByProductProductId(Integer productId);
    
    // Find order item by order and product
    Optional<OrderItem> findByOrderAndProduct(Order order, Product product);
    
    // Count order items by order
    long countByOrder(Order order);
    
    // Count order items by product
    long countByProduct(Product product);
    
    // Get total quantity for a product across all orders
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.product = :product")
    Long getTotalQuantityByProduct(@Param("product") Product product);
    
    // Get total quantity for a product in confirmed orders only
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.product = :product AND oi.order.status = 'CONFIRMED'")
    Long getConfirmedQuantityByProduct(@Param("product") Product product);
    
    // Get total revenue for a product
    @Query("SELECT COALESCE(SUM(oi.quantity * oi.unitPrice), 0) FROM OrderItem oi WHERE oi.product = :product AND oi.order.status = 'CONFIRMED'")
    BigDecimal getTotalRevenueByProduct(@Param("product") Product product);
    
    // Find most popular products (by quantity sold)
    @Query("SELECT oi.product, SUM(oi.quantity) as totalQuantity " +
           "FROM OrderItem oi " +
           "WHERE oi.order.status = 'CONFIRMED' " +
           "GROUP BY oi.product " +
           "ORDER BY totalQuantity DESC")
    List<Object[]> findMostPopularProducts();
    
    // Find most popular products with limit
    @Query(value = "SELECT oi.product_id, p.name, SUM(oi.quantity) as total_quantity " +
                   "FROM order_items oi " +
                   "JOIN products p ON oi.product_id = p.product_id " +
                   "JOIN orders o ON oi.order_id = o.order_id " +
                   "WHERE o.status = 'CONFIRMED' " +
                   "GROUP BY oi.product_id, p.name " +
                   "ORDER BY total_quantity DESC " +
                   "LIMIT :limit", nativeQuery = true)
    List<Object[]> findTopSellingProducts(@Param("limit") int limit);
    
    // Find highest revenue products
    @Query("SELECT oi.product, SUM(oi.quantity * oi.unitPrice) as totalRevenue " +
           "FROM OrderItem oi " +
           "WHERE oi.order.status = 'CONFIRMED' " +
           "GROUP BY oi.product " +
           "ORDER BY totalRevenue DESC")
    List<Object[]> findHighestRevenueProducts();
    
    // Get order items with subtotal greater than specified amount
    @Query("SELECT oi FROM OrderItem oi WHERE (oi.quantity * oi.unitPrice) >= :minSubtotal ORDER BY (oi.quantity * oi.unitPrice) DESC")
    List<OrderItem> findOrderItemsWithMinSubtotal(@Param("minSubtotal") BigDecimal minSubtotal);
    
    // Find order items by product category
    @Query("SELECT oi FROM OrderItem oi WHERE oi.productCategory = :category")
    List<OrderItem> findByProductCategory(@Param("category") String category);
    
    // Get category sales statistics
    @Query("SELECT oi.productCategory, COUNT(oi), SUM(oi.quantity), SUM(oi.quantity * oi.unitPrice) " +
           "FROM OrderItem oi " +
           "WHERE oi.order.status = 'CONFIRMED' " +
           "GROUP BY oi.productCategory " +
           "ORDER BY SUM(oi.quantity * oi.unitPrice) DESC")
    List<Object[]> getCategorySalesStatistics();
    
    // Check if product has been ordered
    boolean existsByProduct(Product product);
    
    // Check if product has been ordered in confirmed orders
    @Query("SELECT COUNT(oi) > 0 FROM OrderItem oi WHERE oi.product = :product AND oi.order.status = 'CONFIRMED'")
    boolean existsByProductInConfirmedOrders(@Param("product") Product product);
    
    // Delete order items by order (useful for cascade operations)
    void deleteByOrder(Order order);
    
    // Get average order item quantity for a product
    @Query("SELECT AVG(oi.quantity) FROM OrderItem oi WHERE oi.product = :product")
    Double getAverageQuantityByProduct(@Param("product") Product product);
}