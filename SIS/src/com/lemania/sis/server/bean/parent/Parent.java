package com.lemania.sis.server.bean.parent;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;

@Entity
@Index
public class Parent extends DatastoreObject implements Comparable<Parent> {
	
	private String title;
	private String firstName;
	private String lastName;
	private String eMail;
	private String phoneMobile;
	private String phoneHome;
	private String phoneWork;
	boolean acceptSMS = false;
	boolean acceptEmail = false;
	private String childIds = "";
	
	@IgnoreSave String childrenNames = "";
	
	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String geteMail() {
		return eMail;
	}


	public void seteMail(String eMail) {
		this.eMail = eMail;
	}


	public String getPhoneMobile() {
		return phoneMobile;
	}


	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile;
	}


	public String getPhoneHome() {
		return phoneHome;
	}


	public void setPhoneHome(String phoneHome) {
		this.phoneHome = phoneHome;
	}


	public String getPhoneWork() {
		return phoneWork;
	}


	public void setPhoneWork(String phoneWork) {
		this.phoneWork = phoneWork;
	}


	public boolean isAcceptSMS() {
		return acceptSMS;
	}


	public void setAcceptSMS(boolean acceptSMS) {
		this.acceptSMS = acceptSMS;
	}


	public boolean isAcceptEmail() {
		return acceptEmail;
	}


	public void setAcceptEmail(boolean acceptEmail) {
		this.acceptEmail = acceptEmail;
	}


	public String getChildIds() {
		return childIds;
	}


	public void setChildIds(String childIds) {
		this.childIds = childIds;
	}


	@Override
	public int compareTo(Parent o) {
		//
		return this.lastName.compareTo(o.getLastName());
	}


	public String getChildrenNames() {
		return childrenNames;
	}


	public void setChildrenNames(String childrenNames) {
		this.childrenNames = childrenNames;
	}
	
}
