package com.schoolfurniture.controller;

import com.schoolfurniture.dto.LoginRequest;
import com.schoolfurniture.dto.LoginResponse;
import com.schoolfurniture.dto.UserProfileUpdateRequest;
import com.schoolfurniture.dto.UserRegistrationRequest;
import com.schoolfurniture.entity.User;
import com.schoolfurniture.service.UserService;
import com.schoolfurniture.util.JwtUtil;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Register a new user
     * @param registrationRequest the user registration request
     * @return ResponseEntity with success message and user info
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        try {
            User user = userService.register(registrationRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("userId", user.getUserId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    /**
     * Authenticate user and return JWT token
     * @param loginRequest the login request
     * @return ResponseEntity with JWT token and user info
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = userService.login(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
    
    /**
     * Update user profile
     * @param id the user ID
     * @param updateRequest the profile update request
     * @param authHeader the authorization header
     * @return ResponseEntity with updated user info
     */
    @PutMapping("/{id}/profile")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and #id == authentication.principal.userId)")
    public ResponseEntity<?> updateProfile(
            @PathVariable Integer id,
            @Valid @RequestBody UserProfileUpdateRequest updateRequest,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from Authorization header
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            Integer tokenUserId = jwtUtil.extractUserId(token);
            String tokenRole = jwtUtil.extractRole(token);
            
            // Check authorization: Admin can update any profile, Customer can only update their own
            if (!"ADMIN".equals(tokenRole) && !id.equals(tokenUserId)) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Access denied: You can only update your own profile");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            User updatedUser = userService.updateProfile(id, updateRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Profile updated successfully");
            response.put("userId", updatedUser.getUserId());
            response.put("name", updatedUser.getName());
            response.put("email", updatedUser.getEmail());
            response.put("role", updatedUser.getRole());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    /**
     * Get user profile by ID (Admin only or own profile)
     * @param id the user ID
     * @param authHeader the authorization header
     * @return ResponseEntity with user info
     */
    @GetMapping("/{id}/profile")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CUSTOMER') and #id == authentication.principal.userId)")
    public ResponseEntity<?> getUserProfile(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from Authorization header
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            Integer tokenUserId = jwtUtil.extractUserId(token);
            String tokenRole = jwtUtil.extractRole(token);
            
            // Check authorization: Admin can view any profile, Customer can only view their own
            if (!"ADMIN".equals(tokenRole) && !id.equals(tokenUserId)) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Access denied: You can only view your own profile");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }
            
            User user = userService.findById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getUserId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}