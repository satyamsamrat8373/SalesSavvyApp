package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class Users {
	
			@Id
			@GeneratedValue(strategy = GenerationType.IDENTITY)
			private Integer user_id;
			
			@Column
			private String username;
			
			@Column
			private String email;
			
			@Column
			private String password;
			
			@Column
			@Enumerated(EnumType.STRING)
			private Role role;
			
			@Column
			LocalDateTime created_at = LocalDateTime.now();
			
			@Column
			LocalDateTime updated_at = LocalDateTime.now();
			
			@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)

			private List<CartItem> cartItems = new ArrayList<>();
			
			// When we build cart service we should add list<cart_items>

			public Users() {
				super();
				// TODO Auto-generated constructor stub
			}

			public Users(Integer user_id, String username, String email, String password, Role role,
					LocalDateTime created_at, LocalDateTime updated_at) {
				super();
				this.user_id = user_id;
				this.username = username;
				this.email = email;
				this.password = password;
				this.role = role;
				this.created_at = created_at;
				this.updated_at = updated_at;
			}

			public Users(String username, String email, String password, Role role, LocalDateTime created_at,
					LocalDateTime updated_at) {
				super();
				this.username = username;
				this.email = email;
				this.password = password;
				this.role = role;
				this.created_at = created_at;
				this.updated_at = updated_at;
			}

			public Integer getUser_id() {
				return user_id;
			}

			public void setUser_id(Integer user_id) {
				this.user_id = user_id;
			}

			public String getUsername() {
				return username;
			}

			public void setUsername(String username) {
				this.username = username;
			}

			public String getEmail() {
				return email;
			}

			public void setEmail(String email) {
				this.email = email;
			}

			public String getPassword() {
				return password;
			}

			public void setPassword(String password) {
				this.password = password;
			}

			public Role getRole() {
				return role;
			}

			public void setRole(Role role) {
				this.role = role;
			}

			public LocalDateTime getCreated_at() {
				return created_at;
			}

			public void setCreated_at(LocalDateTime created_at) {
				this.created_at = created_at;
			}

			public LocalDateTime getUpdated_at() {
				return updated_at;
			}

			public void setUpdated_at(LocalDateTime updated_at) {
				this.updated_at = updated_at;
			}

			public Users(Integer user_id, String username, String email, String password, Role role,
					LocalDateTime created_at, LocalDateTime updated_at, List<CartItem> cartItems) {
				super();
				this.user_id = user_id;
				this.username = username;
				this.email = email;
				this.password = password;
				this.role = role;
				this.created_at = created_at;
				this.updated_at = updated_at;
				this.cartItems = cartItems;
			}

			public List<CartItem> getCartItems() {
				return cartItems;
			}

			public void setCartItems(List<CartItem> cartItems) {
				this.cartItems = cartItems;
			}

			
}
