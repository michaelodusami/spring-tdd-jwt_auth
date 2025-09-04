# Fakeazon: E-Commerce Backend with JWT Authentication

## TL;DR
Fakeazon is a Spring Boot-based backend for an e-commerce platform, designed to replicate core Amazon functionalities like user authentication, product catalog management, shopping cart, checkout, and more. It uses JSON Web Tokens (JWT) for secure authentication, Spring Security for role-based access control, and PostgreSQL for data persistence. The project follows a modular, test-driven development (TDD) approach, with comprehensive unit and integration tests using JUnit 5, MockMvc, and Mockito. The frontend is built with Svelte, and Docker Compose is used for containerization.

## Skills Demonstrated
- **Backend Development**:
  - Java 21: Core OOP (classes, enums, interfaces), RESTful API development.
  - Spring Boot: REST APIs, Dependency Injection (`@Autowired`, `@Bean`).
  - Spring Security: JWT-based authentication, role-based access control (`@RolesAllowed`, `SecurityFilterChain`).
  - Spring Data JPA: Repository pattern, entity mapping, custom queries (`findByEmail`).
  - Jakarta Validation: Input validation (`@Valid`, `@Email`, `@NotBlank`).
- **Security**:
  - JWT: Stateless authentication, token generation/validation via `JwtService` and `JwtAuthFilter`.
  - BCrypt: Secure password hashing with `BCryptPasswordEncoder`.
  - Role-based Authorization: `ROLE_USER` and `ROLE_ADMIN` endpoints.
- **Database**:
  - PostgreSQL: Data persistence for user and product data.
  - Entity Management: Mapping `User` entity with fields (`id`, `name`, `email`, `password`, `roles`).
- **Testing**:
  - JUnit 5: Unit tests for services (`UserServiceTest`).
  - MockMvc: Integration tests for controllers (`UserAuthControllerTest`, `UserControllerTest`).
  - Mockito: Mocking dependencies (`UserRepository`, `JwtService`, `AuthenticationManager`).
  - TestContainers: PostgreSQL testing in isolated containers.
- **DevOps**:
  - Maven: Dependency management and build automation.
  - Docker Compose: Containerized environment setup.
- **Frontend**:
  - Svelte: Building a responsive frontend (not detailed in project structure).
- **Other**:
  - RESTful API Design: CRUD operations with secure endpoints.
  - Project Management: Modular structure, phased development (authentication, catalog, cart, etc.).

## Overview
Fakeazon is a scalable backend for an e-commerce platform, inspired by Amazon’s core functionalities. It is built with Spring Boot and uses a modular architecture to implement features like user authentication, product catalog management, shopping cart, checkout, user profiles, recommendations, and admin analytics. The project emphasizes security with JWT-based authentication, role-based access control, and password encryption using BCrypt. PostgreSQL handles data persistence, while Docker Compose ensures easy deployment. Comprehensive testing with JUnit 5, MockMvc, and Mockito ensures reliability. The frontend, built with Svelte, integrates with the backend to provide a seamless user experience.

## Features
- **User Authentication**: Secure registration and login with JWT, supporting `ROLE_USER` and `ROLE_ADMIN`.
- **Role-Based Access Control**: Protects endpoints using Spring Security’s `@RolesAllowed`.
- **User Management**: CRUD operations, password updates, and profile management via `UserController` and `UserService`.
- **Product Catalog**: Planned APIs for product management, search, filtering, and sorting (Phase 2).
- **Shopping Cart & Wishlist**: APIs for cart/wishlist management, with persistence for logged-in and guest users (Phase 3).
- **Checkout & Payment**: Planned integration with payment gateways (e.g., Stripe, PayPal) and address management (Phase 4).
- **User Profiles**: Planned features for updating details, viewing order history, and managing notifications (Phase 5).
- **Recommendations & Reviews**: Planned product recommendation engine and user review system (Phase 6).
- **Admin Dashboard**: Planned interface for managing products, users, orders, and analytics (Phase 7).
- **Testing**: Comprehensive unit and integration tests for authentication and user management.
- **Containerization**: Docker Compose for consistent development and deployment environments.

## Prerequisites
- **Java 21**: Required for Spring Boot 3.4.0.
- **Maven 3.6+**: For dependency management and builds.
- **PostgreSQL**: For data persistence (H2 can be used for quick setup).
- **Docker**: For containerized deployment with Docker Compose.
- **Node.js**: For Svelte frontend development.

## Setup Instructions
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/michaelodusami/fakeazon.git
   cd fakeazon
   ```
2. **Configure Database**:
   - Set up PostgreSQL and update `application.properties` with your database URL, username, and password.
   - Alternatively, use H2 for in-memory testing.
3. **Build the Project**:
   ```bash
   mvn clean install
   ```
4. **Run with Docker**:
   ```bash
   docker-compose up
   ```
5. **Run Locally**:
   ```bash
   mvn spring-boot:run
   ```
6. **Access Endpoints**:
   - **Register**: `POST /v1/auth/register`
   - **Login**: `POST /v1/auth/login`
   - **User Management**: CRUD endpoints under `/v1/users` (requires JWT and appropriate roles).

## Endpoints
- **Authentication**:
  - `POST /v1/auth/register`: Register a new user (name, email, password).
  - `POST /v1/auth/login`: Authenticate and receive a JWT.
- **User Management**:
  - `GET /v1/users`: List all users (admin only).
  - `GET /v1/users/{id}`: Get user by ID.
  - `PUT /v1/users/{id}`: Update user details.
  - `DELETE /v1/users/{id}`: Delete a user (admin only).
  - `PUT /v1/users/{id}/password`: Change user password.

## Testing
- **Unit Tests**: Run `mvn test` to execute JUnit 5 tests for services (`UserServiceTest`).
- **Integration Tests**: Use MockMvc for controller tests (`UserAuthControllerTest`, `UserControllerTest`).
- **TestContainers**: PostgreSQL containers for isolated database testing.
- **Coverage**: Tests cover authentication, user CRUD, and security configurations.

## Future Enhancements
- **Complete Remaining Phases**:
  - **Phase 2**: Implement product catalog with search, filtering, and sorting.
  - **Phase 3**: Build shopping cart and wishlist with persistence.
  - **Phase 4**: Integrate payment gateways (Stripe/PayPal) and address management.
  - **Phase 5**: Add user profile management and order history.
  - **Phase 6**: Develop recommendation engine and product review system.
  - **Phase 7**: Create admin dashboard with analytics.
- **Performance Optimization**:
  - Add caching with Spring Boot Starter Cache for frequently accessed data.
  - Optimize database queries with indexing and pagination.
- **Security Enhancements**:
  - Implement refresh tokens for JWT.
  - Add rate limiting to prevent abuse.
- **Scalability**:
  - Integrate Kafka (already in `pom.xml`) for event-driven architecture.
  - Deploy on Kubernetes for high availability.
- **Frontend Improvements**:
  - Enhance Svelte frontend with dynamic product displays and user dashboards.
- **Monitoring**:
  - Add logging and monitoring with tools like Prometheus and Grafana.
- **Accessibility**:
  - Ensure frontend complies with WCAG standards for inclusivity.

## Contributing
Contributions are welcome! Please:
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/YourFeature`).
3. Commit changes (`git commit -m "Add YourFeature"`).
4. Push to the branch (`git push origin feature/YourFeature`).
5. Open a pull request.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

---

This README reflects the current state of the Fakeazon project, focusing on Phase 1 (authentication) while outlining plans for future phases. It highlights your technical skills and provides clear setup instructions for potential users or recruiters. Let me know if you want to add more context or refine any section!
