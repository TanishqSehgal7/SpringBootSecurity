package com.example.SpringSecurityApp.SpringSecurityApp.controllers;

import com.example.SpringSecurityApp.SpringSecurityApp.dto.LoginDto;
import com.example.SpringSecurityApp.SpringSecurityApp.dto.SignUpDto;
import com.example.SpringSecurityApp.SpringSecurityApp.dto.UserDto;
import com.example.SpringSecurityApp.SpringSecurityApp.services.AuthService;
import com.example.SpringSecurityApp.SpringSecurityApp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;
    private AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto) {
        UserDto userDto =  userService.signUp(signUpDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        return ResponseEntity.ok(token);
    }
}
