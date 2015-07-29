package com.lemania.sis.server.bean.studylog;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.bean.student.Student;

@Index
@Entity
public class StudyLog extends DatastoreObject {
	
	// A log is about a subject, for a student, do not care about which professor or which class
	private Key<Subject> keySubject;
	private Key<Classe> keyClasse;
	
	// If a log is for a specific student, set the boolean as true and set the student key
	private boolean isForStudent = false;
	private Key<Student> keyStudent;
	
	// Log content
	String logTitle = "";
	String logContent = "";
	String logDate = "";				// YYYYMMDD
	String fileName = "";
	
	//
	@IgnoreSave
	String subjectName = "";

	@IgnoreSave
	String studentName = "";
	
	@IgnoreSave
	String classeName = "";
	//

	public String getLogTitle() {
		return logTitle;
	}

	public void setLogTitle(String logTitle) {
		this.logTitle = logTitle;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public String getLogDate() {
		return logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Key<Subject> getKeySubject() {
		return keySubject;
	}

	public void setKeySubject(Key<Subject> keySubject) {
		this.keySubject = keySubject;
	}

	public Key<Student> getKeyStudent() {
		return keyStudent;
	}

	public void setKeyStudent(Key<Student> keyStudent) {
		this.keyStudent = keyStudent;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Key<Classe> getKeyClasse() {
		return keyClasse;
	}

	public void setKeyClasse(Key<Classe> keyClasse) {
		this.keyClasse = keyClasse;
	}

	public String getClasseName() {
		return classeName;
	}

	public void setClasseName(String classeName) {
		this.classeName = classeName;
	}

	public boolean isForStudent() {
		return isForStudent;
	}

	public void setForStudent(boolean isForStudent) {
		this.isForStudent = isForStudent;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
