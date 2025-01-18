package com.example.SpringSecurityApp.SpringSecurityApp.filters;

import com.example.SpringSecurityApp.SpringSecurityApp.entities.User;
import com.example.SpringSecurityApp.SpringSecurityApp.services.JwtService;
import com.example.SpringSecurityApp.SpringSecurityApp.services.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String requestTokenHeader = request.getHeader("Authorization");

            // Check if Authorization header exists and starts with 'Bearer'
            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response); // Pass the request further if no token
                return;
            }

            String token = requestTokenHeader.split("Bearer ")[1]; // Extract token
            logger.info("RequestTokenHeader: {}", token);

            Long userId = jwtService.getUserIdFromToken(token);
            logger.info("UserId From JwtToken: {}", userId);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.getUserById(userId);

                // Check if the token has expired
                if (jwtService.isTokenExpired(token)) {
                    logger.debug("JWT is Expired for userId: {}", userId);
                    throw new JwtException("JWT token is expired"); // Token is expired, throw an exception
                }

                // Create authentication token if JWT is valid
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(request);

                // Set authentication to Spring Security context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response); // Continue filter chain
        } catch (JwtException e) {
            // Log the error if an exception occurs
            logger.error("JWT Exception occurred: ", e);
            // Pass the exception to the handler to return a response
            handlerExceptionResolver.resolveException(request, response, null, e);
        } catch (Exception e) {
            // General exception catch block to ensure any other unexpected issues are logged
            logger.error("Exception in JwtAuthFilter: ", e);
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}

