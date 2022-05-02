L'api tourne avec java 11.0.12

Repository : https://github.com/mehdux/app-test.git

Se positionner dans le dossier apres avoir cloné le repos.

Lancer l'api avec la commande suivante : 

	mvn spring-boot:run

Suite des eventuelles actions (jeu de test) de l'api :

1) Pour la création d'un nouvel utilisateur :
	http://localhost:8080/api/Users/create 
	
	Data JSON :
	
	{
		"nom": "user3",
		"email":"user3@test.fr",
		"password":"Alger@1967",
		"confirm_password": "Alger@1967",
		"id_roles" : [4,2]
	}
	
	info : pour le mot de passe faut une majuscule, un symbole, un chiffre et une tail de plus de 8 positions
	
	lors de la création d'un nouvel utilisateur il faut que le nom et l'email n'existent dans la base de données au préalable.
	
2) Pour la connexion :

	http://localhost:8080/api/Users/login
	
	il y a une photo en fichier attaché qui résume la situation avec le nom : connexion.jpg
	
	les parametres doivent etres mis dans l'onglet "body" puis sous onglet "x-www-form-urlencoded"
	
3) lister les users (faut être connecté pour que ça marche) :

	http://localhost:8080/api/Users
	
	il y a une photo qui résume l'action en fichier attaché avec le nom : listeUser.jpg
	
4) Détail utilisateur (faut être connecté pour que ça marche) :

	http://localhost:8080/api/Users/3
	
5) Modification d'un utilisateur méthode put (faut être connecté pour que ça marche):

	http://localhost:8080/api/Users/3
	
6) Déconnexion (faut être connecté pour que ça marche) :

	http://localhost:8080/api/Users/logout
	
7) Suppression d'un utilisateur (faut être connecté pour que ça marche) avec la méthode DELETE:

	http://localhost:8080/api/Users
	

	
	
