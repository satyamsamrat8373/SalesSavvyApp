package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Product;
import com.example.demo.entities.Users;
import com.example.demo.services.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(originPatterns = "http://localhost:5173", allowCredentials = "true")
public class ProductController {

			ProductService pserv;
			
			public ProductController(ProductService pserv) {
				this.pserv = pserv;
			}
			
			
			@GetMapping
			public ResponseEntity<Map<String,Object>> getProducts(@RequestParam String category, HttpServletRequest request) {
			try {
				Users authenticatedUser = (Users) request.getAttribute("authenticatedUser");
				if (authenticatedUser == null) {
				return ResponseEntity.status(401).body(Map.of("Error", "Unauthorized Access"));
				}
				
				
			List<Product> products = pserv.getProductByCategory(category);
			Map<String, Object> response = new HashMap<>();
			Map<String, String> userInfo = new HashMap<>();
			userInfo.put("name", authenticatedUser.getUsername());
			userInfo.put("role", authenticatedUser.getRole().name());
			
			response.put("user", userInfo);
			
			List<Map<String, Object>> productList = new ArrayList<>();
			
				for(Product product : products) {
					Map<String, Object> productDetails = new HashMap<>();
					productDetails.put("product_id", product.getProductId());
					productDetails.put("name",product.getName());
					productDetails.put("description",product.getDescription());
					productDetails.put("price",product.getPrice());
					productDetails.put("stock",product.getStock());
					
					
					List<String> images = pserv.getProductImage(product.getProductId());
					productDetails.put("images", images);
					productList.add(productDetails);
				}
					response.put("products", productList);
					return ResponseEntity.ok(response);
				} catch(Exception e) {
						return ResponseEntity.badRequest().body(Map.of("Error", e.getMessage()));
				}
			}
 }
