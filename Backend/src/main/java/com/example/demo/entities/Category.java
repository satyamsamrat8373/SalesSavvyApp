package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @Column
    private String categoryName;

    // Getters and Setters
    public int getCategory_id() {
        return categoryId;
    }

    public void setCategory_id(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory_name() {
        return categoryName;
    }

    public void setCategory_name(String categoryName) {
        this.categoryName = categoryName;
    }
}