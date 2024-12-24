package com.github.michaelodusami.fakeazon.users;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.security.UserDetails;

class UserDetailsUnitTest {
    private User user;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("john.doe@example.com");
        user.setPassword("encodedPassword");
        user.getRoles().addAll(Set.of("ROLE_USER", "ROLE_ADMIN"));

        userDetails = new UserDetails(user);
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        Set<String> expectedRoles = new HashSet<>(Set.of("ROLE_USER", "ROLE_ADMIN"));
        Set<String> actualRoles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertEquals(expectedRoles, actualRoles, "Authorities should match the roles assigned to the user");
    }

    @Test
    void testGetPassword() {
        assertEquals("encodedPassword", userDetails.getPassword(), "Password should match the user's password");
    }

    @Test
    void testGetUsername() {
        assertEquals("john.doe@example.com", userDetails.getUsername(), "Username should match the user's email");
    }

    @Test
    void testConstructorInitializesFieldsCorrectly() {
        assertEquals("john.doe@example.com", userDetails.getUsername(), "Email should be initialized correctly");
        assertEquals("encodedPassword", userDetails.getPassword(), "Password should be initialized correctly");

        Set<String> expectedAuthorities = new HashSet<>(Set.of("ROLE_USER", "ROLE_ADMIN"));
        Set<String> actualAuthorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertEquals(expectedAuthorities, actualAuthorities, "Authorities should be initialized correctly");
    }
}
