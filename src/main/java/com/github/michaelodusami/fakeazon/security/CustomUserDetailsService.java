package com.github.michaelodusami.fakeazon.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.michaelodusami.fakeazon.modules.user.repository.UserRepository;

/**
 * The CustomUserDetailsService class implements the Spring Security `UserDetailsService` interface,
 * providing a custom mechanism for loading user details during authentication.
 * 
 * Purpose:
 * This class adapts the application's user data (stored in the database) to Spring Security's
 * authentication framework by fetching user details based on their email.
 * 
 * Why It Matters:
 * Spring Security requires a `UserDetailsService` to retrieve user credentials and authorities
 * during the login process. This class customizes that behavior to fit the application's needs.
 * 
 * Impact on the Application:
 * - Facilitates secure authentication by integrating the `UserRepository` with Spring Security.
 * - Bridges the `User` entity with Spring Security's `UserDetails` interface.
 * - Provides detailed error handling for invalid login attempts (e.g., user not found).
 * 
 * @author Michael-Andre Odusami
 * @version 1.0.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

     /**
     * Loads user details by email for authentication.
     * 
     * Purpose:
     * Fetches a user's credentials and roles from the database and adapts them
     * to the Spring Security `UserDetails` format.
     * 
     * Impact:
     * - Enables email-based authentication.
     * - Throws a clear exception when the user is not found, which can be used
     *   to provide feedback to the client or log invalid access attempts.
     * 
     * @param email the email address of the user to load.
     * @return a `UserDetails` object containing the user's credentials and authorities.
     * @throws UsernameNotFoundException if no user is found with the specified email.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email);
        if (user.isEmpty())
        {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        UserDetails userDetails = new UserDetails(user.get());
        return userDetails;
        
    }
    
}
