package com.app.test.entites;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter //génération automatique des getters
@Setter //génération automatique des setters
@NoArgsConstructor //génération d'un constructeur sans argument
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private Integer symbole;

	@Column(name = "nom")
	private String nom;

	public Role(Integer symbole, String nom) {
		this.symbole = symbole;
		this.nom = nom;
	}
}
