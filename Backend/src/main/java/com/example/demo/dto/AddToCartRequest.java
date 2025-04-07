package com.example.demo.dto;

public class AddToCartRequest {
    private int productId;
    private Integer quantity;
    
    // Getters and setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}