package com.schoolfurniture.controller;

import com.schoolfurniture.dto.CartOrderRequest;
import com.schoolfurniture.dto.CreateOrderRequest;
import com.schoolfurniture.dto.OrderResponse;
import com.schoolfurniture.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    
    @Autowired
    private OrderService orderService;
    
    /**
     * Get user's orders
     */
    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<?> getMyOrders(Authentication authentication) {
        try {
            logger.debug("Fetching orders for user: {}", authentication.getName());
            
            List<OrderResponse> orders = orderService.getOrdersByUser(authentication.getName());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orders", orders);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching orders for user {}: {}", authentication.getName(), e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to fetch orders: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Create order from cart
     */
    @PostMapping("/from-cart")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<?> createOrderFromCart(@Valid @RequestBody CartOrderRequest request, 
                                               Authentication authentication) {
        try {
            logger.info("Creating order from cart for user: {}", authentication.getName());
            
            OrderResponse orderResponse = orderService.createOrderFromCart(authentication.getName(), request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order created successfully from cart");
            response.put("order", orderResponse);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            logger.error("Error creating order from cart for user {}: {}", authentication.getName(), e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to create order: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Create custom order
     */
    @PostMapping("/custom")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<?> createCustomOrder(@Valid @RequestBody CreateOrderRequest request, 
                                             Authentication authentication) {
        try {
            logger.info("Creating custom order for user: {}", authentication.getName());
            
            OrderResponse orderResponse = orderService.createCustomOrder(authentication.getName(), request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Custom order created successfully");
            response.put("order", orderResponse);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            logger.error("Error creating custom order for user {}: {}", authentication.getName(), e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to create custom order: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Get order by ID
     */
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<?> getOrderById(@PathVariable Integer orderId, Authentication authentication) {
        try {
            logger.debug("Fetching order {} for user: {}", orderId, authentication.getName());
            
            OrderResponse orderResponse = orderService.getOrderById(orderId);
            
            // Check if user can access this order (admin can access all, user can access only their own)
            boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            
            if (!isAdmin && !orderResponse.getUserName().equals(authentication.getName())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Access denied: You can only view your own orders");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("order", orderResponse);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching order {} for user {}: {}", orderId, authentication.getName(), e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to fetch order: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * Get all orders (admin only)
     */
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllOrders() {
        try {
            logger.debug("Admin fetching all orders");
            
            List<OrderResponse> orders = orderService.getAllOrders();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orders", orders);
            response.put("count", orders.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching all orders: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to fetch orders: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get pending orders (admin only)
     */
    @GetMapping("/admin/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPendingOrders() {
        try {
            logger.debug("Admin fetching pending orders");
            
            List<OrderResponse> orders = orderService.getPendingOrders();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orders", orders);
            response.put("count", orders.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching pending orders: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to fetch pending orders: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get confirmed orders (admin only)
     */
    @GetMapping("/admin/confirmed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getConfirmedOrders() {
        try {
            logger.debug("Admin fetching confirmed orders");
            
            List<OrderResponse> orders = orderService.getConfirmedOrders();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orders", orders);
            response.put("count", orders.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching confirmed orders: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to fetch confirmed orders: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Confirm order (admin only)
     */
    @PutMapping("/admin/{orderId}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> confirmOrder(@PathVariable Integer orderId) {
        try {
            logger.info("Admin confirming order: {}", orderId);
            
            OrderResponse orderResponse = orderService.confirmOrder(orderId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order confirmed successfully");
            response.put("order", orderResponse);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error confirming order {}: {}", orderId, e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to confirm order: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Cancel order
     */
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer orderId, Authentication authentication) {
        try {
            logger.info("Cancelling order {} by user: {}", orderId, authentication.getName());
            
            boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            
            orderService.cancelOrder(orderId, authentication.getName(), isAdmin);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order cancelled successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error cancelling order {} by user {}: {}", orderId, authentication.getName(), e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to cancel order: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Get order statistics (admin only)
     */
    @GetMapping("/admin/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getOrderStatistics() {
        try {
            logger.debug("Admin fetching order statistics");
            
            OrderService.OrderStatistics stats = orderService.getOrderStatistics();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("statistics", stats);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching order statistics: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to fetch order statistics: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}