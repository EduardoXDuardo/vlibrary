# VLibrary

![Status](https://img.shields.io/badge/Status-In%20Progress-yellow)
![Java](https://img.shields.io/badge/Java-24-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green)

## 📚 Project Description

VLibrary is a digital library management system that allows users to organize and track their personal book collections, reading progress, and reviews.  
The platform offers a secure, modern REST API built with Java and Spring Boot, following best practices for API design, architecture, security, and deployment.

## 🚧 Project Status

**Last Updated:** 2025-01-05

VLibrary is currently **under active development**.  
The foundational features are implemented and being improved.

### ✅ Main Features

- **User authentication** with JWT-based security
- **Role-based access control** (Admin, Librarian, User roles)
- **User management** (registration, password change, profile, role assignment)
- **Genre, author, and book management**
- **Integration with Google Books API** for searching and importing books
- **Personal library**: add books, update reading status, remove from library
- **Review system**: users can review and rate books in their library
- **Advanced search and filtering** for books, authors, genres, users, reviews
- **Multi-environment configuration** with Spring Profiles (dev, test, prod)
- **Interactive API documentation** via Swagger/OpenAPI (environment-aware)
- **Postman collection** for easy API testing

## 📋 Setup & Configuration

### Prerequisites
- Java 24 JDK
- PostgreSQL (for production environment)
- Maven

### Environment Profiles & Configuration Files

VLibrary uses **Spring Profiles** for environment-specific configuration, ensuring security and optimal settings for each deployment stage. The following files are in `src/main/resources/`:

| Profile | File | Database | Swagger UI | CORS Policy | Admin Password | JWT Secret & Google API Key | Use Case |
|---------|------|----------|------------|-------------|----------------|----------------------------|----------|
| **dev** | application-dev.properties | H2 in-memory | ✅ Enabled | Permissive (*) | `admin123` | Must set in file | Local development |
| **test** | application-test.properties | H2 isolated | ✅ Enabled | Permissive (*) | `test123` | Must set in file | Automated testing |
| **prod** | application-prod.properties | PostgreSQL | ❌ Disabled | Restricted | Via env variable | Via env variable | Production deployment |

### How to Configure Each Environment

<details>
<summary><b>Development (default)</b></summary>

1. Open `src/main/resources/application-dev.properties` and add:
   ```ini
   app.jwt.secret=dev-secret-key
   google.api.key=dev-google-api-key
   ```
2. Run the project:
   ```bash
   mvn spring-boot:run
   ```
   Access: `http://localhost:8080` | Swagger UI: `http://localhost:8080/swagger-ui/index.html`

</details>

<details>
<summary><b>Testing</b></summary>

1. Open `src/main/resources/application-test.properties` and add:
   ```ini
   app.jwt.secret=dev-secret-key
   google.api.key=dev-google-api-key
   ```
2. Run the tests:
   ```bash
   mvn test
   ```

</details>

<details>
<summary><b>Production</b></summary>

1. Set the environment variables before running:
   ```bash
   export SPRING_PROFILES_ACTIVE=prod
   export DB_URL=your-postgres-host:5432/vlibrary_db
   export DB_USERNAME=your-db-username
   export DB_PASSWORD=your-db-password
   export ADMIN_DEFAULT_PASSWORD=your-secure-admin-password
   export JWT_SECRET=your-production-jwt-secret
   export GOOGLE_API_KEY=your-production-google-api-key
   mvn spring-boot:run
   ```

</details>

### Security Notes
- **Never use dev/test values in production!**
- Never hardcode sensitive data in production, always use environment variables.
- CORS is automatically restricted in production (configure allowed origins in `ApplicationConfig.java`).
- Swagger UI is disabled in production for security.

## 🔧 ️Technologies

- **Backend:** Java 24, Spring Boot 3.5.3
- **Security:** Spring Security, JWT Authentication, Role-based Access Control
- **Database:** PostgreSQL (prod), H2 (dev/test)
- **Documentation:** SpringDoc OpenAPI, Swagger UI (environment-aware)
- **External API:** Google Books API integration
- **Build Tool:** Maven
- **Code Quality:** Lombok
- **Testing:** Postman collection

## 🏗️ Project Architecture

VLibrary follows a standard layered architecture pattern with environment-aware configuration:

### Key Components

- **Environment Management**: Spring Profiles for deployment flexibility
- **Configuration Layer**: Environment-specific beans and security policies
- **Security**: JWT-based authentication with role hierarchy
- **API Layer**: REST controllers with role-based security annotations
- **Service Layer**: Business logic and security implementation
- **Client Layer**: External API communication (Google Books API)
- **Data Layer**: JPA repositories and entity models
- **Entities**: Domain models representing the database structure
- **DTOs**: Data Transfer Objects for clean API contracts

The application separates domain entities from API representations using DTOs, ensuring clean data contracts and preventing entity exposure. Spring Security provides authentication with JWT tokens, enabling stateless API access with secure, role-based endpoints.

<details>
<summary><b>Architecture Flow</b></summary>

```
Environment-Aware Configuration (Spring Profiles)
                    ↓
          Security Layer (JWT + Roles)
                    ↓
DTOs → Controllers → Services → External APIs (Google Books)
      ↑                 |                       |
      |                 +-> Repositories → Database (PostgreSQL/H2)
      |                                         |
      +-----------------------------------------+
               (Entity to DTO conversion)
```
</details>

<details>
<summary><b>Class Diagram</b></summary>
Below is a UML class diagram representing the main entities and their relationships in the project:

![class-diagram.png](src/main/resources/static/class-diagram.png)

The diagram shows the core entities (`User`, `Book`, `Author`, `Genre`, `UserBook`, `Review`, `ReadingStatus`) and how they are related.
</details>

## 🔑 API Endpoints

### 🔐 Role-Based Access Control

The system uses role-based access control:
- `ROLE_ADMIN`: Can create, edit, and delete authors, genres, books, and users, and assign roles to other users.
- `ROLE_LIBRARIAN`: Can create, edit, and delete authors, genres, and books.
- `ROLE_USER`: Can view and search data, manage their own library and reviews.

When registering a new user, they automatically receive the `ROLE_USER` role. The first admin is created automatically when the system starts with credentials that depend on the active profile:
- **Development**: username: `admin`, password: `admin123`
- **Test**: username: `admin`, password: `test123`
- **Production**: username: `admin`, password: set via `ADMIN_DEFAULT_PASSWORD` environment variable

<details>
<summary><b>🔑 Authentication Endpoints</b></summary>

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and receive JWT token

</details>

<details>
<summary><b>📚 Genre Endpoints</b></summary>

- `POST /api/genres` - Create a new genre
- `GET /api/genres` - Search for genres
  - Supports searching by name, as well as pagination and sorting
  - Example: `GET /api/genres?name=abc&page=0&size=10&sortBy=name&sortDirection=asc`
- `GET /api/genres/{id}` - Get genre by ID
- `PATCH /api/genres/{id}` - Update a genre
- `DELETE /api/genres/{id}` - Delete a genre
  > Note: Deleting a genre will also remove its references from all books associated with it.

</details>

<details>
<summary><b>🖊️ Author Endpoints</b></summary>

- `POST /api/authors` - Create a new author
- `GET /api/authors` - Search for authors
  - Supports searching by name, as well as pagination and sorting
  - Example: `GET /api/authors?name=abc&page=0&size=10&sortBy=name&sortDirection=asc`
- `GET /api/authors/{id}` - Get author by ID
- `PATCH /api/authors/{id}` - Update an author
- `DELETE /api/authors/{id}` - Delete an author
  > Note: An author can only be deleted if it has no books associated with them.

</details>

<details>
<summary><b>📖 Book Endpoints</b></summary>

- `POST /api/books` - Create a new book
- `POST /api/books/external/{apiId}` - Import a book from the Google Books API into the local database using its unique `apiId`. If the book already exists, it is returned; otherwise, it is created.
  - Example: `POST /api/books/import/vxB1mAEACAAJ`
- `GET /api/books` - Search for books
  - Supports searching by title, author, and genre, as well as pagination and sorting
  - Example: `GET /api/books?title=xyz&authorId=123&genreId=456&page=0&size=10&sortBy=title&sortDirection=asc`
- `GET /api/books/external` - Search for books in the Google Books API
  - Supports searching by title, author, publisher, category, ISBN and language.
  - Example: `GET /api/books/external?title=xyz&author=abc&publisher=def&category=ghi&isbn=1234567890`
  - Note: The language parameter seems to be been ignored by the Google Books API, so it may not filter results as expected.
- `GET /api/books/{id}` - Get book by ID
- `PATCH /api/books/{id}` - Update a book
- `DELETE /api/books/{id}` - Delete a book
  > Note: A book can only be deleted if it has no user entries associated with it.

</details>

<details>
<summary><b>👤 User Endpoints</b></summary>

- `GET /api/users/me` - Get the current authenticated user's profile
- `GET /api/users` - Search for users
  - Supports searching by username and email, as well as pagination and sorting
  - Example: `GET /api/users?username=abc&email=def&page=0&size=10&sortBy=username&sortDirection=asc`
- `GET /api/users/{id}` - Get user by ID
- `PATCH /api/users/{id}/password` - Update the current user's password
- `PATCH /api/users/{id}/roles` - Update a user's roles
- `DELETE /api/users/{id}` - Delete a user
  > Note: Deleting a user will also delete all their associated reviews and library entries.

</details>

<details>
<summary><b>📚 Library Endpoints</b></summary>

- `POST /api/library` - Add a book to the user's personal library
- `GET /api/library` - Search the library
  - Supports searching by user, title, author, genre, rating and status, as well as pagination and sorting
  - Example: `GET /api/library?userId=123&bookTitle=abc&authorId=456&genreId=789&rating=2.5&status=read&page=0&size=10&sortBy=id&sortDirection=asc`
  - Note: If no user ID is provided, it defaults to the authenticated user.
- `PATCH /api/library/{userBookId}/reading-status` - Update a book's reading status in the user library
- `DELETE /api/library/{bookId}` - Remove a book from the user's library

</details>

<details>
<summary><b>⭐ Review Endpoints</b></summary>

- `POST /api/library/{userBookId}/reviews` - Create a review for a book in the user's library
  > Only the owner of the library entry can create a review.
- `GET /api/reviews` - Search for reviews
  - Supports searching by user, book, comment text (`commentContains`), and rating, as well as pagination and sorting
  - Example: `GET /api/reviews?userId=123&bookId=456&commentContains=abc&rating=4&page=0&size=10&sortBy=id&sortDirection=asc`
  - Note: If no `userId` is provided, it defaults to the authenticated user and only their reviews are shown.
- `GET /api/reviews/{id}` - Get a review by ID
- `PATCH /api/reviews/{id}` - Update a review
- `DELETE /api/reviews/{id}` - Delete a review

</details>

## 📖 API Documentation with Swagger

This project includes **environment-aware** interactive API documentation:

| Environment | Swagger UI | Access URL |
|-------------|------------|------------|
| **Development** | ✅ Enabled | `http://localhost:8080/swagger-ui/index.html` |
| **Test** | ✅ Enabled | Available during testing |
| **Production** | ❌ Disabled | Not accessible (security) |

### Authorizing Requests in Swagger UI

The Swagger UI is fully integrated with Spring Security and role-based access control:

1. **Login:** Execute `POST /api/auth/login` with valid credentials
2. **Authorize:** Click the **Authorize** button in Swagger UI
3. **Token:** Enter `Bearer <your-jwt-token>` in the authorization field
4. **Test:** All endpoints now respect your user's roles and permissions

**Default admin credentials (dev environment):**
- Username: `admin`
- Password: `admin123`

## 🧪 API Testing with Postman

This project includes a Postman collection with all the available endpoints for easy testing and exploration.

Click the button below to import the collection into your Postman application:

[![Run in Postman](https://run.pstmn.io/button.svg)](https://www.postman.com/luizdudu35/workspace/public-projects/collection/46291934-d0419c87-4271-4e71-a90a-00ba9b9aa2c3?action=share&source=copy-link&creator=46291934)

### Getting Started with Postman

1.  **Import the Collection:** Click the "Run in Postman" button above.
2.  **Set Up an Environment:** It's recommended to create a Postman Environment and add a variable for your JWT token.
3.  **Authenticate:** Run the `POST /api/auth/login` request first to get a token.
4.  **Use the Token:** Copy the token from the login response and set it as the `Bearer Token` in the "Authorization" tab for the protected requests.

## 🛣️ Roadmap & Next Steps

- **Centralized API Exception Handling:**  
  Implement a global exception handler to provide consistent and informative error responses for all API endpoints, improving developer experience and maintainability.
- **Enhanced user profile features**
- **Additional security improvements and refactoring**
- **More detailed error handling and user feedback**

> For details and status of ongoing and planned features, see the [issue tracker](https://github.com/EduardoXDuardo/vlibrary/issues).

## 🤝 Contributing

Contributions are welcome! If you have suggestions for improvements, bug reports, or want to help implement new features, feel free to open an issue or submit a pull request.

**How to contribute:**
1. Fork the repository.
2. Create a new branch for your feature or fix.
3. Commit your changes with clear messages.
4. Push your branch and open a pull request.
5. Describe your changes and reference any related issues.

Check the [issue tracker](https://github.com/EduardoXDuardo/vlibrary/issues) for things to work on.

Please follow the project's code style and conventions.  
For major changes, open an issue first to discuss what you would like to change.

Thank you for helping make VLibrary better!

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.