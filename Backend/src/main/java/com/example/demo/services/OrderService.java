package com.example.demo.services;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.demo.entities.OrderItem;
import com.example.demo.entities.Users;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductImage;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductImageRepository;

@Service
public class OrderService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public OrderService(OrderItemRepository orderItemRepository, ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    public Map<String, Object> getOrdersForUser(Users user) {
        // Fetch all successful order items for the user
        List<OrderItem> orderItems = orderItemRepository.findSuccessfulOrderItemsByUserId(user.getUser_id());

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("role", user.getRole()); // Directly use the role as it is an enum mapped to a string

        // Transform order items into a list of product details
        List<Map<String, Object>> products = new ArrayList<>();
        
        for (OrderItem item : orderItems) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product == null) {
                continue; // Skip if the product does not exist
            }

            // Fetch the product image (if available)
            List<ProductImage> images = productImageRepository.findByProduct_ProductId(product.getProductId());
            String imageUrl = images.isEmpty() ? null : images.get(0).getImageUrl();

            // Create a product details map
            Map<String, Object> productDetails = new HashMap<>();
            productDetails.put("order_id", item.getOrder().getOrderId());
            productDetails.put("quantity", item.getQuantity());
            productDetails.put("total_price", item.getTotalPrice());
            productDetails.put("image_url", imageUrl);
            productDetails.put("product_id", product.getProductId());
            productDetails.put("name", product.getName());
            productDetails.put("description", product.getDescription());
            productDetails.put("price_per_unit", item.getPricePerUnit());

            products.add(productDetails);
        }

        // Add the products list to the response
        response.put("products", products);

        return response;
    }
}
