package com.lemania.sis.server.bean.profilesubject;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;
import com.lemania.sis.server.Profile;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.professor.Professor;

@Entity
@Index
public class ProfileSubject extends DatastoreObject {
	//
	private Key<Subject> subject;
	private Key<Profile> profile;
	private Boolean isActive = true;
	//
	private Key<Professor> professor;
	private Key<Professor> professor1;
	private Key<Professor> professor2;
	//
	private String subjectName = "";
	private Double subjectCoef;
	//
	private String profName = "";
	private String prof1Name = "";
	private String prof2Name = "";
	
	// This number should always be 1
	private Double totalBrancheCoef = 0.0;
	
	//
	
	public Key<Profile> getProfile() {
		return profile;
	}
	
	public void setProfile(Key<Profile> profile) {
		this.profile = profile;
	}
	
	public Key<Subject> getSubject() {
		return subject;
	}
	
	public void setSubject(Key<Subject> subject) {
		this.subject = subject;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Double getSubjectCoef() {
		return subjectCoef;
	}

	public void setSubjectCoef(Double subjectCoef) {
		this.subjectCoef = subjectCoef;
	}

	public Double getTotalBrancheCoef() {
		if (totalBrancheCoef == null)
			return 0.0;
		else
			return totalBrancheCoef;
	}

	public void setTotalBrancheCoef(Double totalBrancheCoef) {
		this.totalBrancheCoef = totalBrancheCoef;
	}

	public Key<Professor> getProfessor() {
		return professor;
	}

	public void setProfessor(Key<Professor> professor) {
		this.professor = professor;
	}

	public String getProfName() {
		if (profName == null)
			return "";
		else
			return profName;
	}

	public void setProfName(String profName) {
		this.profName = profName;
	}

	public Key<Professor> getProfessor1() {
		return professor1;
	}

	public void setProfessor1(Key<Professor> professor1) {
		this.professor1 = professor1;
	}

	public Key<Professor> getProfessor2() {
		return professor2;
	}

	public void setProfessor2(Key<Professor> professor2) {
		this.professor2 = professor2;
	}

	public String getProf1Name() {
		return prof1Name;
	}

	public void setProf1Name(String prof1Name) {
		this.prof1Name = prof1Name;
	}

	public String getProf2Name() {
		return prof2Name;
	}

	public void setProf2Name(String prof2Name) {
		this.prof2Name = prof2Name;
	}
}
