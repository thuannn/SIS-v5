package com.lemania.sis.server;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.bean.profilesubject.ProfileSubject;

@Entity
@Index
public class ProfileBranche extends DatastoreObject {
	//
	private Key<ProfileSubject> profileSubject;
	private Key<Branche> profileBranche;
	
	private String profileBrancheName;
	private Double brancheCoef;
	
	public Double getBrancheCoef() {
		return brancheCoef;
	}
	
	public void setBrancheCoef(Double brancheCoef) {
		this.brancheCoef = brancheCoef;
	}

	public String getProfileBrancheName() {
		return profileBrancheName;
	}

	public void setProfileBrancheName(String profileBrancheName) {
		this.profileBrancheName = profileBrancheName;
	}

	public Key<Branche> getProfileBranche() {
		return profileBranche;
	}

	public void setProfileBranche(Key<Branche> profileBranche) {
		this.profileBranche = profileBranche;
	}

	public Key<ProfileSubject> getProfileSubject() {
		return profileSubject;
	}

	public void setProfileSubject(Key<ProfileSubject> profileSubject) {
		this.profileSubject = profileSubject;
	}
	
	
}
