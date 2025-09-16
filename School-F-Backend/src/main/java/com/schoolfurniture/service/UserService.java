package com.schoolfurniture.service;

import com.schoolfurniture.dto.LoginRequest;
import com.schoolfurniture.dto.LoginResponse;
import com.schoolfurniture.dto.UserProfileUpdateRequest;
import com.schoolfurniture.dto.UserRegistrationRequest;
import com.schoolfurniture.entity.User;
import com.schoolfurniture.repository.UserRepository;
import com.schoolfurniture.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Register a new user
     * @param registrationRequest the user registration request
     * @return the registered user
     * @throws RuntimeException if email already exists
     */
    public User register(UserRegistrationRequest registrationRequest) {
        // Check if email already exists
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new RuntimeException("Email already exists: " + registrationRequest.getEmail());
        }
        
        // Create new user
        User user = new User();
        user.setName(registrationRequest.getName());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(registrationRequest.getRole());
        
        return userRepository.save(user);
    }
    
    /**
     * Authenticate user and return JWT token
     * @param loginRequest the login request
     * @return LoginResponse with JWT token and user info
     * @throws RuntimeException if authentication fails
     */
    public LoginResponse login(LoginRequest loginRequest) {
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Invalid email or password");
        }
        
        User user = userOptional.get();
        
        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getUserId());
        
        return new LoginResponse(token, user.getUserId(), user.getName(), user.getEmail(), user.getRole());
    }
    
    /**
     * Update user profile
     * @param userId the user ID
     * @param updateRequest the profile update request
     * @return the updated user
     * @throws RuntimeException if user not found or email already exists
     */
    public User updateProfile(Integer userId, UserProfileUpdateRequest updateRequest) {
        // Find user by ID
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        
        User user = userOptional.get();
        
        // Check if email is being changed and if new email already exists
        if (!user.getEmail().equals(updateRequest.getEmail())) {
            if (userRepository.existsByEmail(updateRequest.getEmail())) {
                throw new RuntimeException("Email already exists: " + updateRequest.getEmail());
            }
        }
        
        // Update user information
        user.setName(updateRequest.getName());
        user.setEmail(updateRequest.getEmail());
        
        return userRepository.save(user);
    }
    
    /**
     * Find user by ID
     * @param userId the user ID
     * @return the user
     * @throws RuntimeException if user not found
     */
    public User findById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
    
    /**
     * Find user by email
     * @param email the email
     * @return the user
     * @throws RuntimeException if user not found
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    
    /**
     * Check if user exists by email
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Get all users (Admin only)
     * @return list of all users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    /**
     * Delete user by ID (Admin only)
     * @param userId the user ID to delete
     * @throws RuntimeException if user not found
     */
    public void deleteById(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }
}