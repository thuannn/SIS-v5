package com.lemania.sis.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.evaluations.EvaluationHeader;

@ProxyFor(value=EvaluationHeader.class, locator=ObjectifyLocator.class)
public interface EvaluationHeaderProxy extends EntityProxy {
	//
	Long getId();
	
	public String getFromDate();
	public void setFromDate(String fromDate);
	
	public String getToDate();
	public void setToDate(String toDate);
	
	public String getObjective();
	public void setObjective(String objective);
	
	public String getSchoolYear();
	public void setSchoolYear(String schoolYear);
	
	public String getClassId();
	public String getClassMasterId(); 
	
	public String getClassMasterName();
	public void setClassMasterName(String classMasterName);
}
