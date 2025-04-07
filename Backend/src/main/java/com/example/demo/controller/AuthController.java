package com.example.demo.controller;

import java.util.HashMap;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.entities.Users;
import com.example.demo.services.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(originPatterns = "http://localhost:5173", allowCredentials = "true")
public class AuthController {
	
			AuthService authservice;
			
			public AuthController(AuthService authservice)
			{
				this.authservice = authservice;
			}
			
			@PostMapping("/login")
			@CrossOrigin(origins= "http://localhost:5173")
			public ResponseEntity<?> login (@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
			try {
					Users user = authservice.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
			
					String token = authservice.generateToken(user);
			
					Cookie cookie = new Cookie("authToken", token);
					cookie.setHttpOnly(true);
					cookie.setSecure (false); 
					cookie.setPath("/");
					cookie.setMaxAge (3600); 
					cookie.setDomain("localhost"); 
					response.addCookie(cookie);
			
					response.addHeader("Set-Cookie", 
					String.format("authToken=%s; HttpOnly; Path=/; Max-Age=3600; SameSite=None", token));
					
					Map<String, Object> responseBody =  new HashMap<>();
					responseBody.put("message", "Login successful");
					responseBody.put("username", user.getUsername());
					responseBody.put("role", user.getRole().name());
					return ResponseEntity.ok(responseBody);
					} catch (RuntimeException e) {
					return ResponseEntity.status (HttpStatus.UNAUTHORIZED).body (Map.of("error", e.getMessage()));
					}
			}
			
			@PostMapping("/logout") 
			public ResponseEntity<Map<String, String>> logout(HttpServletRequest request,HttpServletResponse response) { 
				try { 
					Users user=(Users) request.getAttribute("authenticatedUser"); 
					authservice.logout(user); 
					Cookie cookie = new Cookie("authToken", null); 
					cookie.setHttpOnly(true); 
					cookie.setMaxAge(0); 
					cookie.setPath("/"); 
					response.addCookie(cookie); 
					
					response.addHeader("Set-Cookie", 
							String.format("authToken=%s; HttpOnly; Path=/; Max-Age=3600; SameSite=None", null));
					
					Map<String, String> responseBody = new HashMap<>(); 
					responseBody.put("message", "Logout successful"); 
					
					return ResponseEntity.ok(responseBody); 
					} 
					catch (RuntimeException e) { 
						Map<String, String> errorResponse = new HashMap<>(); 
						errorResponse.put("message", "Logout failed"); 
						return ResponseEntity.status(500).body(errorResponse); 
						} 
				}
}
