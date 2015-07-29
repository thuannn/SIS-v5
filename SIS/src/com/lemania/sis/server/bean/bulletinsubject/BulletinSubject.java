package com.lemania.sis.server.bean.bulletinsubject;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.bulletin.Bulletin;
import com.lemania.sis.server.bean.professor.Professor;

@Entity
@Index
public class BulletinSubject extends DatastoreObject implements Comparable<BulletinSubject> {
	//
	private Key<Subject> subject;
	private Key<Bulletin> bulletin;
	private Key<Professor> professor;
	//
	private Boolean isActive = true;
	private Double subjectCoef;
	//
	private String T1 = "";
	private String T2 = "";
	private String T3 = "";
	private String T4 = "";
	//
	private String examT1 = "";
	private String examT2 = "";
	private String examT3 = "";
	private String examT4 = "";
	//
	private String An = "";
	//
	private String remarqueT1 = "";
	private String remarqueT2 = "";
	private String remarqueT3 = "";
	private String remarqueT4 = "";
	//
	// This number should always be 1
	private Double totalBrancheCoef = 0.0;
	//
	// 20141202 - Multiple professors for one subject
	private Key<Professor> professor1;
	private Key<Professor> professor2;
	
	//
	//
	@IgnoreSave String subjectId;
	private String subjectName;
	//
	@IgnoreSave String profId;
	private String profName;
	//
	@IgnoreSave String prof1Id;
	@IgnoreSave private String prof1Name = "";
	//
	@IgnoreSave String prof2Id;
	@IgnoreSave private String prof2Name = "";
	//
	@IgnoreSave private String studentId;
	@IgnoreSave private String studentName;
	//
	@IgnoreSave String classId;
	
	
	
	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public Key<Subject> getSubject() {
		return subject;
	}
	
	public void setSubject(Key<Subject> subject) {
		this.subject = subject;
	}
	
	public Key<Bulletin> getBulletin() {
		return bulletin;
	}
	
	public void setBulletin(Key<Bulletin> bulletin) {
		this.bulletin = bulletin;
	}
	
	public Key<Professor> getProfessor() {
		return professor;
	}
	
	public void setProfessor(Key<Professor> professor) {
		this.professor = professor;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public Double getSubjectCoef() {
		return subjectCoef;
	}
	
	public void setSubjectCoef(Double subjectCoef) {
		this.subjectCoef = subjectCoef;
	}
	
	public String getSubjectName() {
		return subjectName;
	}
	
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	public String getProfName() {
		return profName;
	}
	
	public void setProfName(String profName) {
		this.profName = profName;
	}

	public Double getTotalBrancheCoef() {
		return totalBrancheCoef;
	}

	public void setTotalBrancheCoef(Double totalBrancheCoef) {
		this.totalBrancheCoef = totalBrancheCoef;
	}

	public String getT1() {
		return T1;
	}

	public void setT1(String t1) {
		T1 = t1;
	}

	public String getT2() {
		return T2;
	}

	public void setT2(String t2) {
		T2 = t2;
	}

	public String getT3() {
		return T3;
	}

	public void setT3(String t3) {
		T3 = t3;
	}

	public String getAn() {
		return An;
	}

	public void setAn(String an) {
		An = an;
	}

	public String getRemarqueT1() {
		return remarqueT1;
	}

	public void setRemarqueT1(String remarqueT1) {
		this.remarqueT1 = remarqueT1;
	}

	public String getRemarqueT2() {
		return remarqueT2;
	}

	public void setRemarqueT2(String remarqueT2) {
		this.remarqueT2 = remarqueT2;
	}

	public String getRemarqueT3() {
		return remarqueT3;
	}

	public void setRemarqueT3(String remarqueT3) {
		this.remarqueT3 = remarqueT3;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getExamT1() {
		return examT1;
	}

	public void setExamT1(String examT1) {
		this.examT1 = examT1;
	}

	public String getExamT2() {
		return examT2;
	}

	public void setExamT2(String examT2) {
		this.examT2 = examT2;
	}

	public String getExamT3() {
		return examT3;
	}

	public void setExamT3(String examT3) {
		this.examT3 = examT3;
	}

	@Override
	public int compareTo(BulletinSubject bs) {
		int result = this.studentName.compareTo( bs.getStudentName() );
		return result;
	}

	public String getT4() {
		return T4;
	}

	public void setT4(String t4) {
		T4 = t4;
	}

	public String getExamT4() {
		return examT4;
	}

	public void setExamT4(String examT4) {
		this.examT4 = examT4;
	}

	public String getRemarqueT4() {
		return remarqueT4;
	}

	public void setRemarqueT4(String remarqueT4) {
		this.remarqueT4 = remarqueT4;
	}

	public String getProfId() {
		return profId;
	}

	public void setProfId(String profId) {
		this.profId = profId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public Key<Professor> getProfessor1() {
		return professor1;
	}

	public void setProfessor1(Key<Professor> professor1) {
		this.professor1 = professor1;
	}

	public Key<Professor> getProfessor2() {
		return professor2;
	}

	public void setProfessor2(Key<Professor> professor2) {
		this.professor2 = professor2;
	}

	public String getProf1Id() {
		return prof1Id;
	}

	public void setProf1Id(String prof1Id) {
		this.prof1Id = prof1Id;
	}

	public String getProf1Name() {
		return prof1Name;
	}

	public void setProf1Name(String prof1Name) {
		this.prof1Name = prof1Name;
	}

	public String getProf2Id() {
		return prof2Id;
	}

	public void setProf2Id(String prof2Id) {
		this.prof2Id = prof2Id;
	}

	public String getProf2Name() {
		return prof2Name;
	}

	public void setProf2Name(String prof2Name) {
		this.prof2Name = prof2Name;
	}
}
