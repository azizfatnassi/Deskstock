package com.schoolfurniture.service;

import com.schoolfurniture.dto.CartOrderRequest;
import com.schoolfurniture.dto.CreateOrderRequest;
import com.schoolfurniture.dto.OrderResponse;
import com.schoolfurniture.entity.*;
import com.schoolfurniture.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    /**
     * Create a new order from cart items
     */
    public OrderResponse createOrderFromCart(String username, CartOrderRequest request) {
        logger.info("=== CREATING ORDER FROM CART START ===");
        logger.info("User: {}", username);
        logger.info("Request: {}", request);
        
        try {
            // Find user
            logger.debug("Finding user by email: {}", username);
            Optional<User> userOpt = userRepository.findByEmail(username);
            if (!userOpt.isPresent()) {
                logger.error("User not found with email: {}", username);
                throw new RuntimeException("User not found with email: " + username);
            }
            User user = userOpt.get();
            logger.info("Found user: ID={}, Email={}", user.getUserId(), user.getEmail());
            
            // Get user's cart
            logger.debug("Finding cart for user ID: {}", user.getUserId());
            Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> {
                    logger.error("Cart not found for user: {}", username);
                    return new RuntimeException("Cart not found for user: " + username);
                });
            logger.info("Found cart: ID={}", cart.getCartId());
            
            // Get cart items
            logger.debug("Finding cart items for cart ID: {}", cart.getCartId());
            List<CartItem> cartItems = cartItemRepository.findByCart(cart);
            logger.info("Found {} cart items", cartItems.size());
            if (cartItems.isEmpty()) {
                logger.error("Cart is empty for user: {}", username);
                throw new RuntimeException("Cart is empty");
            }
        
            // Create order
            logger.debug("Creating new order entity");
            Order order = new Order(user, BigDecimal.ZERO, request.getShippingAddress());
            order.setPhoneNumber(request.getCustomerPhone());
            order.setNotes(request.getNotes());
            order.setStatus(OrderStatus.PENDING); // Pending by default
            logger.info("Order entity created with status: {}, phone: {}", order.getStatus(), order.getPhoneNumber());
            
            // Calculate total amount first
            logger.debug("Calculating total amount and validating stock");
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (CartItem cartItem : cartItems) {
                Product product = cartItem.getProduct();
                logger.debug("Processing cart item: Product ID={}, Name={}, Quantity={}, Price={}", 
                    product.getProductId(), product.getName(), cartItem.getQuantity(), product.getPrice());
                
                // Check stock availability
                if (product.getStockQuantity() < cartItem.getQuantity()) {
                    logger.error("Insufficient stock for product: {} - Available: {}, Requested: {}", 
                        product.getName(), product.getStockQuantity(), cartItem.getQuantity());
                    throw new RuntimeException("Insufficient stock for product: " + product.getName() + 
                        ". Available: " + product.getStockQuantity() + ", Requested: " + cartItem.getQuantity());
                }
                
                // Calculate total
                BigDecimal itemSubtotal = product.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
                totalAmount = totalAmount.add(itemSubtotal);
                
                logger.debug("Added to total: {} x {} = {}, Running total: {}", product.getName(), cartItem.getQuantity(), itemSubtotal, totalAmount);
            }
            
            // Set total amount before saving
            order.setTotalAmount(totalAmount);
            logger.info("Order prepared with total amount: {}", totalAmount);
            
            // Save order with total amount
            logger.debug("Saving order to database...");
            order = orderRepository.save(order);
            logger.info("Order saved successfully with ID: {} and total: {}", order.getOrderId(), totalAmount);
        
            // Create order items from cart items
            logger.debug("Creating order items from {} cart items", cartItems.size());
            for (CartItem cartItem : cartItems) {
                Product product = cartItem.getProduct();
                logger.debug("Creating order item for product: {} (ID: {}), quantity: {}", 
                    product.getName(), product.getProductId(), cartItem.getQuantity());
                
                // Create order item
                OrderItem orderItem = new OrderItem(order, product, cartItem.getQuantity(), product.getPrice());
                order.addOrderItem(orderItem);
                logger.debug("Order item created and added to order");
            }
            
            // Save order with items
            logger.debug("Saving order with {} items to database...", order.getOrderItems().size());
            order = orderRepository.save(order);
            logger.info("Order with items saved successfully. Final order ID: {}", order.getOrderId());
        
            // Clear cart after successful order creation
            logger.debug("Clearing cart items for cart ID: {}", cart.getCartId());
            cartItemRepository.deleteByCart(cart);
            logger.info("Cart cleared for user: {}", username);
            
            logger.info("=== ORDER CREATION COMPLETED SUCCESSFULLY ===");
             logger.info("Order ID: {}, Total Amount: {}, Items: {}", order.getOrderId(), totalAmount, order.getOrderItems().size());
             return new OrderResponse(order);
             
        } catch (Exception e) {
            logger.error("=== ORDER CREATION FAILED ===");
            logger.error("Error type: {}", e.getClass().getSimpleName());
            logger.error("Error message: {}", e.getMessage());
            logger.error("Full stack trace:", e);
            
            if (e.getCause() != null) {
                logger.error("Root cause: {}", e.getCause().getClass().getSimpleName());
                logger.error("Root cause message: {}", e.getCause().getMessage());
            }
            
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        }
     }
    
    /**
     * Create order from custom request (not from cart)
     */
    public OrderResponse createCustomOrder(String username, CreateOrderRequest request) {
        logger.info("=== CREATING CUSTOM ORDER START ===");
        logger.info("User: {}", username);
        logger.info("Request: {}", request);
        
        try {
            // Find user
            logger.debug("Finding user by email: {}", username);
            Optional<User> userOpt = userRepository.findByEmail(username);
            if (!userOpt.isPresent()) {
                logger.error("User not found with email: {}", username);
                throw new RuntimeException("User not found with email: " + username);
            }
            User user = userOpt.get();
            logger.info("Found user: ID={}, Email={}", user.getUserId(), user.getEmail());
            
            // Create order
            logger.debug("Creating new order entity");
            Order order = new Order(user, BigDecimal.ZERO, request.getShippingAddress());
            order.setPhoneNumber(request.getCustomerPhone());
            order.setNotes(request.getNotes());
            order.setStatus(OrderStatus.PENDING); // Pending by default
            logger.info("Order entity created with status: {}, phone: {}", order.getStatus(), order.getPhoneNumber());
            
            // Calculate total amount first
            logger.debug("Calculating total amount and validating stock for {} items", request.getOrderItems().size());
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (CreateOrderRequest.OrderItemRequest itemRequest : request.getOrderItems()) {
                logger.debug("Processing order item: Product ID={}, Quantity={}", itemRequest.getProductId(), itemRequest.getQuantity());
                
                Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> {
                        logger.error("Product not found: {}", itemRequest.getProductId());
                        return new RuntimeException("Product not found: " + itemRequest.getProductId());
                    });
                
                logger.debug("Found product: ID={}, Name={}, Price={}, Stock={}", 
                    product.getProductId(), product.getName(), product.getPrice(), product.getStockQuantity());
                
                // Check stock availability
                if (product.getStockQuantity() < itemRequest.getQuantity()) {
                    logger.error("Insufficient stock for product: {} - Available: {}, Requested: {}", 
                        product.getName(), product.getStockQuantity(), itemRequest.getQuantity());
                    throw new RuntimeException("Insufficient stock for product: " + product.getName() + 
                        ". Available: " + product.getStockQuantity() + ", Requested: " + itemRequest.getQuantity());
                }
                
                // Calculate total
                BigDecimal itemSubtotal = product.getPrice().multiply(new BigDecimal(itemRequest.getQuantity()));
                totalAmount = totalAmount.add(itemSubtotal);
                
                logger.debug("Added to custom order total: {} x {} = {}, Running total: {}", 
                    product.getName(), itemRequest.getQuantity(), itemSubtotal, totalAmount);
            }
        
            // Set total amount before saving
            order.setTotalAmount(totalAmount);
            logger.info("Order prepared with total amount: {}", totalAmount);
            
            // Save order with total amount
            logger.debug("Saving order to database...");
            order = orderRepository.save(order);
            logger.info("Order saved successfully with ID: {} and total: {}", order.getOrderId(), totalAmount);
            
            // Create order items from request
            logger.debug("Creating order items from {} request items", request.getOrderItems().size());
            for (CreateOrderRequest.OrderItemRequest itemRequest : request.getOrderItems()) {
                Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.getProductId()));
                
                logger.debug("Creating order item for product: {} (ID: {}), quantity: {}", 
                    product.getName(), product.getProductId(), itemRequest.getQuantity());
                
                // Create order item
                OrderItem orderItem = new OrderItem(order, product, itemRequest.getQuantity(), product.getPrice());
                order.addOrderItem(orderItem);
                logger.debug("Order item created and added to order");
            }
            
            // Save order with items
            logger.debug("Saving order with {} items to database...", order.getOrderItems().size());
            order = orderRepository.save(order);
            
            logger.info("=== CUSTOM ORDER CREATION COMPLETED SUCCESSFULLY ===");
            logger.info("Order ID: {}, Total Amount: {}, Items: {}", order.getOrderId(), totalAmount, order.getOrderItems().size());
            return new OrderResponse(order);
            
        } catch (Exception e) {
            logger.error("=== CUSTOM ORDER CREATION FAILED ===");
            logger.error("Error type: {}", e.getClass().getSimpleName());
            logger.error("Error message: {}", e.getMessage());
            logger.error("Full stack trace:", e);
            
            if (e.getCause() != null) {
                logger.error("Root cause: {}", e.getCause().getClass().getSimpleName());
                logger.error("Root cause message: {}", e.getCause().getMessage());
            }
            
            throw new RuntimeException("Failed to create custom order: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get order by ID
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Integer orderId) {
        logger.debug("Fetching order by ID: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        
        return new OrderResponse(order);
    }
    
    /**
     * Get orders by user
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(String username) {
        logger.debug("Fetching orders for user: {}", username);
        
        Optional<User> userOpt = userRepository.findByEmail(username);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found with email: " + username);
        }
        User user = userOpt.get();
        
        List<Order> orders = orderRepository.findByUserOrderByOrderDateDesc(user);
        return orders.stream()
            .map(OrderResponse::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all orders (admin)
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        logger.debug("Fetching all orders");
        
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
            .map(OrderResponse::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get pending orders (admin)
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getPendingOrders() {
        logger.debug("Fetching pending orders");
        
        List<Order> orders = orderRepository.findPendingOrders();
        return orders.stream()
            .map(OrderResponse::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get confirmed orders (admin)
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getConfirmedOrders() {
        logger.debug("Fetching confirmed orders");
        
        List<Order> orders = orderRepository.findConfirmedOrders();
        return orders.stream()
            .map(OrderResponse::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Confirm order (admin)
     */
    public OrderResponse confirmOrder(Integer orderId) {
        logger.info("Confirming order: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new RuntimeException("Order is already confirmed: " + orderId);
        }
        
        // Update product stock quantities
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            int newStock = product.getStockQuantity() - orderItem.getQuantity();
            
            if (newStock < 0) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName() + 
                    ". Available: " + product.getStockQuantity() + ", Required: " + orderItem.getQuantity());
            }
            
            product.setStockQuantity(newStock);
            productRepository.save(product);
            
            logger.debug("Updated stock for product {}: {} -> {}", product.getName(), 
                product.getStockQuantity() + orderItem.getQuantity(), newStock);
        }
        
        // Confirm order
        order.setStatus(OrderStatus.CONFIRMED);
        order = orderRepository.save(order);
        
        logger.info("Order confirmed successfully: {}", orderId);
        return new OrderResponse(order);
    }
    
    /**
     * Cancel order (admin or user if pending)
     */
    public void cancelOrder(Integer orderId, String username, boolean isAdmin) {
        logger.info("Cancelling order: {} by user: {} (admin: {})", orderId, username, isAdmin);
        
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        
        // Check permissions
        if (!isAdmin && !order.getUser().getEmail().equals(username)) {
            throw new RuntimeException("Access denied: You can only cancel your own orders");
        }
        
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new RuntimeException("Cannot cancel confirmed order: " + orderId);
        }
        
        // Delete order (cascade will delete order items)
        orderRepository.delete(order);
        
        logger.info("Order cancelled successfully: {}", orderId);
    }
    
    /**
     * Get order statistics
     */
    @Transactional(readOnly = true)
    public OrderStatistics getOrderStatistics() {
        logger.debug("Fetching order statistics");
        
        long totalOrders = orderRepository.count();
        long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING);
        long confirmedOrders = orderRepository.countByStatus(OrderStatus.CONFIRMED);
        
        Object[] stats = orderRepository.getConfirmedOrderStatistics();
        BigDecimal totalRevenue = stats.length > 1 && stats[1] != null ? (BigDecimal) stats[1] : BigDecimal.ZERO;
        BigDecimal averageOrderValue = stats.length > 2 && stats[2] != null ? (BigDecimal) stats[2] : BigDecimal.ZERO;
        
        return new OrderStatistics(totalOrders, pendingOrders, confirmedOrders, totalRevenue, averageOrderValue);
    }
    
    // Inner class for order statistics
    public static class OrderStatistics {
        private long totalOrders;
        private long pendingOrders;
        private long confirmedOrders;
        private BigDecimal totalRevenue;
        private BigDecimal averageOrderValue;
        
        public OrderStatistics(long totalOrders, long pendingOrders, long confirmedOrders, 
                             BigDecimal totalRevenue, BigDecimal averageOrderValue) {
            this.totalOrders = totalOrders;
            this.pendingOrders = pendingOrders;
            this.confirmedOrders = confirmedOrders;
            this.totalRevenue = totalRevenue;
            this.averageOrderValue = averageOrderValue;
        }
        
        // Getters
        public long getTotalOrders() { return totalOrders; }
        public long getPendingOrders() { return pendingOrders; }
        public long getConfirmedOrders() { return confirmedOrders; }
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public BigDecimal getAverageOrderValue() { return averageOrderValue; }
    }
}