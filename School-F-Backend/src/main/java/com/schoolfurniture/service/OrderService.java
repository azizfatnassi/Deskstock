package com.schoolfurniture.service;

import com.schoolfurniture.entity.*;
import com.schoolfurniture.enums.OrderStatus;
import com.schoolfurniture.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private ProductService productService;
    
    /**
     * Create order from cart
     */
    public Order createOrderFromCart(Integer userId, String shippingAddress, String shippingCity, 
                                   String shippingPostalCode, String phoneNumber, String notes) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
        
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot create order from empty cart");
        }
        
        // Validate cart items before creating order
        List<String> validationErrors = cartService.validateCartItems(userId);
        if (!validationErrors.isEmpty()) {
            throw new RuntimeException("Cart validation failed: " + String.join(", ", validationErrors));
        }
        
        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setShippingCity(shippingCity);
        order.setShippingPostalCode(shippingPostalCode);
        order.setPhoneNumber(phoneNumber);
        order.setNotes(notes);
        order.setStatus(OrderStatus.PENDING);
        
        // Save order to get ID for order number generation
        order = orderRepository.save(order);
        
        // Create order items and reduce stock
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            
            // Reduce product stock
            productService.reduceProductStock(product.getProductId(), cartItem.getQuantity());
            
            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setProductName(product.getName());
            orderItem.setProductDescription(product.getDescription());
            
            orderItemRepository.save(orderItem);
            
            totalAmount = totalAmount.add(orderItem.getSubtotal());
        }
        
        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);
        
        // Clear cart after successful order creation
        cartService.clearCart(userId);
        
        return order;
    }
    
    /**
     * Get order by ID
     */
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Integer orderId) {
        return orderRepository.findById(orderId);
    }
    
    /**
     * Get order by order number
     */
    @Transactional(readOnly = true)
    public Optional<Order> getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
    
    /**
     * Get orders by user
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
    
    /**
     * Get orders by user with pagination
     */
    @Transactional(readOnly = true)
    public Page<Order> getOrdersByUser(Integer userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return orderRepository.findByUserOrderByOrderDateDesc(user, pageable);
    }
    
    /**
     * Get all orders with pagination
     */
    @Transactional(readOnly = true)
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
    
    /**
     * Get orders by status
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatusOrderByOrderDateDesc(status);
    }
    
    /**
     * Get orders by status with pagination
     */
    @Transactional(readOnly = true)
    public Page<Order> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatusOrderByOrderDateDesc(status, pageable);
    }
    
    /**
     * Update order status
     */
    public Order updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        OrderStatus currentStatus = order.getStatus();
        
        // Validate status transition
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new RuntimeException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
        
        order.setStatus(newStatus);
        
        // Set specific dates based on status
        if (newStatus == OrderStatus.SHIPPED) {
            order.setShippedDate(LocalDateTime.now());
        } else if (newStatus == OrderStatus.DELIVERED) {
            order.setDeliveredDate(LocalDateTime.now());
            if (order.getShippedDate() == null) {
                order.setShippedDate(LocalDateTime.now());
            }
        }
        
        return orderRepository.save(order);
    }
    
    /**
     * Cancel order
     */
    public Order cancelOrder(Integer orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        // Check if order can be cancelled
        if (order.getStatus() == OrderStatus.SHIPPED || 
            order.getStatus() == OrderStatus.DELIVERED || 
            order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }
        
        // Restore product stock
        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
        for (OrderItem item : orderItems) {
            productService.increaseProductStock(item.getProduct().getProductId(), item.getQuantity());
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        if (reason != null && !reason.trim().isEmpty()) {
            order.setNotes(order.getNotes() != null ? 
                          order.getNotes() + "\nCancellation reason: " + reason : 
                          "Cancellation reason: " + reason);
        }
        
        return orderRepository.save(order);
    }
    
    /**
     * Get order items by order ID
     */
    @Transactional(readOnly = true)
    public List<OrderItem> getOrderItems(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        return orderItemRepository.findByOrder(order);
    }
    
    /**
     * Get recent orders
     */
    @Transactional(readOnly = true)
    public List<Order> getRecentOrders(Pageable pageable) {
        return orderRepository.findRecentOrders(pageable);
    }
    
    /**
     * Get orders by date range
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
    
    /**
     * Get orders by total amount range
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByTotalAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        return orderRepository.findByTotalAmountBetween(minAmount, maxAmount);
    }
    
    /**
     * Get order statistics
     */
    @Transactional(readOnly = true)
    public Object[] getOrderStatistics() {
        return orderRepository.getOrderStatistics();
    }
    
    /**
     * Get monthly sales data
     */
    @Transactional(readOnly = true)
    public List<Object[]> getMonthlySalesData(int year) {
        return orderRepository.getMonthlySalesData();
    }
    
    /**
     * Get daily sales data for a month
     */
    @Transactional(readOnly = true)
    public List<Object[]> getDailySalesData(int year, int month) {
        return orderRepository.getDailySalesData(year, month);
    }
    
    /**
     * Get total revenue
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue() {
        List<Order> completedOrders = orderRepository.findByStatusOrderByOrderDateDesc(OrderStatus.DELIVERED);
        return completedOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Get revenue by date range
     */
    @Transactional(readOnly = true)
    public BigDecimal getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
        return orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Update order shipping information
     */
    public Order updateShippingInfo(Integer orderId, String shippingAddress, String shippingCity, 
                                  String shippingPostalCode, String phoneNumber) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        // Only allow updates for pending or confirmed orders
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new RuntimeException("Cannot update shipping info for order with status: " + order.getStatus());
        }
        
        order.setShippingAddress(shippingAddress);
        order.setShippingCity(shippingCity);
        order.setShippingPostalCode(shippingPostalCode);
        order.setPhoneNumber(phoneNumber);
        
        return orderRepository.save(order);
    }
    
    /**
     * Validate status transition
     */
    private boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        switch (currentStatus) {
            case PENDING:
                return newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.CANCELLED;
            case CONFIRMED:
                return newStatus == OrderStatus.PROCESSING || newStatus == OrderStatus.CANCELLED;
            case PROCESSING:
                return newStatus == OrderStatus.SHIPPED || newStatus == OrderStatus.CANCELLED;
            case SHIPPED:
                return newStatus == OrderStatus.DELIVERED;
            case DELIVERED:
                return newStatus == OrderStatus.REFUNDED;
            case CANCELLED:
            case REFUNDED:
                return false; // Terminal states
            default:
                return false;
        }
    }
}