package com.schoolfurniture.service;

import com.schoolfurniture.dto.LoginRequest;
import com.schoolfurniture.dto.LoginResponse;
import com.schoolfurniture.dto.UserProfileUpdateRequest;
import com.schoolfurniture.dto.UserRegistrationRequest;
import com.schoolfurniture.entity.User;
import com.schoolfurniture.enums.Role;
import com.schoolfurniture.repository.UserRepository;
import com.schoolfurniture.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtUtil jwtUtil;
    
    @InjectMocks
    private UserService userService;
    
    private User testUser;
    private UserRegistrationRequest registrationRequest;
    private LoginRequest loginRequest;
    private UserProfileUpdateRequest updateRequest;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1);
        testUser.setName("John Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(Role.CUSTOMER);
        
        registrationRequest = new UserRegistrationRequest();
        registrationRequest.setName("John Doe");
        registrationRequest.setEmail("john.doe@example.com");
        registrationRequest.setPassword("password123");
        registrationRequest.setRole(Role.CUSTOMER);
        
        loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password123");
        
        updateRequest = new UserProfileUpdateRequest();
        updateRequest.setName("John Updated");
        updateRequest.setEmail("john.updated@example.com");
    }
    
    @Test
    void testRegister_Success() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // When
        User result = userService.register(registrationRequest);
        
        // Then
        assertNotNull(result);
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getRole(), result.getRole());
        
        verify(userRepository).existsByEmail(registrationRequest.getEmail());
        verify(passwordEncoder).encode(registrationRequest.getPassword());
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void testRegister_EmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.register(registrationRequest));
        
        assertEquals("Email already exists: " + registrationRequest.getEmail(), exception.getMessage());
        verify(userRepository).existsByEmail(registrationRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void testLogin_Success() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString(), any(Integer.class))).thenReturn("jwt-token");
        
        // When
        LoginResponse result = userService.login(loginRequest);
        
        // Then
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        assertEquals(testUser.getUserId(), result.getUserId());
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getRole(), result.getRole());
        
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), testUser.getPassword());
        verify(jwtUtil).generateToken(testUser.getEmail(), testUser.getRole().name(), testUser.getUserId());
    }
    
    @Test
    void testLogin_UserNotFound() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.login(loginRequest));
        
        assertEquals("Invalid email or password", exception.getMessage());
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
    
    @Test
    void testLogin_InvalidPassword() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.login(loginRequest));
        
        assertEquals("Invalid email or password", exception.getMessage());
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), testUser.getPassword());
        verify(jwtUtil, never()).generateToken(anyString(), anyString(), any(Integer.class));
    }
    
    @Test
    void testUpdateProfile_Success() {
        // Given
        User updatedUser = new User();
        updatedUser.setUserId(1);
        updatedUser.setName("John Updated");
        updatedUser.setEmail("john.updated@example.com");
        updatedUser.setPassword("encodedPassword");
        updatedUser.setRole(Role.CUSTOMER);
        
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        
        // When
        User result = userService.updateProfile(1, updateRequest);
        
        // Then
        assertNotNull(result);
        assertEquals(updateRequest.getName(), result.getName());
        assertEquals(updateRequest.getEmail(), result.getEmail());
        
        verify(userRepository).findById(1);
        verify(userRepository).existsByEmail(updateRequest.getEmail());
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void testUpdateProfile_UserNotFound() {
        // Given
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.updateProfile(1, updateRequest));
        
        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(userRepository).findById(1);
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void testUpdateProfile_EmailAlreadyExists() {
        // Given
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.updateProfile(1, updateRequest));
        
        assertEquals("Email already exists: " + updateRequest.getEmail(), exception.getMessage());
        verify(userRepository).findById(1);
        verify(userRepository).existsByEmail(updateRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void testFindById_Success() {
        // Given
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(testUser));
        
        // When
        User result = userService.findById(1);
        
        // Then
        assertNotNull(result);
        assertEquals(testUser.getUserId(), result.getUserId());
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getEmail(), result.getEmail());
        
        verify(userRepository).findById(1);
    }
    
    @Test
    void testFindById_UserNotFound() {
        // Given
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.findById(1));
        
        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(userRepository).findById(1);
    }
    
    @Test
    void testFindByEmail_Success() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        
        // When
        User result = userService.findByEmail("john.doe@example.com");
        
        // Then
        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        
        verify(userRepository).findByEmail("john.doe@example.com");
    }
    
    @Test
    void testFindByEmail_UserNotFound() {
        // Given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> userService.findByEmail("john.doe@example.com"));
        
        assertEquals("User not found with email: john.doe@example.com", exception.getMessage());
        verify(userRepository).findByEmail("john.doe@example.com");
    }
    
    @Test
    void testExistsByEmail() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        
        // When
        boolean result = userService.existsByEmail("john.doe@example.com");
        
        // Then
        assertTrue(result);
        verify(userRepository).existsByEmail("john.doe@example.com");
    }
}