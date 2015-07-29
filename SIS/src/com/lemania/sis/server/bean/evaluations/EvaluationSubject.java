package com.lemania.sis.server.bean.evaluations;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.bean.professor.Professor;
import com.lemania.sis.server.bean.student.Student;

@Entity
@Index
public class EvaluationSubject extends DatastoreObject{
	//
	private String objective1 = "";
	private String objective2 = "";
	private String objective3 = "";
	private String objective4 = "";
	private String objective5 = "";
	private String objective6 = "";
	
	private String studentName = "";
	private String subjectName = "";
	private String professorName = "";
	
	private Key<Subject> subject;
	private Key<Classe> classe;
	private Key<Professor> prof;
	private Key<Student> student;
	private Key<EvaluationHeader> evaluationHeader;
	
	public String getObjective1() {
		return objective1;
	}
	public void setObjective1(String objective1) {
		this.objective1 = objective1;
	}
	public String getObjective2() {
		return objective2;
	}
	public void setObjective2(String objective2) {
		this.objective2 = objective2;
	}
	public String getObjective3() {
		return objective3;
	}
	public void setObjective3(String objective3) {
		this.objective3 = objective3;
	}
	public String getObjective4() {
		return objective4;
	}
	public void setObjective4(String objective4) {
		this.objective4 = objective4;
	}
	public String getObjective5() {
		return objective5;
	}
	public void setObjective5(String objective5) {
		this.objective5 = objective5;
	}
	public String getObjective6() {
		return objective6;
	}
	public void setObjective6(String objective6) {
		this.objective6 = objective6;
	}
	public Key<Subject> getSubject() {
		return subject;
	}
	public void setSubject(Key<Subject> subject) {
		this.subject = subject;
	}
	public Key<Classe> getClasse() {
		return classe;
	}
	public void setClasse(Key<Classe> classe) {
		this.classe = classe;
	}
	public Key<Professor> getProf() {
		return prof;
	}
	public void setProf(Key<Professor> prof) {
		this.prof = prof;
	}
	public Key<Student> getStudent() {
		return student;
	}
	public void setStudent(Key<Student> student) {
		this.student = student;
	}
	public Key<EvaluationHeader> getEvaluationHeader() {
		return evaluationHeader;
	}
	public void setEvaluationHeader(Key<EvaluationHeader> evaluationHeader) {
		this.evaluationHeader = evaluationHeader;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getProfessorName() {
		return professorName;
	}
	public void setProfessorName(String professorName) {
		this.professorName = professorName;
	}
}
