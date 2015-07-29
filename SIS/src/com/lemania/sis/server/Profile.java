package com.lemania.sis.server;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.bean.classe.Classe;

@Entity
@Index
public class Profile extends DatastoreObject {
	//
	private String profileName = "";
	private Boolean isActive = true;
	
	Key<Classe> classe;

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Key<Classe> getClasse() {
		return classe;
	}

	public void setClasse(Key<Classe> classe) {
		this.classe = classe;
	}
}
