package com.schoolfurniture.dto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

public class CreateOrderRequest {
    
    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Customer name cannot exceed 100 characters")
    private String customerName;
    
    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Customer email cannot exceed 100 characters")
    private String customerEmail;
    
    @NotBlank(message = "Customer phone is required")
    @Size(max = 20, message = "Customer phone cannot exceed 20 characters")
    private String customerPhone;
    
    @NotBlank(message = "Shipping address is required")
    @Size(max = 500, message = "Shipping address cannot exceed 500 characters")
    private String shippingAddress;
    
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;
    
    @NotNull(message = "Order items are required")
    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequest> orderItems;
    
    // Default constructor
    public CreateOrderRequest() {}
    
    // Constructor with required fields
    public CreateOrderRequest(String customerName, String customerEmail, String customerPhone, 
                            String shippingAddress, List<OrderItemRequest> orderItems) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.shippingAddress = shippingAddress;
        this.orderItems = orderItems;
    }
    
    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public String getCustomerPhone() {
        return customerPhone;
    }
    
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItemRequest> orderItems) {
        this.orderItems = orderItems;
    }
    
    @Override
    public String toString() {
        return "CreateOrderRequest{" +
                "customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", notes='" + notes + '\'' +
                ", orderItems=" + orderItems +
                '}';
    }
    
    // Nested class for order item requests
    public static class OrderItemRequest {
        
        @NotNull(message = "Product ID is required")
        @Min(value = 1, message = "Product ID must be positive")
        private Integer productId;
        
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        @Max(value = 1000, message = "Quantity cannot exceed 1000")
        private Integer quantity;
        
        // Default constructor
        public OrderItemRequest() {}
        
        // Constructor
        public OrderItemRequest(Integer productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
        
        // Getters and Setters
        public Integer getProductId() {
            return productId;
        }
        
        public void setProductId(Integer productId) {
            this.productId = productId;
        }
        
        public Integer getQuantity() {
            return quantity;
        }
        
        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
        
        @Override
        public String toString() {
            return "OrderItemRequest{" +
                    "productId=" + productId +
                    ", quantity=" + quantity +
                    '}';
        }
    }
}