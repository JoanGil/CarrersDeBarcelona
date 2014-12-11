package com.example.carrersdebarcelona;

import java.io.Serializable;

public class Carrer implements Serializable {

    private String nom;
    private String descripcio;
    private String font;
    private String data;
    private String noms_anteriors;
    private String districte;


	public Carrer() {
    }

    public Carrer(String nom, String descripcio, String font, String data, String noms_anteriors, String districte) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.font = font;
        this.data = data;
        this.noms_anteriors = noms_anteriors;
        this.districte = districte;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getDescripcio() {
		return descripcio;
	}

	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

    public String getNoms_anteriors() {
		return noms_anteriors;
	}

	public void setNoms_anteriors(String noms_anteriors) {
		this.noms_anteriors = noms_anteriors;
	}

	public String getDistricte() {
		return districte;
	}

	public void setDistricte(String districte) {
		this.districte = districte;
	}



}
