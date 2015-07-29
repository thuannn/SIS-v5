package com.lemania.sis.server.bean.absenceitem;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.lemania.sis.server.DatastoreObject;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.bean.motifabsence.MotifAbsence;
import com.lemania.sis.server.bean.period.Period;
import com.lemania.sis.server.bean.professor.Professor;
import com.lemania.sis.server.bean.student.Student;

@Entity
@Index
public class AbsenceItem extends DatastoreObject implements Comparable<AbsenceItem> {
	
	@Load private Key<Student> keyStudent;
	@Load private Key<Period> keyPeriod;
	private Key<Professor> keyProf;
	private Key<Classe> keyClasse;
	@Load private Key<Subject> keySubject;
	private Key<MotifAbsence> keyMotif;
	private String codeAbsenceType = "";
	private String profComment  = "";
	private int lateMinutes = -1;
	private boolean justtified = false;
	private boolean parentNotified = false;
	private String strAbsenceDate = "";				// Format : YYYYMMDD
	private String adminComment  = "";
	private String notificationDateSMS = "";			// A delimited string, format YYYYMMDDHHMM|YYYYMMDDHHMM ...
	private String notificationDateEmail = "";			// A delimited string, format YYYYMMDDHHMM|YYYYMMDDHHMM ...
	
	@IgnoreSave private String periodId  = "";;
	@IgnoreSave private String studentId  = "";
	@IgnoreSave private String motifId = "";
	
	@IgnoreSave private String subjectName  = "";
	@IgnoreSave private String profName = "";
	@IgnoreSave private String studentName = "";
	@IgnoreSave private String periodDesc = "";
	@IgnoreSave private String className = "";
	
	public Key<Student> getKeyStudent() {
		return keyStudent;
	}
	public void setKeyStudent(Key<Student> keyStudent) {
		this.keyStudent = keyStudent;
	}
	public Key<Professor> getKeyProf() {
		return keyProf;
	}
	public void setKeyProf(Key<Professor> keyProf) {
		this.keyProf = keyProf;
	}
	public Key<Classe> getKeyClasse() {
		return keyClasse;
	}
	public void setKeyClasse(Key<Classe> keyClasse) {
		this.keyClasse = keyClasse;
	}
	public Key<Subject> getKeySubject() {
		return keySubject;
	}
	public void setKeySubject(Key<Subject> keySubject) {
		this.keySubject = keySubject;
	}
	public Key<Period> getKeyPeriod() {
		return keyPeriod;
	}
	public void setKeyPeriod(Key<Period> keyPeriod) {
		this.keyPeriod = keyPeriod;
	}
	public Key<MotifAbsence> getKeyMotif() {
		return keyMotif;
	}
	public void setKeyMotif(Key<MotifAbsence> keyMotif) {
		this.keyMotif = keyMotif;
	}
	public String getCodeAbsenceType() {
		return codeAbsenceType;
	}
	public void setCodeAbsenceType(String codeAbsenceType) {
		this.codeAbsenceType = codeAbsenceType;
	}
	public String getProfComment() {
		return (profComment == null)? "" : profComment;
	}
	public void setProfComment(String profComment) {
		this.profComment = profComment;
	}
	public int getLateMinutes() {
		return lateMinutes;
	}
	public void setLateMinutes(int lateMinutes) {
		this.lateMinutes = lateMinutes;
	}
	public boolean isJusttified() {
		return justtified;
	}
	public void setJusttified(boolean justtified) {
		this.justtified = justtified;
	}
	public boolean isParentNotified() {
		return parentNotified;
	}
	public void setParentNotified(boolean parentNotified) {
		this.parentNotified = parentNotified;
	}
	public String getPeriodId() {
		return periodId;
	}
	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStrAbsenceDate() {
		return strAbsenceDate;
	}
	public void setStrAbsenceDate(String strAbsenceDate) {
		this.strAbsenceDate = strAbsenceDate;
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
	public String getAdminComment() {
		return (adminComment == null)? "" : adminComment;
	}
	public void setAdminComment(String adminComment) {
		this.adminComment = adminComment;
	}
	public String getMotifId() {
		return motifId;
	}
	public void setMotifId(String motifId) {
		this.motifId = motifId;
	}
	public String getPeriodDesc() {
		return periodDesc;
	}
	public void setPeriodDesc(String periodDesc) {
		this.periodDesc = periodDesc;
	}
	
	
	@Override
	public int compareTo(AbsenceItem o) {
		// 
		return ( o.getStrAbsenceDate() + o.getPeriodDesc() ).compareTo( this.strAbsenceDate + this.periodDesc );
	}
	
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getNotificationDateSMS() {
		return notificationDateSMS;
	}
	public void setNotificationDateSMS(String notificationDate) {
		this.notificationDateSMS = notificationDate;
	}
	public String getNotificationDateEmail() {
		return notificationDateEmail;
	}
	public void setNotificationDateEmail(String notificationDate) {
		this.notificationDateEmail = notificationDate;
	}
	
}
