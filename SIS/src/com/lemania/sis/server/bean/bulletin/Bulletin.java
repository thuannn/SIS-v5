package com.lemania.sis.server.bean.bulletin;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;
import com.lemania.sis.server.Profile;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.bean.student.Student;

@Entity
@Index
public class Bulletin extends DatastoreObject implements Comparable<Bulletin> {
	//
	private String bulletinName;
	private Boolean isActive = true;
	private String classeName;
	private String studentName;
	private String year;
	private String remarqueDirection;
	
	// When a bulletin is finished, a student can be assigned to another class, professors will not see him in the student list to input the grades
	private Boolean isFinished = false;
	//
	@IgnoreSave private String programmeName;
	//
	private Key<Classe> classe;
	private Key<Student> student;
	private Key<Profile> profile;
	
	//
	
	public String getBulletinName() {
		return bulletinName;
	}
	
	public void setBulletinName(String bulletinName) {
		this.bulletinName = bulletinName;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public String getClasseName() {
		return classeName;
	}
	
	public void setClasseName(String classeName) {
		this.classeName = classeName;
	}
	
	public String getStudentName() {
		return studentName;
	}
	
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	public Key<Classe> getClasse() {
		return classe;
	}
	
	public void setClasse(Key<Classe> classe) {
		this.classe = classe;
	}
	
	public Key<Student> getStudent() {
		return student;
	}
	
	public void setStudent(Key<Student> student) {
		this.student = student;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getProgrammeName() {
		return programmeName;
	}

	public void setProgrammeName(String programmeName) {
		this.programmeName = programmeName;
	}

	public Key<Profile> getProfile() {
		return profile;
	}

	public void setProfile(Key<Profile> profile) {
		this.profile = profile;
	}

	@Override
	public int compareTo(Bulletin bulletin) {
		int result = studentName.compareTo( bulletin.getStudentName() );
		return result;
	}

	public String getRemarqueDirection() {
		return (this.remarqueDirection == null)? "" : this.remarqueDirection;
	}

	public void setRemarqueDirection(String remarqueDirection) {
		this.remarqueDirection = remarqueDirection;
	}

	public Boolean getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(Boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	public Long getStudentId() {
		return this.getStudent().getId();
	}
	
	public Long getClassId() {
		return this.getClasse().getId();
	}
}
