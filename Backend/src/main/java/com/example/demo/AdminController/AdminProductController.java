package com.example.demo.AdminController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.AdminService.AdminProductService;
import com.example.demo.entities.Product;

@RestController
@RequestMapping("/admin/products")
@CrossOrigin(origins="http://localhost:5173", allowCredentials = "true")
public class AdminProductController {
			AdminProductService ads;
			
			public AdminProductController(AdminProductService ads) {
				this.ads = ads;
			}
			
			@PostMapping("/add")
			public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> productRequest) {
				try {
						String name = (String) productRequest.get("name");
						String description = (String) productRequest.get("description");
						Double price = Double.valueOf(String.valueOf(productRequest.get("price")));
						int  stock = (int) productRequest.get("stock");
						Integer categoryId = (Integer) productRequest.get("categoryId");
						String imageUrl = (String) productRequest.get("imageUrl");
						
						Product addedProduct = ads.addProductWithImage(name, description, price, stock,categoryId, imageUrl);
						
						Map<String,Object> response = new HashMap<>();
						response.put("product", addedProduct);
						response.put("imageUrl", imageUrl);
						
						return ResponseEntity.status(HttpStatus.CREATED).body(response);
					} catch (IllegalArgumentException e) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
					} catch (Exception e) {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
					}
			}
			
			
					@DeleteMapping("/delete")
					public ResponseEntity<?> deleteProduct(@RequestBody Map<String, Integer> requestBody) {
					try {
						Integer productId = (Integer) requestBody.get("productId");
							ads.deleteProduct (productId);
							return ResponseEntity.status(HttpStatus.OK).body ("Product Deleted Successfully");
						} catch (IllegalArgumentException e) {
							return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
						} catch (Exception e) {
							return ResponseEntity.status (HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
						}
					}
}
