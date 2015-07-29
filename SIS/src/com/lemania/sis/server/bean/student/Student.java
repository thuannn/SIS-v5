package com.lemania.sis.server.bean.student;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;

@Entity
@Index
public class Student extends DatastoreObject implements Comparable<Student> {
	private String FirstName;
	private String LastName;
	private String Email;
	private Boolean isActive = false;
	
	public String getFirstName() {
		return FirstName;
	}
	
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	
	public String getLastName() {
		return LastName;
	}
	
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	
	public String getEmail() {
		return Email;
	}
	
	public void setEmail(String email) {
		Email = email;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public int compareTo(Student o) {
		//
		return ( this.LastName + this.FirstName ).compareTo( o.getLastName() + o.getFirstName() );
	}
}