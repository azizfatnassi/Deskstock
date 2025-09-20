package com.schoolfurniture.repository;

import com.schoolfurniture.entity.Order;
import com.schoolfurniture.entity.OrderStatus;
import com.schoolfurniture.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    // Find orders by user
    List<Order> findByUserOrderByOrderDateDesc(User user);
    
    // Find orders by user with pagination
    Page<Order> findByUserOrderByOrderDateDesc(User user, Pageable pageable);
    
    // Find orders by status
    List<Order> findByStatusOrderByOrderDateDesc(OrderStatus status);
    
    // Find orders by status with pagination
    Page<Order> findByStatusOrderByOrderDateDesc(OrderStatus status, Pageable pageable);
    
    // Find pending orders
    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' ORDER BY o.orderDate DESC")
    List<Order> findPendingOrders();
    
    // Find confirmed orders
    @Query("SELECT o FROM Order o WHERE o.status = 'CONFIRMED' ORDER BY o.orderDate DESC")
    List<Order> findConfirmedOrders();
    
    // Find orders by user and status
    List<Order> findByUserAndStatusOrderByOrderDateDesc(User user, OrderStatus status);
    
    // Find orders within date range
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    List<Order> findOrdersByDateRange(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
    // Find orders by user within date range
    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    List<Order> findOrdersByUserAndDateRange(@Param("user") User user,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
    
    // Count orders by status
    long countByStatus(OrderStatus status);
    
    // Count orders by user
    long countByUser(User user);
    
    // Count orders by user and status
    long countByUserAndStatus(User user, OrderStatus status);
    
    // Find recent orders (last N days)
    @Query("SELECT o FROM Order o WHERE o.orderDate >= :sinceDate ORDER BY o.orderDate DESC")
    List<Order> findRecentOrders(@Param("sinceDate") LocalDateTime sinceDate);
    
    // Find orders with total amount greater than specified value
    @Query("SELECT o FROM Order o WHERE o.totalAmount >= :minAmount ORDER BY o.totalAmount DESC")
    List<Order> findOrdersWithMinAmount(@Param("minAmount") java.math.BigDecimal minAmount);
    
    // Get order statistics
    @Query("SELECT COUNT(o), SUM(o.totalAmount), AVG(o.totalAmount) FROM Order o WHERE o.status = 'CONFIRMED'")
    Object[] getConfirmedOrderStatistics();
    
    // Find orders by shipping address containing text
    @Query("SELECT o FROM Order o WHERE LOWER(o.shippingAddress) LIKE LOWER(CONCAT('%', :address, '%')) ORDER BY o.orderDate DESC")
    List<Order> findOrdersByShippingAddressContaining(@Param("address") String address);
    
    // Find orders by customer name or email
    @Query("SELECT o FROM Order o WHERE LOWER(o.user.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(o.user.email) LIKE LOWER(CONCAT('%', :search, '%')) ORDER BY o.orderDate DESC")
    List<Order> findOrdersByCustomerInfo(@Param("search") String search);
    
    // Check if user has any orders
    boolean existsByUser(User user);
    
    // Check if user has confirmed orders
    boolean existsByUserAndStatus(User user, OrderStatus status);
}