package com.app.test.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.test.dto.UserDto;
import com.app.test.entites.User;
import com.app.test.repository.RoleRepository;
import com.app.test.repository.UserRepository;
import com.app.test.services.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UserController {
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserService userService;

	@GetMapping("/Users")
	public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request,
			@RequestParam(required = false) String user) {
		// tester si on est connecté si oui alors on affiche les users
		if (userService.isAuthenticate(request)) {
			try {
				List<User> Users = new ArrayList<User>();

				if (user == null)
					userRepository.findAll().forEach(Users::add);
				else
					userRepository.findUsers(user).forEach(Users::add);

				if (Users.isEmpty()) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}

				return new ResponseEntity<>(Users, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("/Users/{id}")
	public ResponseEntity<User> getUserById(HttpServletRequest request, @PathVariable("id") long id) {
		// tester si on est connecté si oui alors on affiche les users
		if (userService.isAuthenticate(request)) {
			Optional<User> UserData = userRepository.findById(id);

			if (UserData.isPresent()) {
				return new ResponseEntity<>(UserData.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/Users/login")
	public ResponseEntity<User> login(HttpServletResponse response, @RequestParam("user") String username,
			@RequestParam("password") String pwd) {

		User userDataByNom = userRepository.findByNom(username.toLowerCase().trim());
		if (pwd.trim().length() > 0 && username.trim().length() > 0 && userDataByNom != null
				&& userDataByNom.getPassword().equals(userService.getMd5(pwd.trim()))) {
			// appel du service de création d'un cookie
			userService.Authenticate(response);

			User user = new User();
			user.setId(userDataByNom.getId());
			user.setNom(username);
			user.setEmail(userDataByNom.getEmail());
			user.setPassword(userDataByNom.getPassword());
			user.setRoles(userDataByNom.getRoles());
			return new ResponseEntity<>(user, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/Users/create")
	public ResponseEntity<User> createUser(@RequestBody UserDto user) {

		User userDataByNom = userRepository.findByNom(user.getNom().toLowerCase());
		User userDataByEmail = userRepository.findByEmail(user.getEmail().toLowerCase());

		try {
			if (!userService.PasswordValidator(user.password.trim())) {
				System.out.println("Le mot de passe ne respecte pas les normes indiquées!");
				return new ResponseEntity<>(null, HttpStatus.CONFLICT);
			} else if (!user.password.trim().equals(user.confirm_password.trim())) {
				System.out.println("Les mots de passes sont incorrects");
				return new ResponseEntity<>(null, HttpStatus.CONFLICT);
				// si l'email est incorrect
			} else if (!userService.isEmailValid(user.getEmail().toLowerCase().trim())) {
				System.out.println("Format de l'email incorrect!");
				return new ResponseEntity<>(null, HttpStatus.CONFLICT);
				// si le user ou l'email existe déjà
			} else if (userDataByNom != null || userDataByEmail != null) {
				System.out.println("Ce user existe déjà!");
				return new ResponseEntity<>(null, HttpStatus.CONFLICT);
			} else {
				System.out.println("c'est bon!");
				User uSer = userService.CreateUser(user);
				return new ResponseEntity<>(uSer, HttpStatus.CREATED);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/Users/{id}")
	public ResponseEntity<User> updateUser(HttpServletRequest request, @PathVariable("id") long id,
			@RequestBody UserDto user) {
		// tester si on est connecté
		if (userService.isAuthenticate(request)) {
			Optional<User> UserData = userRepository.findById(id);

			if (UserData.isPresent()) {
				if (!userService.isEmailValid(user.getEmail().toLowerCase().trim())) {
					System.out.println("Format de l'email incorrect!");
					return new ResponseEntity<>(null, HttpStatus.CONFLICT);
				} else if (!userService.PasswordValidator(user.getPassword().trim())) {
					System.out.println("Le mot de passe ne respecte pas les normes indiquées!");
					return new ResponseEntity<>(null, HttpStatus.CONFLICT);
				} else {
					User uSer = userService.UpdateUser(id, user);
					return new ResponseEntity<>(uSer, HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@DeleteMapping("/Users/{id}")
	public ResponseEntity<HttpStatus> deleteUser(HttpServletRequest request, @PathVariable("id") long id) {
		// tester si on est connecté
		if (userService.isAuthenticate(request)) {
			try {
				userRepository.deleteById(id);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@DeleteMapping("/Users")
	public ResponseEntity<HttpStatus> deleteAllUsers(HttpServletRequest request) {
		// tester si on est connecté
		if (userService.isAuthenticate(request)) {
			try {
				userRepository.deleteAll();
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

	}

	@GetMapping("/Users/logout")
	public String fetchSignoutSite(HttpServletRequest request, HttpServletResponse response) {
		// tester si on est connecté
		if (userService.isAuthenticate(request)) {
			userService.logOut(response);
			return "Vous êtes hors ligne maintenant! à bientôt.";
		} else {
			return "Vous n'êtes pas connecté!";
		}
	}
}
