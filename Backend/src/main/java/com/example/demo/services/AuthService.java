package com.example.demo.services;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.JWtToken;
import com.example.demo.entities.Users;
import com.example.demo.repository.JWTTokenRepo;
import com.example.demo.repository.UserRepository;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {

			private final Key SIGNING_KEY;
			UserRepository urepo;
			JWTTokenRepo jrepo;
			BCryptPasswordEncoder bpe;
			
			public AuthService(UserRepository urepo, JWTTokenRepo jrepo, @Value("${jwt.secret}") String jwtSecret) {
				this.urepo = urepo;
				this.jrepo = jrepo;
				this.bpe = new BCryptPasswordEncoder();
				this.SIGNING_KEY = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
				
			}
			public Users authenticate(String username, String password) throws RuntimeException{
			    Optional<Users> existinguser = urepo.findByUsername(username);
			    if (existinguser.isPresent()) {
			    	Users user = existinguser.get();
			    	if (!bpe.matches(password, user.getPassword())) {
			    		throw new RuntimeException("Invalid Password");
			    	
			    	}
			    	return user;
			    }
			    else {
			    	throw new RuntimeException("Invalid Username");
			    }
			    	
			}
			
			public boolean validateToken(String token) {
				System.out.println("validating Token");
				try {
					Jwts.parserBuilder()
					.setSigningKey(SIGNING_KEY)
					.build()
					.parseClaimsJws(token);
					
					Optional<JWtToken> jwtToken = jrepo.findByToken(token);
					if (jwtToken.isPresent()) {
						return jwtToken.get().getExpires_at().isAfter(LocalDateTime.now());			
					}
					return false;
				} 
				catch(Exception e) {
					System.out.println("Token vakidation Failed " + e.getMessage());
					return false;
				}
			}
 			
			
			
			public String generateToken(Users user) {
				String token;
				LocalDateTime currentTime = LocalDateTime.now();
				JWtToken existingToken = jrepo.findByUserId(user.getUser_id());
				if (existingToken != null && currentTime.isBefore(existingToken.getExpires_at())) {
					token = existingToken.getToken();				
				
			} else {
				token = generateNewToken(user);
				if(existingToken != null) {
					jrepo.delete(existingToken);
				}
				saveToken(user, token);
			}
				return token;
		}
			
			public String generateNewToken(Users user) {
				JwtBuilder builder = Jwts.builder();
				builder.setSubject(user.getUsername());
				builder.claim("role",user.getRole().name());
				builder.setIssuedAt(new Date());
				builder.setExpiration(new Date(System.currentTimeMillis()+3600000));
				builder.signWith(SIGNING_KEY);
				String token = builder.compact();
				return token;
			}
			
			public void saveToken(Users user, String token) {
				JWtToken jwt = new JWtToken(user,token,LocalDateTime.now(),LocalDateTime.now().plusHours(1));
				jrepo.save(jwt);
			}
			
			
			public String extractUsername(String token)
			{
				return Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(token).getBody().getSubject();
				
			}
			
			public void logout(Users user) {
				jrepo.deleteByUserId(user.getUser_id());
			}
 }
