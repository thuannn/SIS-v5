package com.lemania.sis.server.bean.assignment;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.bean.professor.Professor;

@Entity
@Index
public class Assignment extends DatastoreObject implements Comparable<Assignment> {
	//
	private Key<Professor> prof;
	private Key<Classe> classe;
	private Key<Subject> subject;
	
	private Boolean active = false;
	
	@IgnoreSave
	private String classeName;
	
	@IgnoreSave
	private String classId;
	
	@IgnoreSave
	private String subjectId;
	
	@IgnoreSave
	private String programmeName;
	
	@IgnoreSave
	private String schoolName;
	
	@IgnoreSave
	private String subjectName;
	
	
	public String getSubjectName() {
		return subjectName;
	}


	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}


	public String getClasseName() {
		return classeName;
	}


	public void setClasseName(String classeName) {
		this.classeName = classeName;
	}


	public String getProgrammeName() {
		return programmeName;
	}


	public void setProgrammeName(String programmeName) {
		this.programmeName = programmeName;
	}
	
	
	public String getCourseName() {
		return classeName;
	}

	
	public void setCourseName(String classeeName) {
		this.classeName = classeeName;
	}

	
	public String getSchoolName() {
		return schoolName;
	}

	
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	
	public Key<Professor> getProf() {
		return prof;
	}
	
	
	public void setProf(Key<Professor> prof) {
		this.prof = prof;
	}
	
	
	public Key<Classe> getClasse() {
		return classe;
	}
	
	
	public void setClasse(Key<Classe> classe) {
		this.classe = classe;
	}

	
	public Boolean getActive() {
		return active;
	}

	
	public void setActive(Boolean active) {
		this.active = active;
	}


	public Key<Subject> getSubject() {
		return subject;
	}


	public void setSubject(Key<Subject> subject) {
		this.subject = subject;
	}


	public String getClassId() {
		return classId;
	}
	
	


	public void setClassId(String classId) {
		this.classId = classId;
	}


	@Override
	public int compareTo(Assignment a) {
		int result1;
		int result = this.programmeName.compareTo(a.getProgrammeName());
		if (result != 0)
			return result;
		else {
			result1 = this.classeName.compareTo(a.getClasseName());
			if (result1 != 0)
				return result1;
			else
				return this.subjectName.compareTo(a.getSubjectName());
		}
			
	}


	public String getSubjectId() {
		return subjectId;
	}


	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
}
