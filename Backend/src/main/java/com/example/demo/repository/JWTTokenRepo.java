package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.JWtToken;

import jakarta.transaction.Transactional;

@Repository
public interface JWTTokenRepo extends JpaRepository<JWtToken,Integer>{
	
	
	    @Query("SELECT j FROM JWtToken j WHERE j.user_id.user_id = :userId")
	    JWtToken findByUserId(int userId);
	    
	    Optional<JWtToken> findByToken(String token);
	    
	    @Modifying
	    @Transactional
	    @Query("DELETE FROM JWtToken t WHERE t.user_id.user_id = :userId")
	    void deleteByUserId(int userId);
	    
	    
	    
	    
}
