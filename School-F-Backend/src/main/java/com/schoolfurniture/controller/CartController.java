package com.schoolfurniture.controller;

import com.schoolfurniture.dto.CartItemResponse;
import com.schoolfurniture.entity.CartItem;
import com.schoolfurniture.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "http://localhost:4202"})
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    /**
     * Get cart items for user
     */
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItems(@RequestParam Integer userId) {
        try {
            List<CartItemResponse> cartItems = cartService.getCartItemsAsDTO(userId);
            return ResponseEntity.ok(cartItems);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Add item to cart
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam Integer userId,
            @RequestParam Integer productId,
            @RequestParam Integer quantity) {
        try {
            CartItem cartItem = cartService.addItemToCart(userId, productId, quantity);
            return ResponseEntity.ok(cartItem);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Update cart item quantity
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(
            @RequestParam Integer userId,
            @RequestParam Integer productId,
            @RequestParam Integer quantity) {
        try {
            CartItem cartItem = cartService.updateCartItemQuantity(userId, productId, quantity);
            return ResponseEntity.ok(cartItem);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Remove item from cart
     */
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromCart(
            @RequestParam Integer userId,
            @RequestParam Integer productId) {
        try {
            cartService.removeItemFromCart(userId, productId);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Item removed from cart");
            return ResponseEntity.ok().body(successResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Clear cart
     */
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestParam Integer userId) {
        try {
            cartService.clearCart(userId);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Cart cleared");
            return ResponseEntity.ok().body(successResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Get cart summary (total items and price)
     */
    @GetMapping("/summary")
    public ResponseEntity<?> getCartSummary(@RequestParam Integer userId) {
        try {
            Map<String, Object> summary = cartService.getCartSummary(userId);
            return ResponseEntity.ok(summary);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}