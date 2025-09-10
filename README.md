# School Furniture E-commerce Platform

A full-stack e-commerce platform for school furniture built with Angular frontend and Spring Boot backend.

## Project Structure

```
├── School-F-Backend/          # Spring Boot REST API
└── School-F-Front/
    └── school-furniture-angular/  # Angular Frontend
```

## Features

### Backend (Spring Boot)
- RESTful API with JWT authentication
- User registration and login
- Product catalog management
- Shopping cart functionality
- H2 in-memory database
- Spring Security configuration
- CORS support for frontend integration

### Frontend (Angular)
- Modern responsive UI with SCSS styling
- User authentication (login/register)
- Product browsing and search
- Shopping cart management
- JWT token handling
- HTTP interceptors for authentication

## Technology Stack

### Backend
- Java 8+
- Spring Boot 2.x
- Spring Security
- Spring Data JPA
- H2 Database
- JWT (JSON Web Tokens)
- Maven

### Frontend
- Angular 15+
- TypeScript
- SCSS
- Angular Material (optional)
- RxJS
- Angular CLI

## Getting Started

### Prerequisites
- Java 8 or higher
- Node.js 16+ and npm
- Git

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd School-F-Backend
   ```

2. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
   Or on Windows:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

3. The backend will start on `http://localhost:8081`

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd School-F-Front/school-furniture-angular
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   ng serve --port 4201
   ```

4. The frontend will be available at `http://localhost:4201`

## API Endpoints

### Authentication
- `POST /api/users/register` - User registration
- `POST /api/users/login` - User login

### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID

### Cart
- `GET /api/cart?userId={userId}` - Get cart items
- `POST /api/cart/add?userId={userId}&productId={productId}&quantity={quantity}` - Add to cart

## Default Data

The application comes with sample products:
1. **Executive Desk** - $299.99
2. **Ergonomic Chair** - $199.99
3. **Bookshelf** - $149.99

## Development Notes

- The backend uses H2 in-memory database, so data resets on restart
- CORS is configured to allow requests from `http://localhost:4201`
- JWT tokens are used for authentication
- All API endpoints except authentication are public for development

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License.