package com.github.michaelodusami.fakeazon.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

/**
 * The JwtService class handles JSON Web Token (JWT) operations, such as token generation, validation,
 * and claims extraction, for secure authentication in the Fakeazon application.
 * 
 * Purpose:
 * This service provides methods to generate JWTs, extract claims, and validate tokens. 
 * JWTs are used to securely transmit user identity and claims in a stateless manner.
 * 
 * Why It Matters:
 * JWT-based authentication allows scalable, secure, and efficient user session management 
 * without storing session state on the server.
 * 
 * Impact on the Application:
 * - Enables stateless authentication by issuing tokens with encoded user information.
 * - Validates tokens for secure access to protected resources.
 * - Extracts user-specific information from tokens for authorization and session management.
 * 
 * @author Michael-Andre Odusami
 * @version 1.0.0
 */
@Getter
@Service
public class JwtService {

    @Value("${spring.jwt.secret}")
    private String secret; //t = "851e2cb4ac7657b6cff30a0ae25080e70c1a27d4c452050bcd55cf55ed8af4c0795a717a03ff5c290ebb1073c45e8f9b22aa65796f17d06c6df2e30cb01f27c56b760cd5432f758cd7df86fc8e9f6d9c678a33c2b2022dca999b349ac76f2fcf65fd0ca2f1a16920aef6df175aa215035ffc6221d769ee92a6518470773f4d208a33f75adb32ce9acfb621615684d85fd78ddc906a8d98c891bd13843edf776f079d301d216427dc6593ee978f6d313d4dfb0e82f842cdd526f06ad495d160f009fea20210b35f7bb6a143de9bf3e356d52194f04b07cc33c7e3e10da4692a6488bd3b2f7100335f7919eda948aa82945b21c64fd4df2f50c02e8021d21c35e9"; // Secret key for signing the JWT.

    private static final long EXPIRATION_TIME = 100 * 60 * 30; // Token validity period (30 minutes).

    /**
     * Generates a JWT for the given email.
     * 
     * Purpose:
     * Issues a token for the user with the specified email, embedding any necessary claims.
     * 
     * Impact:
     * Provides a token to authenticate users during their session.
     * 
     * @param email the email address of the user.
     * @return the generated JWT as a string.
     */
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    /**
     * Creates a JWT with the provided claims and subject (email).
     * 
     * Purpose:
     * Builds the token by embedding claims, setting expiration time, and signing it.
     * 
     * Impact:
     * Encodes user-specific information and ensures the token's integrity using a signature.
     * 
     * @param claims a map of claims to embed in the token.
     * @param email the email to set as the subject of the token.
     * @return the generated token as a string.
     */
    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Token valid for 30 minutes
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Retrieves the signing key for token generation and validation.
     * 
     * Purpose:
     * Decodes the secret and returns a cryptographic key for signing the token.
     * 
     * Impact:
     * Ensures secure token signing and validation using the HS256 algorithm.
     * 
     * @return the signing key.
     */
    protected Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the username (email) from a token.
     * 
     * Purpose:
     * Retrieves the token's subject, which corresponds to the user's email.
     * 
     * Impact:
     * Facilitates user identification based on the token.
     * 
     * @param token the JWT.
     * @return the email (username) embedded in the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from a token.
     * 
     * Purpose:
     * Retrieves the token's expiration date for validation.
     * 
     * Impact:
     * Supports checks to determine whether a token is still valid.
     * 
     * @param token the JWT.
     * @return the expiration date of the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from a token using a resolver function.
     * 
     * Purpose:
     * Provides a generic method to retrieve various claims from the token.
     * 
     * Impact:
     * Simplifies claim retrieval for different token-related operations.
     * 
     * @param <T> the type of the claim to extract.
     * @param token the JWT.
     * @param claimsResolver a function to resolve the desired claim.
     * @return the resolved claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from a token.
     * 
     * Purpose:
     * Parses the token to retrieve all embedded claims.
     * 
     * Impact:
     * Allows extraction of any claim for validation or other operations.
     * 
     * @param token the JWT.
     * @return the claims contained in the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validates a token against user details.
     * 
     * Purpose:
     * Ensures the token belongs to the user and is not expired.
     * 
     * Impact:
     * Provides a secure mechanism to authenticate users based on their token.
     * 
     * @param token the JWT to validate.
     * @param userDetails the user details to compare against.
     * @return true if the token is valid, otherwise false.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractUsername(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if a token is expired.
     * 
     * Purpose:
     * Determines whether the token is still valid based on its expiration date.
     * 
     * Impact:
     * Prevents the use of expired tokens in authentication and authorization.
     * 
     * @param token the JWT.
     * @return true if the token is expired, otherwise false.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
