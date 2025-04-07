package com.example.demo.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entities.CartItem;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductImage;
import com.example.demo.entities.Users;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;

@Service
public class CartService {
	
			CartRepository crepo;
			UserRepository urepo;
			ProductRepository prepo;
			ProductImageRepository imagerepo;
			
			public CartService(CartRepository crepo, UserRepository urepo, ProductRepository prepo,ProductImageRepository imagerepo) {
				this.crepo = crepo;
				this.urepo = urepo;
				this.prepo = prepo;
				this.imagerepo = imagerepo;
			}

			public int getCartItemCount(int userId) {
				return crepo.countTotalItems(userId);
			}
			
			public void addToCart(int userId, int productId, int quanity) {
			    // Fix: Use userId to find user, not productId
			    Users user = urepo.findById(userId).orElseThrow( ()-> new IllegalArgumentException("User not found with this UserId" + userId));
			    Product product = prepo.findById(productId).orElseThrow( ()-> new IllegalArgumentException("Product not found with this productId" + productId));
			    
			    Optional<CartItem> existingItem = crepo.findByUserAndProduct(userId, productId);
			    
			    if(existingItem.isPresent()) {
			        CartItem  cartItem = existingItem.get();
			        cartItem.setQuantity(cartItem.getQuantity() + 1);
			        crepo.save(cartItem);
			    } else {
			        CartItem newItem = new CartItem(user,product,quanity);
			        crepo.save(newItem);
			    }
			}
			
			
			public Map<String, Object> getCartItems(int userId) {
				
				List<CartItem> cartItems = crepo.findCartItemsWithProductDetails(userId);
				
				Map<String, Object> response = new HashMap<>();
				Users user = urepo.findById(userId).orElseThrow( ()-> new IllegalArgumentException("User not found with this UserId"));
				
				response.put("username", user.getUsername()); 
				response.put("role", user.getRole().name());
				
				List<Map<String, Object>> products = new ArrayList<>();
				
				int overallTotalPrice = 0;
				
				for (CartItem cartItem: cartItems) {
					
					Map<String, Object> productDetails = new HashMap<>();
					Product product = cartItem.getProduct();
					List<ProductImage> productImages = imagerepo.findByProduct_ProductId(product.getProductId());
					String imageurls = null;
					
					if(productImages != null && !productImages.isEmpty()) {
						imageurls = productImages.get(0).getImageUrl();
					} else {
						imageurls = "default-image-url";
					}
				
				productDetails.put("product_id", product.getProductId()); 
				productDetails.put("image_url", imageurls); 
				productDetails.put("name", product.getName()); 
				productDetails.put("description", product.getDescription()); 
				productDetails.put("price_per_unit", product.getPrice()); 
				productDetails.put("quantity", cartItem.getQuantity()); 
				productDetails.put("total_price", cartItem.getQuantity() * product.getPrice().doubleValue());
				
				products.add(productDetails);
				
				overallTotalPrice += cartItem.getQuantity()* product.getPrice().doubleValue();
			
			}
				Map<String, Object> cart = new HashMap<>();
				cart.put("products", products); 
				cart.put("overall_total_price", overallTotalPrice);
				response.put("cart", cart);
				return response;
			}
			
			public void updateCartItemQuantity(int userId, int productId, int quantity) {
				Users user = urepo.findById(userId).orElseThrow(()-> new IllegalArgumentException("User not found"));
				Product product = prepo.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
				// Fetch cart item for this userId and productId
				Optional<CartItem> existingItem = crepo.findByUserAndProduct(userId, productId);
				if (existingItem.isPresent()) {
						CartItem cartItem = existingItem.get();
						if (quantity == 0) {
								deleteCartItem(userId, productId);
						} else {
							cartItem.setQuantity (quantity);
							crepo.save(cartItem);
						}
					}
			}
			
			public void deleteCartItem(int userId, int productId) {
				Users user = urepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
				Product product = prepo.findById(productId).orElseThrow(()-> new IllegalArgumentException("Product not found"));
				crepo.deleteCartItem(userId, productId);
			}
}
