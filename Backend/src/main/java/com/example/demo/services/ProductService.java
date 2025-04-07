package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Category;
import com.example.demo.entities.Product;
import com.example.demo.entities.ProductImage;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {
	
			ProductRepository prepo;
			ProductImageRepository imagerepo;
			CategoryRepository crepo;
			
			public ProductService(ProductRepository prepo,ProductImageRepository imagerepo, CategoryRepository crepo) {
				this.prepo = prepo;
				this.imagerepo = imagerepo;
				this.crepo = crepo;
			}
			
			public List<Product> getProductByCategory(String categoryName) {
				if(categoryName != null && !categoryName.isEmpty()) {
					Optional<Category> categoryOpt = crepo.findBycategoryName(categoryName);
					if(categoryOpt.isPresent()) {
						Category category = categoryOpt.get();
						return prepo.findByCategory_CategoryId(category.getCategory_id());
					}
				else {
					throw new RuntimeException("Category Not Found");
					
					}
				} else {
					return prepo.findAll();
				}
			}
			
			public List<String> getProductImage(Integer productId) {
		        List<ProductImage> productImages = imagerepo.findByProduct_ProductId(productId);
		        List<String> imageUrls = new ArrayList<>();
		        for (ProductImage image : productImages) {
		            imageUrls.add(image.getImageUrl());
		        }
		        return imageUrls;
		    }
}
