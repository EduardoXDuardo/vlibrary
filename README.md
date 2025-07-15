# VLibrary

![Status](https://img.shields.io/badge/Status-In%20Progress-yellow)
![Java](https://img.shields.io/badge/Java-24-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green)

A digital library management system built with Spring Boot.

## 🚧 Project Status

**Last Updated:** 2025-07-15 15:57:26 UTC

This project is currently **under development**. Authentication, genre management, author management, book management, user management, review management, and personal library features have been implemented. Delete functionality and additional features will be added in the coming weeks.

## 🛠️ Technologies

- Java 17
- Spring Boot 3.5.3
- Spring Security with JWT Authentication
- Spring Data JPA
- PostgreSQL (Production) / H2 (Development)
- Maven
- Lombok
- SpringDoc OpenAPI 3 (Swagger)
- Hibernate Validation

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
- `DELETE /api/genres/{id}` - Delete a genre
  > Note: Deleting a genre will also remove its references from all books associated with it.

### Authors
- `POST /api/authors` - Create a new author
- `GET /api/authors` - Get all authors
- `GET /api/authors/{id}` - Get author by ID
- `PATCH /api/authors/{id}` - Update an author
- `DELETE /api/authors/{id}` - Delete an author 
  > Note: An author can only be deleted if it has no books associated with them.

### Books
- `POST /api/books` - Create a new book
- `GET /api/books/search` - Get all books
- `GET /api/books/search?title=xyz` - Search books by title
- `GET /api/books/search?authorId=123` - Search books by author
- `GET /api/books/search?genreId=456` - Search books by genre
- `GET /api/books/{id}` - Get book by ID
- `PATCH /api/books/{id}` - Update a book
- `DELETE /api/books/{id}`  - Delete a book
  > Note: A book can only be deleted if it has no user entries associated with it.

### Users
- `GET /api/users/me` - Get the current authenticated user's profile
- `GET /api/users/search` - Search for users
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `PATCH /api/users/{id}/password` - Update the current user's password
- `DELETE /api/users/{id}` - Delete a user
  > Note: Deleting a user will also delete all their associated reviews and library entries.

### Library
- `POST /api/library` - Add a book to the user's personal library
- `GET /api/library` - Get all books in the user's personal library
- `PATCH /api/library/{userBookId}/reading-status` - Update a book's reading status in the user library
- `DELETE /api/library/{bookId}` - Remove a book from the user's library

### Reviews
- `POST /api/library/{userBookId}/reviews` - Create a review for a book in the user's library
- `GET /api/library/{userBookId}/reviews` - Get all reviews for a book in the user's library
- `GET /api/books/{id}/reviews` - Get all reviews for a book
- `GET /api/users/{id}/reviews` - Get all reviews by a user
- `GET /api/reviews/{id}` - Get a review by ID
- `PATCH /api/reviews/{id}` - Update a review
- `DELETE /api/reviews/{id}` - Delete a review

## 🧪 API Testing with Postman

This project includes a Postman collection with all the available endpoints for easy testing and exploration.

Click the button below to import the collection into your Postman application:

[![Run in Postman](https://run.pstmn.io/button.svg)](https://www.postman.com/luizdudu35/workspace/public-projects/collection/46291934-d0419c87-4271-4e71-a90a-00ba9b9aa2c3?action=share&source=copy-link&creator=46291934)

### Getting Started with Postman

1.  **Import the Collection:** Click the "Run in Postman" button above.
2.  **Set Up an Environment:** It's recommended to create a Postman Environment and add a variable for your JWT token.
3.  **Authenticate:** Run the `POST /api/auth/login` request first to get a token.
4.  **Use the Token:** Copy the token from the login response and set it as the `Bearer Token` in the "Authorization" tab for the protected requests.

## 📋 Setup Instructions

### Prerequisites
- Java 17 JDK or later
- PostgreSQL (for production)
- Maven 3.6+

### Quick Start (Development)
1. Clone the repository
```bash
git clone https://github.com/EduardoXDuardo/vlibrary.git
cd vlibrary
```

2. Run with development profile (uses H2 in-memory database)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

3. Access the application:
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console

### Production Setup
1. Set up PostgreSQL database
2. Create your environment-specific configuration by setting environment variables:
```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/vlibrary
export DATABASE_USERNAME=your_username
export DATABASE_PASSWORD=your_password
export JWT_SECRET=your_very_long_and_secure_secret_key
```

3. Run with production profile
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Configuration Profiles
- **dev**: Development profile with H2 database, debug logging, and Swagger UI enabled
- **prod**: Production profile with PostgreSQL, optimized logging, and security hardening
- **test**: Test profile with H2 database for running tests

### Environment Variables (Production)
- `DATABASE_URL`: PostgreSQL connection URL
- `DATABASE_USERNAME`: Database username
- `DATABASE_PASSWORD`: Database password
- `JWT_SECRET`: JWT signing secret (must be long and secure)
- `JWT_EXPIRATION`: JWT token expiration time in milliseconds (default: 86400000)
- `PORT`: Server port (default: 8080)

## 🚀 Future Enhancements
- User profile management
- Integration with external book APIs
- Enhanced search functionality
- Caching implementation
- Rate limiting for API endpoints
- Enhanced password strength validation
- Email verification system
- Book recommendation system

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
