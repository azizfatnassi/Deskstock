-- School Furniture Database Schema
-- MySQL Script for creating the complete database structure

-- Create database
CREATE DATABASE IF NOT EXISTS school_furniture_db;
USE school_furniture_db;

-- Drop tables if they exist (in correct order to avoid foreign key constraints)
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20),
    address TEXT,
    city VARCHAR(100),
    postal_code VARCHAR(20),
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_username (username),
    INDEX idx_role (role)
);

-- Create products table
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    category VARCHAR(100) NOT NULL,
    color VARCHAR(50),
    material VARCHAR(100),
    dimensions VARCHAR(100),
    weight DECIMAL(8,2),
    image_url VARCHAR(500),
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_color (color),
    INDEX idx_price (price),
    INDEX idx_available (is_available)
);

-- Create carts table
CREATE TABLE carts (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
);

-- Create cart_items table
CREATE TABLE cart_items (
    cart_item_id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES carts(cart_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    UNIQUE KEY unique_cart_product (cart_id, product_id),
    INDEX idx_cart_id (cart_id),
    INDEX idx_product_id (product_id)
);

-- Create orders table
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    user_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    shipping_address VARCHAR(500) NOT NULL,
    shipping_city VARCHAR(100),
    shipping_postal_code VARCHAR(20),
    phone_number VARCHAR(20),
    notes TEXT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    shipped_date TIMESTAMP NULL,
    delivered_date TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_order_number (order_number),
    INDEX idx_status (status),
    INDEX idx_order_date (order_date)
);

-- Create order_items table
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_description TEXT,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
);

-- Insert sample data

-- Insert admin user (password: admin123)
INSERT INTO users (username, email, password, first_name, last_name, role) VALUES
('admin', 'admin@schoolfurniture.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin', 'User', 'ADMIN');

-- Insert sample users (password: user123)
INSERT INTO users (username, email, password, first_name, last_name, phone_number, address, city, postal_code) VALUES
('john_doe', 'john@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'John', 'Doe', '123-456-7890', '123 Main St', 'Anytown', '12345'),
('jane_smith', 'jane@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Jane', 'Smith', '098-765-4321', '456 Oak Ave', 'Somewhere', '67890');

-- Insert sample products
INSERT INTO products (name, description, price, stock_quantity, category, color, material, dimensions, weight, image_url) VALUES
('Student Desk', 'Adjustable height student desk perfect for classroom use', 149.99, 50, 'Desks', 'Brown', 'Wood', '120x60x75 cm', 25.5, 'https://example.com/student-desk.jpg'),
('Ergonomic Chair', 'Comfortable ergonomic chair with lumbar support', 89.99, 30, 'Chairs', 'Black', 'Plastic/Fabric', '45x45x85 cm', 8.2, 'https://example.com/ergonomic-chair.jpg'),
('Bookshelf Unit', 'Five-shelf bookshelf unit for classroom storage', 199.99, 20, 'Storage', 'White', 'Metal', '80x30x180 cm', 35.0, 'https://example.com/bookshelf.jpg'),
('Teacher Desk', 'Large teacher desk with multiple drawers', 299.99, 15, 'Desks', 'Oak', 'Wood', '150x80x75 cm', 45.0, 'https://example.com/teacher-desk.jpg'),
('Plastic Chair', 'Stackable plastic chair for students', 39.99, 100, 'Chairs', 'Blue', 'Plastic', '40x40x75 cm', 3.5, 'https://example.com/plastic-chair.jpg'),
('Filing Cabinet', 'Four-drawer filing cabinet for office use', 179.99, 25, 'Storage', 'Gray', 'Metal', '40x60x130 cm', 28.0, 'https://example.com/filing-cabinet.jpg'),
('Whiteboard', 'Magnetic whiteboard with aluminum frame', 129.99, 40, 'Boards', 'White', 'Metal/Melamine', '120x90x2 cm', 12.0, 'https://example.com/whiteboard.jpg'),
('Computer Table', 'Compact computer table with keyboard tray', 119.99, 35, 'Desks', 'Black', 'Wood/Metal', '100x60x75 cm', 22.0, 'https://example.com/computer-table.jpg');

-- Create carts for sample users
INSERT INTO carts (user_id) VALUES (2), (3);

-- Insert sample cart items
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES
(1, 1, 2),
(1, 2, 1),
(2, 3, 1),
(2, 5, 4);

-- Insert sample orders
INSERT INTO orders (order_number, user_id, total_amount, status, shipping_address, shipping_city, shipping_postal_code, phone_number) VALUES
('ORD-2024-001', 2, 389.97, 'DELIVERED', '123 Main St', 'Anytown', '12345', '123-456-7890'),
('ORD-2024-002', 3, 359.95, 'SHIPPED', '456 Oak Ave', 'Somewhere', '67890', '098-765-4321');

-- Insert sample order items
INSERT INTO order_items (order_id, product_id, quantity, unit_price, product_name, product_description) VALUES
(1, 1, 2, 149.99, 'Student Desk', 'Adjustable height student desk perfect for classroom use'),
(1, 2, 1, 89.99, 'Ergonomic Chair', 'Comfortable ergonomic chair with lumbar support'),
(2, 3, 1, 199.99, 'Bookshelf Unit', 'Five-shelf bookshelf unit for classroom storage'),
(2, 5, 4, 39.99, 'Plastic Chair', 'Stackable plastic chair for students');

-- Create indexes for better performance
CREATE INDEX idx_products_category_price ON products(category, price);
CREATE INDEX idx_orders_user_date ON orders(user_id, order_date);
CREATE INDEX idx_order_items_order_product ON order_items(order_id, product_id);

-- Show table structure
SHOW TABLES;

-- Display sample data
SELECT 'Users' as Table_Name, COUNT(*) as Record_Count FROM users
UNION ALL
SELECT 'Products', COUNT(*) FROM products
UNION ALL
SELECT 'Carts', COUNT(*) FROM carts
UNION ALL
SELECT 'Cart Items', COUNT(*) FROM cart_items
UNION ALL
SELECT 'Orders', COUNT(*) FROM orders
UNION ALL
SELECT 'Order Items', COUNT(*) FROM order_items;

-- Database setup complete
SELECT 'School Furniture Database setup completed successfully!' as Status;