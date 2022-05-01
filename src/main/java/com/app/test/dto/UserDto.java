package com.app.test.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter //génération automatique des getters
@Setter //génération automatique des setters
public class UserDto {

	public String nom;

	public String email;

	public String password;
	
	public String confirm_password;
	
	public List <Integer> id_roles;
}
