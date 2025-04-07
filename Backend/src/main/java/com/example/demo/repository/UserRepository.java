package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer>{

	
		public Optional<Users> findByUsername(String username);
		
		public Optional<Users> findByEmail(String email);
}
