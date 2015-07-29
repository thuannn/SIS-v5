package com.lemania.sis.server.bean.masteragendaitem;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;
import com.lemania.sis.server.Profile;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.classroom.Classroom;
import com.lemania.sis.server.bean.period.Period;
import com.lemania.sis.server.bean.professor.Professor;

@Entity
@Index
public class MasterAgendaItem extends DatastoreObject implements Comparable<MasterAgendaItem> {
	//
	private String jourCode = "";
	private Key<Period> period;
	private Key<Profile> profile;
	private Key<Subject> subject;
	private Key<Professor> prof;
	private Key<Classroom> classroom;
	private int duration;
	
	@IgnoreSave
	private String periodDescription;
	@IgnoreSave
	private String subjectName;
	@IgnoreSave
	private String profName;
	@IgnoreSave
	private String classroomName;
	@IgnoreSave
	private String periodId;
	
	
	public String getJourCode() {
		return jourCode;
	}
	public void setJourCode(String jourCode) {
		this.jourCode = jourCode;
	}
	
	public Key<Period> getPeriod() {
		return period;
	}
	public void setPeriod(Key<Period> period) {
		this.period = period;
	}
	
	public Key<Profile> getProfile() {
		return profile;
	}
	public void setProfile(Key<Profile> profile) {
		this.profile = profile;
	}
	
	public Key<Subject> getSubject() {
		return subject;
	}
	public void setSubject(Key<Subject> subject) {
		this.subject = subject;
	}
	
	public Key<Professor> getProf() {
		return prof;
	}
	public void setProf(Key<Professor> prof) {
		this.prof = prof;
	}
	
	public Key<Classroom> getClassroom() {
		return classroom;
	}
	public void setClassroom(Key<Classroom> classroom) {
		this.classroom = classroom;
	}
	
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public String getPeriodDescription() {
		return periodDescription;
	}
	public void setPeriodDescription(String periodDescription) {
		this.periodDescription = periodDescription;
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
	
	public String getClassroomName() {
		return classroomName;
	}
	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}
	public String getPeriodId() {
		return periodId;
	}
	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	@Override
	public int compareTo(MasterAgendaItem o) {
		//
		return this.jourCode.equals( o.getJourCode() ) ? 1 : -1 ;
	}
}
