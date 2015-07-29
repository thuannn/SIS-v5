package com.lemania.sis.server;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class Cours extends DatastoreObject implements Comparable<Cours>{
	private String coursNom;
	private Boolean coursActif;
	
	@Index
	Key<Ecole> ecole;
	
	@IgnoreSave
	String schoolName;
	
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Key<Ecole> getEcole() {
		return ecole;
	}

	public void setEcole(Key<Ecole> ecole) {
		this.ecole = ecole;
	}

	public String getCoursNom() {
		return coursNom;
	}
	
	public void setCoursNom(String coursNom) {
		this.coursNom = coursNom;
	}
	
	public Boolean getCoursActif() {
		return coursActif;
	}
	
	public void setCoursActif(Boolean coursActif) {
		this.coursActif = coursActif;
	}
	
	@Override
	public int compareTo(Cours cours) {
		int lastCourseName = coursNom.compareTo( cours.getCoursNom() );
		return lastCourseName;
	}
}