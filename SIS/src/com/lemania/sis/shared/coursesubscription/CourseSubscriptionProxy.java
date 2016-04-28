package com.lemania.sis.shared.coursesubscription;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.client.values.Repetition;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.coursesubscription.CourseSubscription;

@ProxyFor( value=CourseSubscription.class, locator=ObjectifyLocator.class )
public interface CourseSubscriptionProxy extends EntityProxy {

	//
	public String getProfessorName();
	public String getProfessor1Name();
	//
	public String getStudentName();
	//
	public String getSubjectName();
	//
	public boolean isR();
	public void setR(boolean r);
	public boolean isES();
	public void setES(boolean eS);
	//
	public String getDate();
	public void setDate(String date);
	//
	public String getNote();
	public void setNote(String note);
	//
	public String getNote1();
	public void setNote1(String note);
	//
	public String getRepetitionCode();
	//
	public Repetition getRep();
	public String getEndDate();
}
