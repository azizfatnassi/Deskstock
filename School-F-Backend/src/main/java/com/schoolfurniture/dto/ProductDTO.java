package com.schoolfurniture.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private String color;
    private String codeArticle;
    private Integer stock;
    private String imageUrl;
    private Boolean active;
}