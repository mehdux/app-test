package com.app.test.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.test.dto.UserDto;
import com.app.test.entites.Role;
import com.app.test.entites.User;
import com.app.test.repository.RoleRepository;
import com.app.test.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;

	// service de création d'un nouvelle utilsateur
	public User CreateUser(UserDto user) {
		List<Role> roles = new ArrayList<>();
		if (user.getId_roles().size() > 0)
			for (Integer symRole : user.getId_roles()) {
				Role role = roleRepository.findBySymbole(symRole);
				roles.add(role);
			}
		User _User = new User();
		_User.setNom(user.getNom().toLowerCase().trim());
		_User.setEmail(user.getEmail().toLowerCase().trim());
		_User.setPassword(getMd5(user.getPassword().trim()));
		_User.setRoles(roles);
		User uSer = userRepository.save(_User);
		return uSer;
	}

	// service de mise à jour d'un utilisateur existant
	public User UpdateUser(long id, UserDto user) {
		Optional<User> UserData = userRepository.findById(id);
		List<Role> roles = new ArrayList<>();
		if (user.getId_roles().size() > 0)
			for (Integer symRole : user.getId_roles()) {
				Role role = roleRepository.findBySymbole(symRole);
				roles.add(role);
			}
		
		User userDataByNom = userRepository.findByNom(user.getNom().toLowerCase().trim());
		User userDataByEmail = userRepository.findByEmail(user.getEmail().toLowerCase().trim());
		
		User _User = UserData.get();
		_User.setNom(userDataByNom == null && user.getNom().trim().length() > 0 ? user.getNom().trim().toLowerCase() : _User.getNom());
		_User.setEmail(userDataByEmail == null && user.getEmail().trim().length() > 0 ? user.getEmail().trim().toLowerCase() : _User.getEmail());
		_User.setPassword(user.getPassword().trim().length() > 0 ? getMd5(user.getPassword().trim()) : _User.getPassword());
		_User.setRoles(roles);
		User uSer = userRepository.save(_User);
		return uSer;
	}

	// service de création de cookie
	public Cookie Authenticate(HttpServletResponse response) {
		Cookie cookie = new Cookie("user-id", "c2FtLnNtaXRoQGV4YW1wbGUuY29t");

		cookie.setMaxAge(86400);
		cookie.setSecure(false);
		cookie.setHttpOnly(true);
		cookie.setDomain("localhost");

		response.addCookie(cookie);
		return cookie;
	}

	// service de mise hors ligne
	public Cookie logOut(HttpServletResponse response) {
		Cookie cookie = new Cookie("user-id", "");

		cookie.setMaxAge(0);
		cookie.setSecure(false);
		cookie.setHttpOnly(true);
		cookie.setDomain("localhost");

		response.addCookie(cookie);
		return cookie;
	}

	// tester si on est connecté ou pas
	public boolean isAuthenticate(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		boolean found = false;

		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user-id") && cookie.getMaxAge() != 0) {
					found = true;
					System.out.println(cookie.getName() + " : " + cookie.getValue());
				}
			}
		}
		return found;
	}

	// service pour tester le password
	public boolean PasswordValidator(String password) {

		// chiffre + caractère minuscule + caractère majuscule + ponctuation + symbole
		final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

		final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public String getMd5(String input) {
		try {
			// La méthode statique getInstance est appelée avec le hachage MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// la méthode digest() est appelée pour calculer le résumé du message
			// d'une entrée digest() renvoie un tableau d'octets
			byte[] messageDigest = md.digest(input.getBytes());

			// Convertit le tableau d'octets en représentation signum
			BigInteger no = new BigInteger(1, messageDigest);

			// Convertit le résumé du message en valeur hexadécimale
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}
		// Pour spécifier des algorithmes de résumé de message erronés
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	// tester le format de l'email
	public boolean isEmailValid(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}
}
