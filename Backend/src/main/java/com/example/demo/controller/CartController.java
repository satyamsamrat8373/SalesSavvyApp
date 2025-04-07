package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AddToCartRequest;
import com.example.demo.entities.Users;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.CartService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(originPatterns = "http://localhost:5173", allowCredentials = "true")
public class CartController {
	
				UserRepository urepo;
				CartService cserv;
				
				public CartController(UserRepository urepo, CartService cserv) {
					this.urepo = urepo;
					this.cserv = cserv;
				}
				
				@GetMapping("/items/count")
				public ResponseEntity<Integer> getCartItemCount(@RequestParam String username) {
					Users user = urepo.findByUsername(username).orElseThrow( ()-> new IllegalArgumentException("User not found with this " + username));
					int count = cserv.getCartItemCount(user.getUser_id());
					return ResponseEntity.ok(count);
				}
				
				@PostMapping("/add")
				public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> request) {
					 String username = (String) request.get("username");
				        int productId = (int) request.get("productId");
				        
				        System.out.println(username);
				        System.out.println("Product ID" + productId);

				        // Handle quantity: Default to 1 if not provided
				        int quantity = request.containsKey("quantity") ? (int) request.get("quantity") : 1;

				        // Fetch the user using username
				        Users user = urepo.findByUsername(username)
				                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

				        // Add the product to the cart
				        cserv.addToCart(user.getUser_id(), productId, quantity);
				        return ResponseEntity.status(HttpStatus.CREATED).build();
				    }
				
				@GetMapping("/items")
				public ResponseEntity<Map<String, Object>> getCartItems(HttpServletRequest request) {
					Users user = (Users) request.getAttribute("authenticatedUser");
					Map<String, Object> cartItems = cserv.getCartItems(user.getUser_id());
					return ResponseEntity.ok(cartItems);
				}
				
				@PutMapping("/update")
				public ResponseEntity<Void> updateCartItemQuantity(@RequestBody Map<String, Object> request) {
				String username = (String) request.get("username");
				int productId = (int) request.get("productId");
				int quantity = (int) request.get("quantity");
				// Fetch the user using username
				Users user = urepo.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
				// Update the cart item quantity
				cserv.updateCartItemQuantity(user.getUser_id(), productId, quantity);
				return ResponseEntity.status (HttpStatus.OK).build();
				}
				
				@DeleteMapping("/delete")
				public ResponseEntity<Void> deleteCartItem(@RequestBody Map<String, Object> request) {
				String username = (String) request.get("username");
				int productId = (int) request.get("productId");
				// Fetch the user using username
				Users user = urepo.findByUsername(username).orElseThrow(()-> new IllegalArgumentException("User not found with username: " + username));
				// Delete the cart item.
				cserv.deleteCartItem(user.getUser_id(), productId);
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				}
}
