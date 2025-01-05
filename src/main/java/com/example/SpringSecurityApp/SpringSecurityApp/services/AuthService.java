package com.example.SpringSecurityApp.SpringSecurityApp.services;

import com.example.SpringSecurityApp.SpringSecurityApp.dto.LoginDto;
import com.example.SpringSecurityApp.SpringSecurityApp.entities.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                        loginDto.getPassword()));

        User user = (User) authentication.getPrincipal();

        return jwtService.generateToken(user);
    }
}
