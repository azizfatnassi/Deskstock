# School Furniture E-commerce Backend

A Spring Boot REST API for a school furniture e-commerce platform with user management, JWT authentication, and role-based access control.

## Features

- User registration and authentication
- JWT-based security
- Role-based access control (ADMIN, CUSTOMER)
- Password encryption with BCrypt
- MySQL database integration
- Comprehensive validation
- Unit tests

## Technologies Used

- **Spring Boot 3.2.0**
- **Spring Security**
- **Spring Data JPA**
- **MySQL 8.0**
- **JWT (JSON Web Tokens)**
- **BCrypt Password Encoding**
- **Maven**
- **JUnit 5 & Mockito**

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd school-furniture-backend
```

### 2. Database Setup
1. Install MySQL and create a database:
```sql
CREATE DATABASE school_furniture_db;
```

2. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Run Tests
```bash
mvn test
```

## API Endpoints

### Authentication Endpoints

#### Register User
```http
POST /api/users/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "role": "CUSTOMER"
}
```

#### Login User
```http
POST /api/users/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "role": "CUSTOMER"
}
```

### User Profile Endpoints

#### Update Profile
```http
PUT /api/users/{id}/profile
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "John Updated",
  "email": "john.updated@example.com"
}
```

#### Get Profile
```http
GET /api/users/{id}/profile
Authorization: Bearer <jwt-token>
```

## Security Rules

- **Public Endpoints**: `/api/users/register`, `/api/users/login`
- **Admin Only**: `/api/admin/**`
- **Profile Access**: 
  - Admins can access any user profile
  - Customers can only access their own profile

## Validation Rules

- **Name**: 2-100 characters, required
- **Email**: Valid email format, unique, required
- **Password**: Minimum 6 characters, required
- **Role**: ADMIN or CUSTOMER (defaults to CUSTOMER)

## Project Structure

```
src/
├── main/
│   ├── java/com/schoolfurniture/
│   │   ├── config/
│   │   │   └── SecurityConfig.java
│   │   ├── controller/
│   │   │   └── UserController.java
│   │   ├── dto/
│   │   │   ├── LoginRequest.java
│   │   │   ├── LoginResponse.java
│   │   │   ├── UserProfileUpdateRequest.java
│   │   │   └── UserRegistrationRequest.java
│   │   ├── entity/
│   │   │   └── User.java
│   │   ├── enums/
│   │   │   └── Role.java
│   │   ├── repository/
│   │   │   └── UserRepository.java
│   │   ├── security/
│   │   │   ├── CustomUserDetailsService.java
│   │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── UserPrincipal.java
│   │   ├── service/
│   │   │   └── UserService.java
│   │   ├── util/
│   │   │   └── JwtUtil.java
│   │   └── SchoolFurnitureBackendApplication.java
│   └── resources/
│       └── application.properties
└── test/
    ├── java/com/schoolfurniture/
    │   ├── service/
    │   │   └── UserServiceTest.java
    │   └── SchoolFurnitureBackendApplicationTests.java
    └── resources/
        └── application-test.properties
```

## Configuration

### JWT Configuration
- **Secret Key**: Configurable via `app.jwt.secret`
- **Expiration**: 24 hours (configurable via `app.jwt.expiration`)

### Database Configuration
- **URL**: `jdbc:mysql://localhost:3306/school_furniture_db`
- **Auto DDL**: `update` (creates/updates tables automatically)

## Error Handling

The API returns appropriate HTTP status codes:
- `200 OK`: Successful requests
- `201 Created`: Successful user registration
- `400 Bad Request`: Validation errors, duplicate email
- `401 Unauthorized`: Invalid credentials, missing/invalid JWT
- `403 Forbidden`: Insufficient permissions
- `404 Not Found`: User not found

## Testing

The project includes comprehensive unit tests for:
- User registration (success and failure cases)
- User login (success and failure cases)
- Profile updates (success and failure cases)
- User lookup operations

Tests use H2 in-memory database and Mockito for mocking dependencies.

## Future Enhancements

- Password reset functionality
- Email verification
- User profile pictures
- Admin user management endpoints
- Audit logging
- Rate limiting
- API documentation with Swagger/OpenAPI