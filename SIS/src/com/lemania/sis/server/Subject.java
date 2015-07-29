package com.lemania.sis.server;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class Subject extends DatastoreObject {
	//
	private String subjectName;
	private String subjectName2 = "";		// Used in bilingual school
	private Double defaultCoef;
	private Boolean isActive;
	
	public String getSubjectName() {
		return subjectName;
	}
	
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	public Double getDefaultCoef() {
		return defaultCoef;
	}
	
	public void setDefaultCoef(Double defaultCoef) {
		this.defaultCoef = defaultCoef;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getSubjectName2() {
		return subjectName2;
	}

	public void setSubjectName2(String subjectName2) {
		this.subjectName2 = subjectName2;
	}
}
