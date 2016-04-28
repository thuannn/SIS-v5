package com.lemania.sis.server.bean.coursesubscription;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.client.values.Repetition;
import com.lemania.sis.server.DatastoreObject;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.professor.Professor;
import com.lemania.sis.server.bean.student.Student;

@Entity
@Index
public class CourseSubscription extends DatastoreObject implements Comparable<CourseSubscription> {
	
	//
	Key<Student> student;
	Key<Professor> prof;		// Professor who add subscription
	Key<Professor> prof1;		// Follow up professsor
	Key<Subject> subject;
	//
	String date = "";
	String note = "";
	String note1 = "";
	//
	boolean R = false;
	boolean ES = false;
	//
	String repetitionCode = "";
	Repetition rep;
	String endDate = "";
	//
	@IgnoreSave String professorName = "";
	@IgnoreSave String professor1Name = "";
	@IgnoreSave String studentName = "";
	@IgnoreSave String subjectName = "";
	

	public String getProfessorName() {
		return professorName;
	}

	public void setProfessorName(String professorName) {
		this.professorName = professorName;
	}
	
	public String getProfessor1Name() {
		return professor1Name;
	}

	public void setProfessor1Name(String professorName) {
		this.professor1Name = professorName;
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
	
	public Key<Professor> getProf1() {
		return prof1;
	}

	public void setProf1(Key<Professor> prof) {
		this.prof1 = prof;
	}

	@Override
	public int compareTo(CourseSubscription o) {
		// TODO Auto-generated method stub
		return this.getStudentName().compareTo(o.getStudentName());
	}

	public String getNote1() {
		return note1;
	}

	public void setNote1(String note1) {
		this.note1 = note1;
	}

	public Key<Subject> getSubject() {
		return subject;
	}

	public void setSubject(Key<Subject> subject) {
		this.subject = subject;
	}

	public boolean isR() {
		return R;
	}

	public void setR(boolean r) {
		R = r;
	}

	public boolean isES() {
		return ES;
	}

	public void setES(boolean eS) {
		ES = eS;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getRepetitionCode() {
		return repetitionCode;
	}

	public void setRepetitionCode(String repetitionCode) {
		this.repetitionCode = repetitionCode;
	}

	public Repetition getRep() {
		return rep;
	}

	public void setRep(Repetition rep) {
		this.rep = rep;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
