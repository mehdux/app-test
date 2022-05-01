package com.app.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.test.entites.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findBySymbole(Integer sym);
}
