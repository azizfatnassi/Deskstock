package com.schoolfurniture.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "<h1>School Furniture Backend API</h1>" +
               "<p>Backend is running successfully on port 8081</p>" +
               "<p>Available endpoints:</p>" +
               "<ul>" +
               "<li>/api/auth/* - Authentication endpoints</li>" +
               "<li>/api/users/* - User management</li>" +
               "<li>/api/products/* - Product management</li>" +
               "<li>/api/cart/* - Shopping cart</li>" +
               "<li>/api/orders/* - Order management</li>" +
               "</ul>" +
               "<p>Frontend is available at: <a href='http://localhost:4202'>http://localhost:4202</a></p>";
    }
    
    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "{\"status\": \"UP\", \"message\": \"School Furniture Backend is running\"}";
    }
}