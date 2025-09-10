package com.schoolfurniture.controller;

import com.schoolfurniture.enums.Color;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/colors")
@CrossOrigin(origins = "http://localhost:4200")
public class ColorController {
    
    /**
     * Get all available colors
     */
    @GetMapping
    public ResponseEntity<List<ColorResponse>> getAllColors() {
        List<ColorResponse> colors = Arrays.stream(Color.values())
            .map(color -> new ColorResponse(
                color.name(),
                color.getDisplayName(),
                color.getHexCode()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(colors);
    }
    
    /**
     * Get color by name
     */
    @GetMapping("/{name}")
    public ResponseEntity<ColorResponse> getColorByName(@PathVariable String name) {
        try {
            Color color = Color.valueOf(name.toUpperCase());
            ColorResponse response = new ColorResponse(
                color.name(),
                color.getDisplayName(),
                color.getHexCode()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Response DTO for Color
     */
    public static class ColorResponse {
        private String name;
        private String displayName;
        private String hexCode;
        
        public ColorResponse(String name, String displayName, String hexCode) {
            this.name = name;
            this.displayName = displayName;
            this.hexCode = hexCode;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getHexCode() { return hexCode; }
        
        // Setters
        public void setName(String name) { this.name = name; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
        public void setHexCode(String hexCode) { this.hexCode = hexCode; }
    }
}