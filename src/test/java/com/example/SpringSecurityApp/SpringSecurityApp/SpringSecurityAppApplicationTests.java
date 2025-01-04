package com.example.SpringSecurityApp.SpringSecurityApp;

import com.example.SpringSecurityApp.SpringSecurityApp.entities.User;
import com.example.SpringSecurityApp.SpringSecurityApp.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringSecurityAppApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads() {

		User user = new User(4L, "Tan@gmail.com", "Tan1234");

		String token = jwtService.generateToken(user);

		System.out.println("JWT Token: " + token);

		Long id = jwtService.getUserIdFromToken(token);

		System.out.println("User ID: " + id);
	}

}
