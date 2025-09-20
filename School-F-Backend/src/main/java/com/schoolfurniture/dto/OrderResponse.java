package com.schoolfurniture.dto;

import com.schoolfurniture.entity.Order;
import com.schoolfurniture.entity.OrderItem;
import com.schoolfurniture.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    
    private Integer orderId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;
    private String notes;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private String userName;
    private List<OrderItemResponse> orderItems;
    
    // Default constructor
    public OrderResponse() {}
    
    // Constructor from Order entity
    public OrderResponse(Order order) {
        this.orderId = order.getOrderId();
        this.customerName = order.getUser() != null ? order.getUser().getName() : null;
        this.customerEmail = order.getUser() != null ? order.getUser().getEmail() : null;
        this.customerPhone = order.getPhoneNumber();
        this.shippingAddress = order.getShippingAddress();
        this.notes = order.getNotes();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.orderDate = order.getOrderDate();
        this.userName = order.getUser() != null ? order.getUser().getName() : null;
        this.orderItems = order.getOrderItems() != null ? 
            order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList()) : null;
    }
    
    // Getters and Setters
    public Integer getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    
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
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }
    
    // Helper methods
    public String getStatusText() {
        return status != null ? status.name() : "Unknown";
    }
    
    public int getTotalItems() {
        return orderItems != null ? orderItems.stream()
            .mapToInt(OrderItemResponse::getQuantity)
            .sum() : 0;
    }
    
    @Override
    public String toString() {
        return "OrderResponse{" +
                "orderId=" + orderId +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", orderDate=" + orderDate +
                ", userName='" + userName + '\'' +
                ", itemCount=" + (orderItems != null ? orderItems.size() : 0) +
                '}';
    }
    
    // Nested class for order item responses
    public static class OrderItemResponse {
        
        private Integer orderItemId;
        private Integer productId;
        private String productName;
        private String productDescription;
        private String productImageUrl;
        private String productCategory;
        private String productColor;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
        
        // Default constructor
        public OrderItemResponse() {}
        
        // Constructor from OrderItem entity
        public OrderItemResponse(OrderItem orderItem) {
            this.orderItemId = orderItem.getOrderItemId();
            this.productId = orderItem.getProduct() != null ? orderItem.getProduct().getProductId() : null;
            this.productName = orderItem.getProductName();
            this.productDescription = orderItem.getProductDescription();
            this.productImageUrl = orderItem.getProductImageUrl();
            this.productCategory = orderItem.getProductCategory();
            this.productColor = orderItem.getProductColor();
            this.quantity = orderItem.getQuantity();
            this.unitPrice = orderItem.getUnitPrice();
            this.subtotal = orderItem.getSubtotal();
        }
        
        // Getters and Setters
        public Integer getOrderItemId() {
            return orderItemId;
        }
        
        public void setOrderItemId(Integer orderItemId) {
            this.orderItemId = orderItemId;
        }
        
        public Integer getProductId() {
            return productId;
        }
        
        public void setProductId(Integer productId) {
            this.productId = productId;
        }
        
        public String getProductName() {
            return productName;
        }
        
        public void setProductName(String productName) {
            this.productName = productName;
        }
        
        public String getProductDescription() {
            return productDescription;
        }
        
        public void setProductDescription(String productDescription) {
            this.productDescription = productDescription;
        }
        
        public String getProductImageUrl() {
            return productImageUrl;
        }
        
        public void setProductImageUrl(String productImageUrl) {
            this.productImageUrl = productImageUrl;
        }
        
        public String getProductCategory() {
            return productCategory;
        }
        
        public void setProductCategory(String productCategory) {
            this.productCategory = productCategory;
        }
        
        public String getProductColor() {
            return productColor;
        }
        
        public void setProductColor(String productColor) {
            this.productColor = productColor;
        }
        
        public Integer getQuantity() {
            return quantity;
        }
        
        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
        
        public BigDecimal getUnitPrice() {
            return unitPrice;
        }
        
        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }
        
        public BigDecimal getSubtotal() {
            return subtotal;
        }
        
        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }
        
        @Override
        public String toString() {
            return "OrderItemResponse{" +
                    "orderItemId=" + orderItemId +
                    ", productId=" + productId +
                    ", productName='" + productName + '\'' +
                    ", quantity=" + quantity +
                    ", unitPrice=" + unitPrice +
                    ", subtotal=" + subtotal +
                    '}';
        }
    }
}