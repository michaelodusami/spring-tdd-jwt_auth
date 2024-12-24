package com.github.michaelodusami.fakeazon.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.michaelodusami.fakeazon.modules.user.dto.RegisterRequest;
import com.github.michaelodusami.fakeazon.modules.user.entity.User;
import com.github.michaelodusami.fakeazon.modules.user.repository.UserRepository;
import com.github.michaelodusami.fakeazon.modules.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.getRoles().add("ROLE_USER");
    }

    
    @Test
    void findAll_test()
    {
        // Arrange: Define Behaviors
        User user = User.builder().name("User 1").build();
        User user2 = User.builder().name("User 2").build();
        List<User> users = List.of(user, user2);
        
        when(userRepository.findAll()).thenReturn(users);
        
        // Act: Call Methods
        List<User> usersInRepo = userService.findAll();

        // Assert: Assert Testing
        assertEquals(usersInRepo, users);
    }

    @Test
    void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByEmail() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByEmail("john.doe@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testSaveUserWithRegisterRequest() {

        RegisterRequest registerRequest = new RegisterRequest("John Doe", "john.doe@example.com", "password123");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        Optional<User> savedUser = userService.save(registerRequest);

        assertTrue(savedUser.isPresent());
        assertEquals("John Doe", savedUser.get().getName());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSaveUserWithDuplicateEmailThrowsException() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("john.doe@example.com");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.save(registerRequest);
        });

        assertEquals("Email already registered", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        boolean isDeleted = userService.deleteUser(1L);

        assertTrue(isDeleted);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane.doe@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        Optional<User> result = userService.updateUser(1L, updatedUser);

        assertTrue(result.isPresent());
        assertEquals("Jane Doe", result.get().getName());
        assertEquals("jane.doe@example.com", result.get().getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testChangePassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        boolean isChanged = userService.changePassword(1L, "newPassword");

        assertTrue(isChanged);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindUsersByRole() {
        when(userRepository.findUsersByRole("ROLE_USER")).thenReturn(List.of(user));

        List<User> users = userService.findUsersByRole("ROLE_USER");

        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
        verify(userRepository, times(1)).findUsersByRole("ROLE_USER");
    }
}
