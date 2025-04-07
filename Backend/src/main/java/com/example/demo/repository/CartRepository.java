package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.CartItem;

import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer>{

	
			@Query("SELECT c FROM CartItem c WHERE c.user.id = :userId AND c.product.id = :productId")
			Optional<CartItem> findByUserAndProduct(@Param("userId") int userId, @Param("productId") int productId);
		
			
			@Query("SELECT COALESCE(SUM(c.quantity), 0) FROM CartItem c WHERE c.user.id = :userId")
			int countTotalItems(@Param("userId") int userId);
		
			@Query("SELECT c FROM CartItem c JOIN FETCH c.product WHERE c.user.id = :userId")
			List<CartItem> findCartItemsWithProductDetails(@Param("userId") int userId);


			@Modifying
			@Transactional
			@Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.id = :cartItemId")
			void updatecartItemQuantity(@Param("cartItemId") int cartItemId, @Param("quantity") int quantity);


			
			@Modifying
			@Transactional
			@Query("DELETE FROM CartItem c WHERE c.user.id = :userId AND c.product.id = :productId")
			void deleteCartItem(@Param("userId") int userId, @Param("productId") int productId);

			
			@Modifying
			@Transactional
			@Query("DELETE FROM CartItem c WHERE c.user.id = :userId")
			void deleteAllCartItemsByUser(@Param("userId") int userId);

	
	
}
