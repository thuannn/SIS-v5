package com.lemania.sis.shared.profilesubject;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.profilesubject.ProfileSubject;

@ProxyFor(value=ProfileSubject.class, locator=ObjectifyLocator.class)
public interface ProfileSubjectProxy extends EntityProxy {
	//
	Long getId();

	public String getSubjectName();
	public void setSubjectName(String subjectName);
	
	public Double getSubjectCoef();
	public void setSubjectCoef(Double subjectCoef);
	
	public Double getTotalBrancheCoef();
	public void setTotalBrancheCoef(Double totalBrancheCoef);
	
	public String getProfName();
	public void setProfName(String profName);
	
	public String getProf1Name();
	public void setProf1Name(String profName);
	
	public String getProf2Name();
	public void setProf2Name(String profName);

	public Boolean getIsActive();
	public void setIsActive(Boolean isActive);
}
