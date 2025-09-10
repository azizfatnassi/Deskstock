package com.schoolfurniture.repository;

import com.schoolfurniture.entity.Cart;
import com.schoolfurniture.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    
    /**
     * Find cart by user
     */
    Optional<Cart> findByUser(User user);
    
    /**
     * Find cart by user ID
     */
    @Query("SELECT c FROM Cart c WHERE c.user.userId = :userId")
    Optional<Cart> findByUserId(@Param("userId") Integer userId);
    
    /**
     * Check if user has a cart
     */
    boolean existsByUser(User user);
    
    /**
     * Delete cart by user
     */
    void deleteByUser(User user);
    
    /**
     * Get cart with items by user ID
     */
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems ci LEFT JOIN FETCH ci.product WHERE c.user.userId = :userId")
    Optional<Cart> findCartWithItemsByUserId(@Param("userId") Integer userId);
}