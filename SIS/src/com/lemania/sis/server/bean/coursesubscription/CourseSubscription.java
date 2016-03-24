package com.lemania.sis.server.bean.coursesubscription;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.bean.professor.Professor;
import com.lemania.sis.server.bean.student.Student;

@Entity
@Index
public class CourseSubscription extends DatastoreObject implements Comparable<CourseSubscription> {
	
	//
	Key<Student> student;
	Key<Professor> prof;
	
	//
	String date = "";
	String note = "";
	
	//
	@IgnoreSave String professorName = "";
	@IgnoreSave String studentName = "";
	

	public String getProfessorName() {
		return professorName;
	}

	public void setProfessorName(String professorName) {
		this.professorName = professorName;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Key<Student> getStudent() {
		return student;
	}

	public void setStudent(Key<Student> student) {
		this.student = student;
	}

	public Key<Professor> getProf() {
		return prof;
	}

	public void setProf(Key<Professor> prof) {
		this.prof = prof;
	}

	@Override
	public int compareTo(CourseSubscription o) {
		// TODO Auto-generated method stub
		return this.getStudentName().compareTo(o.getStudentName());
	}
	
}
