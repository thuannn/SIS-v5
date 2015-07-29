package com.lemania.sis.server;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class Ecole extends DatastoreObject{
	private String schoolName;
	private String schoolAddress;
	private Boolean schoolActive = false;
	
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSchoolAddress() {
		return schoolAddress;
	}

	public void setSchoolAddress(String schoolAddress) {
		this.schoolAddress = schoolAddress;
	}

	public Boolean getSchoolStatus() {
		return schoolActive;
	}

	public void setSchoolStatus(Boolean schoolActive) {
		this.schoolActive = schoolActive;
	}
}
