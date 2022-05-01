package com.app.test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.test.entites.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.nom LIKE %?1% OR email LIKE %?1%")
	List<User> findUsers(String user);

	User findByNom(String user);
	
	User findByEmail(String user);
}
