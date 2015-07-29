package com.lemania.sis.server.bean.evaluations;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;
import com.lemania.sis.server.bean.student.Student;

@Entity
@Index
public class EvaluationStudentReport extends DatastoreObject {
	//
	private boolean isPublished = false;
	private String evaluationNote = "";
	
	private Key<EvaluationHeader> evaluationHeader;
	private Key<Student> student;
	
	public Key<Student> getStudent() {
		return student;
	}
	
	public void setStudent(Key<Student> student) {
		this.student = student;
	}

	public boolean isPublished() {
		return isPublished;
	}

	public void setPublished(boolean isPublished) {
		this.isPublished = isPublished;
	}

	public String getEvaluationNote() {
		return evaluationNote;
	}

	public void setEvaluationNote(String evaluationNote) {
		this.evaluationNote = evaluationNote;
	}

	public Key<EvaluationHeader> getEvaluationHeader() {
		return evaluationHeader;
	}

	public void setEvaluationHeader(Key<EvaluationHeader> evaluationHeader) {
		this.evaluationHeader = evaluationHeader;
	}
	
	
}
