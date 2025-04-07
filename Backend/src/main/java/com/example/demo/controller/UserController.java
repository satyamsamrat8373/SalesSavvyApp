package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Users;
import com.example.demo.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins= "http://localhost:5173",allowCredentials = "true")
@RequestMapping("/api/users")
public class UserController {

	
				UserService userv;
				
				public UserController(UserService userv) {
					this.userv = userv;
				}
				
				@PostMapping("/register")
				public ResponseEntity<?> registerUser(@RequestBody Users user) {
					try {
						Users ruser= userv.register(user);
						return ResponseEntity.ok(Map.of("message", "User Registered Successfully", "username", ruser));
					} 
					catch(Exception e) {
						e.printStackTrace();
						return ResponseEntity.badRequest().body(Map.of("Error",e.getMessage()));
						
					}
					
				
				}
				
				// In UserController.java, add this new endpoint
				@GetMapping("/profile")
				public ResponseEntity<?> getUserProfile(HttpServletRequest request) {
				    try {
				        Users user = (Users) request.getAttribute("authenticatedUser");
				        if (user == null) {
				            return ResponseEntity.status(401).body(Map.of("error", "User not authenticated"));
				        }
				        
				        Map<String, Object> userProfile = new HashMap<>();
				        userProfile.put("userId", user.getUser_id());
				        userProfile.put("username", user.getUsername());
				        userProfile.put("email", user.getEmail());
				        userProfile.put("role", user.getRole());
				        userProfile.put("created_at", user.getCreated_at());
				        userProfile.put("updated_at", user.getUpdated_at());
				        
				        return ResponseEntity.ok(userProfile);
				    } catch (Exception e) {
				        e.printStackTrace();
				        return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
				    }
				}
}
