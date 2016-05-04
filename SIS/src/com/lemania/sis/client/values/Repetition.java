package com.lemania.sis.client.values;

public enum Repetition {
	
	// Objets directement construits
	NA ( "Unique" ),
	J1 ( "Quotidien" ),
	W1 ( "Chaque semaine" ), 
	W2 ( "Chaque 2 semaines" ), 
	W3 ( "Chaque 3 semaines" ), 
	M1 ( "Chaque mois" ), 
	M2 ( "Chaque 2 mois" ), 
	M3 ( "Chaque 3 mois" ); 

	private String name = "";

	// Constructeur
	Repetition(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}