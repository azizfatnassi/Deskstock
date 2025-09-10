package com.schoolfurniture.repository;

import com.schoolfurniture.entity.User;
import com.schoolfurniture.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    /**
     * Find user by email
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by email
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Find users by role
     * @param role the role to search for
     * @return list of users with the specified role
     */
    List<User> findByRole(Role role);
    
    /**
     * Find users by name containing (case insensitive)
     * @param name the name pattern to search for
     * @return list of users whose names contain the pattern
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Count users by role
     * @param role the role to count
     * @return number of users with the specified role
     */
    long countByRole(Role role);
}