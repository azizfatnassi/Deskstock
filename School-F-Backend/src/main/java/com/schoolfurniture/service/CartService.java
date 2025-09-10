package com.schoolfurniture.service;

import com.schoolfurniture.dto.CartItemResponse;
import com.schoolfurniture.entity.Cart;
import com.schoolfurniture.entity.CartItem;
import com.schoolfurniture.entity.Product;
import com.schoolfurniture.entity.User;
import com.schoolfurniture.repository.CartRepository;
import com.schoolfurniture.repository.CartItemRepository;
import com.schoolfurniture.repository.ProductRepository;
import com.schoolfurniture.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get or create cart for user
     */
    public Cart getOrCreateCartForUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Optional<Cart> existingCart = cartRepository.findByUser(user);
        if (existingCart.isPresent()) {
            return existingCart.get();
        }
        
        // Create new cart for user
        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }
    
    /**
     * Get cart by user ID
     */
    @Transactional(readOnly = true)
    public Optional<Cart> getCartByUserId(Integer userId) {
        return cartRepository.findByUserId(userId);
    }
    
    /**
     * Get cart with items by user ID
     */
    @Transactional(readOnly = true)
    public Optional<Cart> getCartWithItemsByUserId(Integer userId) {
        return cartRepository.findCartWithItemsByUserId(userId);
    }
    
    /**
     * Add item to cart
     */
    public CartItem addItemToCart(Integer userId, Integer productId, Integer quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }
        
        Cart cart = getOrCreateCartForUser(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        // Check if product is active
        if (!product.getIsActive()) {
            throw new RuntimeException("Product is not available");
        }
        
        // Check stock availability
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity());
        }
        
        // Check if item already exists in cart
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        
        if (existingItem.isPresent()) {
            // Update existing item quantity
            CartItem cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            
            // Check total quantity against stock
            if (product.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity() + 
                                         ", Already in cart: " + cartItem.getQuantity());
            }
            
            cartItem.setQuantity(newQuantity);
            cartItem.setUnitPrice(product.getPrice());
            return cartItemRepository.save(cartItem);
        } else {
            // Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
            return cartItemRepository.save(cartItem);
        }
    }
    
    /**
     * Update item quantity in cart
     */
    public CartItem updateCartItemQuantity(Integer userId, Integer productId, Integer newQuantity) {
        if (newQuantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }
        
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));
        
        // Check stock availability
        if (product.getStockQuantity() < newQuantity) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity());
        }
        
        cartItem.setQuantity(newQuantity);
        cartItem.setUnitPrice(product.getPrice()); // Update price in case it changed
        return cartItemRepository.save(cartItem);
    }
    
    /**
     * Remove item from cart
     */
    public void removeItemFromCart(Integer userId, Integer productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));
        
        cartItemRepository.delete(cartItem);
    }
    
    /**
     * Clear all items from cart
     */
    public void clearCart(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
        
        cartItemRepository.deleteByCart(cart);
    }
    
    /**
     * Get all items in cart
     */
    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(Integer userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (!cart.isPresent()) {
            return new java.util.ArrayList<>(); // Return empty list if no cart exists
        }
        
        return cartItemRepository.findByCart(cart.get());
    }
    
    /**
     * Get all items in cart as DTOs (for API responses)
     */
    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItemsAsDTO(Integer userId) {
        List<CartItem> cartItems = getCartItems(userId);
        
        return cartItems.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert CartItem entity to CartItemResponse DTO
     */
    private CartItemResponse convertToDTO(CartItem cartItem) {
        Product product = cartItem.getProduct();
        
        return new CartItemResponse(
                cartItem.getCartItemId(),
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                cartItem.getQuantity(),
                cartItem.getUnitPrice(),
                cartItem.getSubtotal(),
                cartItem.getAddedAt(),
                cartItem.getUpdatedAt()
        );
    }
    
    /**
     * Get cart item count
     */
    @Transactional(readOnly = true)
    public Long getCartItemCount(Integer userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (!cart.isPresent()) {
            return 0L;
        }
        
        return cartItemRepository.countByCart(cart.get());
    }
    
    /**
     * Get total quantity of items in cart
     */
    @Transactional(readOnly = true)
    public Integer getTotalQuantityInCart(Integer userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (!cart.isPresent()) {
            return 0;
        }
        
        Integer totalQuantity = cartItemRepository.getTotalQuantityByCart(cart.get());
        return totalQuantity != null ? totalQuantity : 0;
    }
    
    /**
     * Calculate cart total
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateCartTotal(Integer userId) {
        List<CartItem> cartItems = getCartItems(userId);
        
        return cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Check if item exists in cart
     */
    @Transactional(readOnly = true)
    public boolean isItemInCart(Integer userId, Integer productId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (!cart.isPresent()) {
            return false;
        }
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        return cartItemRepository.existsByCartAndProduct(cart.get(), product);
    }
    
    /**
     * Get cart item by product ID
     */
    @Transactional(readOnly = true)
    public Optional<CartItem> getCartItem(Integer userId, Integer productId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (!cart.isPresent()) {
            return Optional.empty();
        }
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        return cartItemRepository.findByCartAndProduct(cart.get(), product);
    }
    
    /**
     * Validate cart items against current stock and prices
     */
    @Transactional(readOnly = true)
    public List<String> validateCartItems(Integer userId) {
        List<CartItem> cartItems = getCartItems(userId);
        List<String> validationErrors = new java.util.ArrayList<>();
        
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            
            // Check if product is still active
            if (!product.getIsActive()) {
                validationErrors.add("Product '" + product.getName() + "' is no longer available");
                continue;
            }
            
            // Check stock availability
            if (product.getStockQuantity() < item.getQuantity()) {
                validationErrors.add("Insufficient stock for '" + product.getName() + 
                                   "'. Available: " + product.getStockQuantity() + 
                                   ", In cart: " + item.getQuantity());
            }
            
            // Check if price has changed
            if (item.getUnitPrice().compareTo(product.getPrice()) != 0) {
                validationErrors.add("Price changed for '" + product.getName() + 
                                   "'. Current price: $" + product.getPrice() + 
                                   ", Cart price: $" + item.getUnitPrice());
            }
        }
        
        return validationErrors;
    }
    
    /**
     * Update cart item prices to current product prices
     */
    public void updateCartItemPrices(Integer userId) {
        List<CartItem> cartItems = getCartItems(userId);
        
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            if (item.getUnitPrice().compareTo(product.getPrice()) != 0) {
                item.setUnitPrice(product.getPrice());
                cartItemRepository.save(item);
            }
        }
    }
    
    /**
     * Get cart summary with total items and total price
     */
    public Map<String, Object> getCartSummary(Integer userId) {
        List<CartItem> cartItems = getCartItems(userId);
        
        int totalItems = cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
                
        BigDecimal totalPrice = cartItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalItems", totalItems);
        summary.put("totalPrice", totalPrice);
        
        return summary;
    }
}