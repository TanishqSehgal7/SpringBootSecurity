package com.example.SpringSecurityApp.SpringSecurityApp.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "session_id")
    private Long sessionId;  // Unique identifier for session (e.g., UUID)

    @Column(name = "user_id", nullable = false)
    private String userId;  // User ID associated with the session

    @Column(name = "token", nullable = false)
    private String token;  // JWT token associated with the session

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;  // Timestamp when the session was created

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;  // Timestamp when the session expires

    @Column(name = "isSession_active", nullable = false)
    private Boolean isSessionActive;

    public Session() {}

    public Session(Long sessionId, String userId, String token, LocalDateTime createdAt, LocalDateTime expiresAt, Boolean isSessionActive) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.isSessionActive = isSessionActive;
    }

    // Getters and setters


    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getSessionActive() {
        return isSessionActive;
    }

    public void setSessionActive(Boolean sessionActive) {
        isSessionActive = sessionActive;
    }
}
