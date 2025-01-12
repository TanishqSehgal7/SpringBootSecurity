package com.example.SpringSecurityApp.SpringSecurityApp.advices;
import com.example.SpringSecurityApp.SpringSecurityApp.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ApiError apiError = new ApiError(
                exception.getMessage(),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAutnenticationException(AuthenticationException exception) {
        ApiError apiError = new ApiError(exception.getMessage(),
                HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException exception) {
        ApiError apiError = new ApiError(exception.getMessage(),
                HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }
}
