package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
			public Optional<Category> findBycategoryName(String categoryName);
}
