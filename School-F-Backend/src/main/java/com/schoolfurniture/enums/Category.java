package com.schoolfurniture.enums;

public enum Category {
    DESKS("Desks", "Student and teacher desks"),
    CHAIRS("Chairs", "Classroom and office chairs"),
    TABLES("Tables", "Conference and study tables"),
    STORAGE("Storage", "Cabinets, lockers, and shelving"),
    WHITEBOARDS("Whiteboards", "Interactive and traditional whiteboards"),
    LIGHTING("Lighting", "Classroom and office lighting solutions"),
    ACCESSORIES("Accessories", "School furniture accessories and supplies");
    
    private final String displayName;
    private final String description;
    
    Category(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}