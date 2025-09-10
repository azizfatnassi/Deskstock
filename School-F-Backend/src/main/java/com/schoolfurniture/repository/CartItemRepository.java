package com.schoolfurniture.repository;

import com.schoolfurniture.entity.Cart;
import com.schoolfurniture.entity.CartItem;
import com.schoolfurniture.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    
    /**
     * Find cart items by cart
     */
    List<CartItem> findByCart(Cart cart);
    
    /**
     * Find cart item by cart and product
     */
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    
    /**
     * Find cart items by cart ID
     */
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartId = :cartId")
    List<CartItem> findByCartId(@Param("cartId") Integer cartId);
    
    /**
     * Find cart items by user ID
     */
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.user.userId = :userId")
    List<CartItem> findByUserId(@Param("userId") Integer userId);
    
    /**
     * Delete cart items by cart
     */
    void deleteByCart(Cart cart);
    
    /**
     * Delete cart item by cart and product
     */
    void deleteByCartAndProduct(Cart cart, Product product);
    
    /**
     * Count items in cart
     */
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cart = :cart")
    Long countByCart(@Param("cart") Cart cart);
    
    /**
     * Get total quantity in cart
     */
    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.cart = :cart")
    Integer getTotalQuantityByCart(@Param("cart") Cart cart);
    
    /**
     * Check if product exists in cart
     */
    boolean existsByCartAndProduct(Cart cart, Product product);
}