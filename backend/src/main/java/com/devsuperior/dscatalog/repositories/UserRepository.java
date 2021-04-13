package com.devsuperior.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.User;

@Repository 
public interface UserRepository extends JpaRepository<User, Long>{
	
	//uma busca no banco por e-mail para usar o userInsertvalidator (caso if)
	User findByEmail(String email);
}
