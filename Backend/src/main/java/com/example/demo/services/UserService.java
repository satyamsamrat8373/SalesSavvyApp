package com.example.demo.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Users;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	
	
		UserRepository urepo;
		BCryptPasswordEncoder passwordEncoder;
		
		public UserService(UserRepository urepo) {
			this.passwordEncoder =new BCryptPasswordEncoder();
			this.urepo = urepo;
		}

		
		public Users register(Users user) throws RuntimeException{
			
				if (urepo.findByUsername (user.getUsername()).isPresent()) 
				{
						throw new RuntimeException("Username is already taken");
				}
				
				if (urepo.findByEmail(user.getEmail()).isPresent()) 
				{
						throw new RuntimeException("Email is already registered");
				}
				
				user.setPassword (passwordEncoder.encode(user.getPassword()));
				
				return urepo.save(user);
				}
		
		
}
