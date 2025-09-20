package com.schoolfurniture.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Integer orderItemId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Unit price must have at most 8 integer digits and 2 decimal places")
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    // Snapshot fields to preserve product information at time of order
    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name cannot exceed 100 characters")
    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;
    
    @Size(max = 1000, message = "Product description cannot exceed 1000 characters")
    @Column(name = "product_description", columnDefinition = "TEXT")
    private String productDescription;
    
    @Column(name = "product_image_url")
    private String productImageUrl;
    
    @Column(name = "product_category", length = 50)
    private String productCategory;
    
    @Column(name = "product_color", length = 50)
    private String productColor;
    
    // Default constructor
    public OrderItem() {}
    
    // Constructor with required parameters
    public OrderItem(Order order, Product product, Integer quantity, BigDecimal unitPrice) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        // Copy product information as snapshot
        if (product != null) {
            this.productName = product.getName();
            this.productDescription = product.getDescription();
            this.productImageUrl = product.getImageUrl();
            this.productCategory = product.getCategory();
            this.productColor = product.getColor();
        }
    }
    
    // Getters and Setters
    public Integer getOrderItemId() {
        return orderItemId;
    }
    
    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
        // Update snapshot fields when product is set
        if (product != null) {
            this.productName = product.getName();
            this.productDescription = product.getDescription();
            this.productImageUrl = product.getImageUrl();
            this.productCategory = product.getCategory();
            this.productColor = product.getColor();
        }
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
    
    // Helper methods
    public BigDecimal getSubtotal() {
        if (quantity != null && unitPrice != null) {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", orderId=" + (order != null ? order.getOrderId() : null) +
                ", productId=" + (product != null ? product.getProductId() : null) +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}