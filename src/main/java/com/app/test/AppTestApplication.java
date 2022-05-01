package com.app.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.app.test.entites.Role;
import com.app.test.repository.RoleRepository;
import com.app.test.utils.ERole;

@SpringBootApplication
public class AppTestApplication implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(AppTestApplication.class, args);
	}

	// insertion des données (les roles) initiales
	@Override
	public void run(String... args) throws Exception {

		List<Role> Roles = new ArrayList<>();
		List<ERole> roles = new ArrayList<ERole>(Arrays.asList(ERole.values()));

		for (int i = 0; i < roles.size(); i++) {
			int j = i + 1;
			Role role = roleRepository.findBySymbole(j);
			if (role == null) {
				Role insertRole = new Role(j, roles.get(i).toString());
				Roles.add(insertRole);
			}
		}
		if (Roles.size() > 0)
			roleRepository.saveAll(Roles);
	}
}
