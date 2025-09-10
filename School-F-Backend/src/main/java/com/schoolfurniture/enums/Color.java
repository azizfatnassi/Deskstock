package com.schoolfurniture.enums;

public enum Color {
    WHITE("White", "#FFFFFF"),
    BLACK("Black", "#000000"),
    BROWN("Brown", "#8B4513"),
    GRAY("Gray", "#808080"),
    BLUE("Blue", "#0066CC"),
    RED("Red", "#CC0000"),
    GREEN("Green", "#00CC00"),
    YELLOW("Yellow", "#FFCC00"),
    ORANGE("Orange", "#FF6600"),
    BEIGE("Beige", "#F5F5DC"),
    NAVY("Navy Blue", "#000080"),
    MAROON("Maroon", "#800000"),
    NATURAL_WOOD("Natural Wood", "#DEB887"),
    DARK_WOOD("Dark Wood", "#654321"),
    LIGHT_WOOD("Light Wood", "#F4A460");
    
    private final String displayName;
    private final String hexCode;
    
    Color(String displayName, String hexCode) {
        this.displayName = displayName;
        this.hexCode = hexCode;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getHexCode() {
        return hexCode;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}