package com.schoolfurniture.repository;

import com.schoolfurniture.entity.Order;
import com.schoolfurniture.entity.User;
import com.schoolfurniture.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    /**
     * Find order by order number
     */
    Optional<Order> findByOrderNumber(String orderNumber);
    
    /**
     * Find orders by user
     */
    List<Order> findByUserOrderByOrderDateDesc(User user);
    
    /**
     * Find orders by user with pagination
     */
    Page<Order> findByUserOrderByOrderDateDesc(User user, Pageable pageable);
    
    /**
     * Find orders by user ID
     */
    @Query("SELECT o FROM Order o WHERE o.user.userId = :userId ORDER BY o.orderDate DESC")
    List<Order> findByUserIdOrderByOrderDateDesc(@Param("userId") Integer userId);
    
    /**
     * Find orders by status
     */
    List<Order> findByStatusOrderByOrderDateDesc(OrderStatus status);
    
    /**
     * Find orders by status with pagination
     */
    Page<Order> findByStatusOrderByOrderDateDesc(OrderStatus status, Pageable pageable);
    
    /**
     * Find orders by user and status
     */
    List<Order> findByUserAndStatusOrderByOrderDateDesc(User user, OrderStatus status);
    
    /**
     * Find orders by date range
     */
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    List<Order> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find orders by date range with pagination
     */
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    Page<Order> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find orders by total amount range
     */
    @Query("SELECT o FROM Order o WHERE o.totalAmount BETWEEN :minAmount AND :maxAmount ORDER BY o.orderDate DESC")
    List<Order> findByTotalAmountBetween(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);
    
    /**
     * Find recent orders
     */
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findRecentOrders(Pageable pageable);
    
    /**
     * Get order statistics for admin dashboard
     */
    @Query("SELECT COUNT(o), SUM(o.totalAmount), AVG(o.totalAmount) FROM Order o")
    Object[] getOrderStatistics();
    
    /**
     * Get order statistics by status
     */
    @Query("SELECT o.status, COUNT(o), SUM(o.totalAmount) FROM Order o GROUP BY o.status")
    List<Object[]> getOrderStatisticsByStatus();
    
    /**
     * Get order statistics by date range
     */
    @Query("SELECT COUNT(o), SUM(o.totalAmount), AVG(o.totalAmount) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    Object[] getOrderStatisticsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find orders with items by user ID
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product WHERE o.user.userId = :userId ORDER BY o.orderDate DESC")
    List<Order> findOrdersWithItemsByUserId(@Param("userId") Integer userId);
    
    /**
     * Find pending orders older than specified hours
     */
    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.orderDate < :cutoffTime")
    List<Order> findPendingOrdersOlderThan(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    /**
     * Get monthly sales data
     */
    @Query("SELECT YEAR(o.orderDate), MONTH(o.orderDate), COUNT(o), SUM(o.totalAmount) " +
           "FROM Order o " +
           "WHERE o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate) " +
           "ORDER BY YEAR(o.orderDate) DESC, MONTH(o.orderDate) DESC")
    List<Object[]> getMonthlySalesData();
    
    /**
     * Get daily sales data for a specific month
     */
    @Query("SELECT DAY(o.orderDate), COUNT(o), SUM(o.totalAmount) " +
           "FROM Order o " +
           "WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month " +
           "AND o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "GROUP BY DAY(o.orderDate) " +
           "ORDER BY DAY(o.orderDate)")
    List<Object[]> getDailySalesData(@Param("year") int year, @Param("month") int month);
}