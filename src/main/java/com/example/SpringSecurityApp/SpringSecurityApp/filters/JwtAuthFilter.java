package com.example.SpringSecurityApp.SpringSecurityApp.filters;

import com.example.SpringSecurityApp.SpringSecurityApp.entities.User;
import com.example.SpringSecurityApp.SpringSecurityApp.services.JwtService;
import com.example.SpringSecurityApp.SpringSecurityApp.services.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
//        Industry standard of how the token is passed -> "Bearer {token}"

            if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
                filterChain.doFilter(request,response);
                return;
            }

            String token = requestTokenHeader.split("Bearer ")[1];

            Long userId = jwtService.getUserIdFromToken(token);

            if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.getUserById(userId);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null,null);

                usernamePasswordAuthenticationToken.setDetails(request);

                // put user into SpringSecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

            filterChain.doFilter(request,response); // -> passed the flow to the next filter in the chain
            // which is the UsernamePasswordAuthenticationFilter which checks authentication in SecurityContextHolder

            // now we can do something with the response after it is received back from the next filter chain
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request,response,null,e);
        }
    }
}
