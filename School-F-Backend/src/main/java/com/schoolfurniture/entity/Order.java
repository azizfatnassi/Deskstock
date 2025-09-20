package com.schoolfurniture.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;
    
    @Column(name = "order_number", unique = true, length = 50)
    private String orderNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();
    
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Total amount must have at most 8 integer digits and 2 decimal places")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Column(name = "order_date", nullable = false, updatable = false)
    private LocalDateTime orderDate;
    
    @NotBlank(message = "Shipping address is required")
    @Size(max = 500, message = "Shipping address cannot exceed 500 characters")
    @Column(name = "shipping_address", length = 500, nullable = false)
    private String shippingAddress;
    
    @Size(max = 100, message = "Shipping city cannot exceed 100 characters")
    @Column(name = "shipping_city", length = 100)
    private String shippingCity;
    
    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    @Column(name = "shipping_postal_code", length = 20)
    private String shippingPostalCode;
    
    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Default constructor
    public Order() {}
    
    // Constructor with required parameters
    public Order(User user, BigDecimal totalAmount, String shippingAddress) {
        this.user = user;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.orderNumber = generateOrderNumber();
    }
    
    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (orderNumber == null) {
            orderNumber = generateOrderNumber();
        }
    }
    
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    
    public String getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public String getShippingCity() {
        return shippingCity;
    }
    
    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }
    
    public String getShippingPostalCode() {
        return shippingPostalCode;
    }
    
    public void setShippingPostalCode(String shippingPostalCode) {
        this.shippingPostalCode = shippingPostalCode;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Helper methods
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    
    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }
    
    public BigDecimal calculateTotalAmount() {
        return orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public int getTotalItems() {
        return orderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }
    
    public boolean isPaymentConfirmed() {
        return status == OrderStatus.CONFIRMED || status == OrderStatus.PROCESSING || 
               status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + (user != null ? user.getUserId() : null) +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", orderDate=" + orderDate +
                '}';
    }
}