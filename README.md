# VLibrary

![Status](https://img.shields.io/badge/Status-In%20Progress-yellow)
![Java](https://img.shields.io/badge/Java-24-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green)

A digital library management system built with Spring Boot.

## 🚧 Project Status

**Last Updated:** 2025-07-04 17:49:22 UTC

This project is currently **under development**. Authentication, genre management, author management, book management, and user management features have been implemented. Delete functionality and additional features will be added in the coming weeks.
## 🛠️ Technologies

- Java 24
- Spring Boot 3.5.3
- Spring Security
- JWT Authentication
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok

## 🏗️ Project Architecture

VLibrary follows a standard layered architecture pattern:

```
DTOs → Controllers → Services → Repositories → Database
      ↑                                          |
      +------------------------------------------+
               (Entity to DTO conversion)
```

### Key Components

- **API Layer**: REST controllers for authentication endpoints
- **Service Layer**: Business logic and security implementation
- **Data Layer**: JPA repositories and entity models
- **DTOs**: Data Transfer Objects for request/response data encapsulation
- **Security**: JWT-based authentication and authorization

The application separates domain entities from API representations using DTOs, ensuring clean data contracts and preventing entity exposure. Spring Security provides authentication with JWT tokens, enabling stateless API access with secure endpoints.

## ⚠️ Security Implementation Note

The security implementation (authentication and authorization) was developed with the assistance of AI tools. This part of the codebase will be refactored in the future as I enhance my knowledge of Spring Security best practices.

## 🔑 Currently Implemented Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and receive JWT token

### Genres
- `POST /api/genres` - Create a new genre
- `GET /api/genres` - Get all genres
- `GET /api/genres/{id}` - Get genre by ID
- `PATCH /api/genres/{id}` - Update a genre

### Authors
- `POST /api/authors` - Create a new author
- `GET /api/authors` - Get all authors
- `GET /api/authors/{id}` - Get author by ID
- `PATCH /api/authors/{id}` - Update an author

### Books
- `POST /api/books` - Create a new book
- `GET /api/books/search` - Get all books
- `GET /api/books/search?title=xyz` - Search books by title
- `GET /api/books/search?authorId=123` - Search books by author
- `GET /api/books/search?genreId=456` - Search books by genre
- `GET /api/books/{id}` - Get book by ID
- `PATCH /api/books/{id}` - Update a book

## 🔜 Planned Endpoints (Not Yet Implemented)

### Library
All library management endpoints are planned but not yet implemented.

### Reviews
All review management endpoints are planned but not yet implemented.

### All controllers
All delete endpoints are planned but not yet implemented.

## 📋 Setup Instructions

### Prerequisites
- Java 24 JDK
- PostgreSQL
- Maven

### Configuration
1. Clone the repository
```
git clone https://github.com/EduardoXDuardo/vlibrary.git
```

2. Create your own `application.properties` file in `src/main/resources` by copying the example file:
```
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

3. Edit the `application.properties` file with your specific database credentials and settings. The example file provides the structure you need to follow:
```
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/vlibrary
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# JWT Configuration
app.jwt.secret=your_jwt_secret_key
app.jwt.expiration=86400000
```

4. Build the project
```
mvn clean install
```

5. Run the application
```
mvn spring-boot:run
```

## 🚀 Future Enhancements
- Complete implementation of book management features
- User profile management
- Refactoring of security implementation
- Integration with external book APIs
- Enhanced search functionality

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.