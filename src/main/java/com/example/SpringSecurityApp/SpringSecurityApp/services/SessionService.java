package com.example.SpringSecurityApp.SpringSecurityApp.services;

import com.example.SpringSecurityApp.SpringSecurityApp.entities.Session;
import com.example.SpringSecurityApp.SpringSecurityApp.repositories.SessionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final JwtService jwtService;

    private final Logger logger = LoggerFactory.getLogger(SessionService.class);

    public SessionService(SessionRepository sessionRepository, JwtService jwtService) {
        this.sessionRepository = sessionRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public Session createSession(String userId, String token) {
        try {
            // Check for an existing session
            Optional<Session> existingSession = sessionRepository.findByUserId(userId);

            if (existingSession.isPresent()) {
                logger.debug("Existing session found for userId: {}", userId);
                sessionRepository.delete(existingSession.get()); // Delete existing session
                logger.debug("Existing session deleted for userId: {}", userId);
            } else {
                logger.debug("No existing session found for userId: {}", userId);
            }

            // Create a new session
            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime expiresAt = createdAt.plusMinutes(5);

            Session newSession = new Session(null, userId, token, createdAt, expiresAt, true);
            logger.debug("New Session: ", newSession);
            logger.debug("Creating new session for userId: {}", userId);

            return sessionRepository.save(newSession);

        } catch (Exception e) {
            logger.error("Error creating session for userId {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to create session", e); // Rethrow for proper error handling
        }
    }

    public boolean isUserSessionValid(String userId, String token) {
        Optional<Session> sessionOptional = sessionRepository.findByUserId(userId);

        if (sessionOptional.isEmpty()) {
            logger.debug("Session not found for userId: " + userId);
            return false;
        }

        Session session = sessionOptional.get();
        logger.debug("Session Found!");

        // Check if the session token matches and is not expired
        boolean isSessionValid = jwtService.validateToken(session.getToken(), userId);
        logger.debug("IsSessionValid?:" + isSessionValid);

        return isSessionValid;
    }

}
