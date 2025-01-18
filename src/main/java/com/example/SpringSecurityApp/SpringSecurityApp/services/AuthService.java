package com.example.SpringSecurityApp.SpringSecurityApp.services;

import com.example.SpringSecurityApp.SpringSecurityApp.dto.LoginDto;
import com.example.SpringSecurityApp.SpringSecurityApp.entities.Session;
import com.example.SpringSecurityApp.SpringSecurityApp.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SessionService sessionService;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, SessionService sessionService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.sessionService = sessionService;
    }

    public ResponseEntity<?> login(LoginDto loginDto) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );

            logger.info("Authentication successful for user: " + authentication.getName());

            // Get authenticated user details
            User user = (User) authentication.getPrincipal();
            logger.info("User: " + user.getEmail());

            // Generate JWT token
            String token = jwtService.generateToken(user);

            // Check if the session is already valid
            if (sessionService.isUserSessionValid(user.getEmail(), token)) {
                logger.info("An active session already exists for user: " + user.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("An active session already exists for this user.");
            }

            // Create a new session
            Session session = sessionService.createSession(user.getEmail(), token);
            if (session != null) {
                logger.debug("Session Created for user " + user.getEmail() + " with token " + token);
                return ResponseEntity.status(HttpStatus.CREATED).body(session);
            } else {
                logger.error("Failed to create a session for user: " + user.getEmail());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to create an active login session. Please try again.");
            }

        } catch (BadCredentialsException e) {
            // Handle invalid login credentials
            logger.error("Invalid credentials for user: " + loginDto.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password.");
        } catch (Exception e) {
            // Handle unexpected errors
            logger.error("An error occurred during login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login. Please try again.");
        }
    }
}

