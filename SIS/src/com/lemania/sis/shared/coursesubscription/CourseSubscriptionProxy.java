package com.lemania.sis.shared.coursesubscription;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.coursesubscription.CourseSubscription;

@ProxyFor( value=CourseSubscription.class, locator=ObjectifyLocator.class )
public interface CourseSubscriptionProxy extends EntityProxy {

	//
	public String getProfessorName();
	public String getStudentName();
	//
	public String getDate();
	public void setDate(String date);

}
