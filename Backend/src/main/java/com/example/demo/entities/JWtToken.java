package com.example.demo.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="jwt_tokens")
public class JWtToken {
	
			@Id
			@GeneratedValue(strategy= GenerationType.IDENTITY)
			private Integer token_id;
			
			@ManyToOne
			@JoinColumn(name="user_id")
			private Users user_id;
			
			@Column
			private String token;
			
			@Column
			private LocalDateTime created_at;
			
			@Column
			private LocalDateTime expires_at;

			public JWtToken() {
				super();
				// TODO Auto-generated constructor stub
			}

			public JWtToken(Integer token_id, Users user_id, String token, LocalDateTime created_at,
					LocalDateTime expires_at) {
				super();
				this.token_id = token_id;
				this.user_id = user_id;
				this.token = token;
				this.created_at = created_at;
				this.expires_at = expires_at;
			}

			public JWtToken(Users user_id, String token, LocalDateTime created_at, LocalDateTime expires_at) {
				super();
				this.user_id = user_id;
				this.token = token;
				this.created_at = created_at;
				this.expires_at = expires_at;
			}

			public Integer getToken_id() {
				return token_id;
			}

			public void setToken_id(Integer token_id) {
				this.token_id = token_id;
			}

			public Users getUser_id() {
				return user_id;
			}

			public void setUser_id(Users user_id) {
				this.user_id = user_id;
			}

			public String getToken() {
				return token;
			}

			public void setToken(String token) {
				this.token = token;
			}

			public LocalDateTime getCreated_at() {
				return created_at;
			}

			public void setCreated_at(LocalDateTime created_at) {
				this.created_at = created_at;
			}

			public LocalDateTime getExpires_at() {
				return expires_at;
			}

			public void setExpires_at(LocalDateTime expires_at) {
				this.expires_at = expires_at;
			}

			
}
