### Summary of Technologies and Files You've Worked On
### **Technologies Used**

#### 1. **Programming Language**
   - **Java**:
     - Backend logic and API development.
     - Core OOP principles (classes, enums, interfaces).
     - Framework integrations (Spring Boot, JPA).

#### 2. **Frameworks and Libraries**
   - **Spring Boot**:
     - Building RESTful APIs.
     - Dependency Injection via `@Autowired` and `@Bean`.
   - **Spring Security**:
     - Securing endpoints using JWT and role-based access control.
     - Filters and authentication mechanisms.
     - `@RolesAllowed`, `SecurityFilterChain`, and method-level security.
   - **Jakarta Validation**:
     - Input validation using annotations like `@Valid`, `@Email`, and `@NotBlank`.

#### 3. **Database and Persistence**
   - **Spring Data JPA**:
     - Repository pattern for CRUD operations.
     - Entity mapping for user data (`User` entity).
     - Custom queries using `findByEmail`.

#### 4. **Authentication and Security**
   - **JSON Web Tokens (JWT)**:
     - Stateless authentication with token generation and validation.
     - User authentication via `JwtAuthFilter` and `JwtService`.
   - **BCryptPasswordEncoder**:
     - Secure password hashing.
   - **Role-based Access Control**:
     - Differentiated endpoints for `ROLE_USER` and `ROLE_ADMIN`.

#### 5. **Testing**
   - **JUnit 5**:
     - Unit tests for services (`UserServiceTest`).
   - **MockMvc**:
     - Integration tests for controllers (`UserAuthControllerTest`, `UserControllerTest`).
   - **Mockito**:
     - Mocking dependencies (`UserRepository`, `JwtService`, `AuthenticationManager`).

#### 6. **Web Standards**
   - **RESTful API**:
     - HTTP methods for CRUD operations.
     - Endpoints secured with JWT and roles.

#### 7. **Build Tools**
   - **Maven**:
     - Dependency management and build automation.

---

### **Files Worked On**

#### **Controllers**
1. **`UserAuthController.java`**
   - **Purpose**: Handles user authentication (login and registration).
   - **Endpoints**:
     - `POST /v1/auth/login`: Authenticates users and generates JWT.
     - `POST /v1/auth/register`: Registers new users.
   - **Features**:
     - Secure authentication using `AuthenticationManager` and `JwtService`.

2. **`UserController.java`**
   - **Purpose**: Manages user data and operations.
   - **Endpoints**:
     - CRUD operations for users.
     - Role-based creation of users (`ROLE_USER` vs. `ROLE_ADMIN`).
     - Password change functionality.
   - **Features**:
     - `@RolesAllowed` for role-based access control.

#### **Entities**
3. **`User.java`**
   - **Purpose**: Represents the user in the database.
   - **Fields**:
     - `id`, `name`, `email`, `password`, `roles`, `createdAt`, `updatedAt`.
   - **Features**:
     - `@ElementCollection` for roles.
     - Mapped to `users` table.

#### **Enums**
4. **`UserRole.java`**
   - **Purpose**: Defines user roles (`ROLE_USER`, `ROLE_ADMIN`).
   - **Features**:
     - Getter for role strings.
     - Utility method to convert a string to `UserRole`.

#### **Services**
5. **`UserService.java`**
   - **Purpose**: Business logic for user management.
   - **Methods**:
     - `findAll`, `findById`, `findByEmail`.
     - `updateUser`: Updates user fields.
     - `changePassword`: Updates user password securely.
     - `save`: Creates new users with roles.

6. **`CustomUserDetailsService.java`**
   - **Purpose**: Loads user details for Spring Security.
   - **Features**:
     - Fetches users from the database by email.

#### **Security**
7. **`JwtAuthFilter.java`**
   - **Purpose**: Intercepts requests to validate JWT.
   - **Features**:
     - Extracts and validates JWT.
     - Sets `SecurityContext` if the token is valid.

8. **`JwtService.java`**
   - **Purpose**: Handles JWT generation and validation.
   - **Features**:
     - Generates tokens with claims.
     - Validates tokens using the signing key.
     - Extracts claims like username and expiration.

9. **`SecurityConfig.java`**
   - **Purpose**: Configures Spring Security.
   - **Features**:
     - Stateless session management.
     - `@RolesAllowed` integration via `@EnableGlobalMethodSecurity`.

#### **Repositories**
10. **`UserRepository.java`**
    - **Purpose**: Provides database access for `User` entities.
    - **Features**:
      - Custom query method: `findByEmail`.

---

### Summary of Work Completed

You have implemented:
1. **Authentication**:
   - Secure login with JWT and role-based registration for users.
2. **Authorization**:
   - Role-based access control using `@RolesAllowed`.
3. **User Management**:
   - CRUD operations, password updates, and field-specific updates.
4. **Security Configuration**:
   - Spring Security integration for endpoint protection.
5. **Testing**:
   - Comprehensive tests for services (`UserServiceTest`) and controllers (`UserAuthControllerTest`, `UserControllerTest`).

