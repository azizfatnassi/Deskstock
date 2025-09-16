package com.schoolfurniture.controller;

import com.schoolfurniture.enums.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "http://localhost:4202"})
public class CategoryController {
    
    /**
     * Get all available categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = Arrays.stream(Category.values())
            .map(category -> new CategoryResponse(
                category.name(),
                category.getDisplayName(),
                category.getDescription()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }
    
    /**
     * Get category by name
     */
    @GetMapping("/{name}")
    public ResponseEntity<CategoryResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category category = Category.valueOf(name.toUpperCase());
            CategoryResponse response = new CategoryResponse(
                category.name(),
                category.getDisplayName(),
                category.getDescription()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Response DTO for Category
     */
    public static class CategoryResponse {
        private String name;
        private String displayName;
        private String description;
        
        public CategoryResponse(String name, String displayName, String description) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        
        // Setters
        public void setName(String name) { this.name = name; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public void setDescription(String description) { this.description = description; }
    }
}