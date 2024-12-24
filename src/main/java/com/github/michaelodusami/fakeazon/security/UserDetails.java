package com.github.michaelodusami.fakeazon.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;

/**
 * The UserDetails class implements the Spring Security `UserDetails` interface,
 * providing custom user details for authentication and authorization.
 * 
 * Purpose:
 * This class adapts the `User` entity to fit the requirements of Spring Security's
 * authentication framework, encapsulating user credentials and authorities.
 * 
 * Why It Matters:
 * It serves as the bridge between the application's user data model and Spring Security,
 * enabling features like authentication, role-based access control, and session management.
 * 
 * Impact on the Application:
 * - Ensures seamless integration of the `User` entity with Spring Security.
 * - Encodes roles as granted authorities to enforce role-based access control (RBAC).
 * - Supports flexible authentication by adapting the user model to Spring Security's needs.
 * 
 * @author Michael-Andre Odusami
 * @version 1.0.0
 */
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private String email;

    private String password;

    private Set<GrantedAuthority> authorities;

     /**
     * Constructs a UserDetails instance from a User entity.
     * 
     * Purpose:
     * Initializes the UserDetails object by mapping user roles to granted authorities
     * and populating the credentials (email and password).
     * 
     * Impact:
     * Adapts the application's user model for authentication within Spring Security.
     * 
     * @param user the User entity to adapt.
     */
    public UserDetails(User user)
    {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    /**
     * Retrieves the authorities (roles) granted to the user.
     * 
     * Purpose:
     * Provides the user's roles to Spring Security for enforcing access control.
     * 
     * Impact:
     * Enables role-based access management by mapping roles to `GrantedAuthority`.
     * 
     * @return a collection of granted authorities (user roles).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Retrieves the user's password for authentication.
     * 
     * Purpose:
     * Supplies the encoded password for comparison during login.
     * 
     * Impact:
     * Supports secure authentication by matching credentials.
     * 
     * @return the encoded password of the user.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the username (email) for authentication.
     * 
     * Purpose:
     * Supplies the user's email, which acts as the unique identifier during login.
     * 
     * Impact:
     * Enables email-based login functionality.
     * 
     * @return the email address of the user.
     */
    @Override
    public String getUsername() {
        return email;
    }
    
}
