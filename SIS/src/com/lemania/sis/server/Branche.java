package com.lemania.sis.server;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class Branche extends DatastoreObject {
	private String brancheName;
	private Double defaultCoef;
	private Boolean isActive;
	
	public String getBrancheName() {
		return brancheName;
	}
	
	public void setBrancheName(String brancheName) {
		this.brancheName = brancheName;
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
}