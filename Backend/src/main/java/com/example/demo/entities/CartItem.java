package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="cart_items")
public class CartItem {
	
			@Id
			@GeneratedValue(strategy = GenerationType.IDENTITY)
			private Integer id;
			
			@ManyToOne
			@JoinColumn(name="user_id")
			private Users user;
			
			@ManyToOne
			@JoinColumn(name="product_id")
			private Product product;
			
			@Column
			private int quantity;

			public CartItem() {
				super();
				// TODO Auto-generated constructor stub
			}

			public CartItem(Integer id, Users user, Product product, int quantity) {
				super();
				this.id = id;
				this.user = user;
				this.product = product;
				this.quantity = quantity;
			}

			public CartItem(Users user, Product product, int quantity) {
				super();
				this.user = user;
				this.product = product;
				this.quantity = quantity;
			}

			public Integer getId() {
				return id;
			}

			public void setId(Integer id) {
				this.id = id;
			}

			public Users getUser() {
				return user;
			}

			public void setUser(Users user) {
				this.user = user;
			}

			public Product getProduct() {
				return product;
			}

			public void setProduct(Product product) {
				this.product = product;
			}

			public int getQuantity() {
				return quantity;
			}

			public void setQuantity(int quantity) {
				this.quantity = quantity;
			}
			
			

}
