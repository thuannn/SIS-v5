package com.lemania.sis.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.Subject;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

@ProxyFor(value=Subject.class, locator=ObjectifyLocator.class)
public interface SubjectProxy extends EntityProxy {
	Long getId();
		
	public String getSubjectName();
	public void setSubjectName(String subjectName);
	
	public Double getDefaultCoef();
	public void setDefaultCoef(Double defaultCoef);
	
	public Boolean getIsActive();
	public void setIsActive(Boolean isActive);
	
	public String getSubjectName2();
	public void setSubjectName2(String subjectName2);


	/*
	 * GXT Grid
	 * */
	public interface SubjectProxyProperties extends PropertyAccess<SubjectProxy> {
		//
		ModelKeyProvider<SubjectProxy> getId();
		
		ValueProvider<SubjectProxy, String> subjectName();		
		ValueProvider<SubjectProxy, String> subjectName2();
		ValueProvider<SubjectProxy, Double> defaultCoef();
		ValueProvider<SubjectProxy, Boolean> isActive();
	}

}


