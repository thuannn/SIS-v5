package com.lemania.sis.shared.bulletin;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.bulletin.Bulletin;

@ProxyFor(value=Bulletin.class, locator=ObjectifyLocator.class)
public interface BulletinProxy extends EntityProxy {
	//
	Long getId();
	
	public String getBulletinName();
	public void setBulletinName(String profileName);

	public Boolean getIsActive();
	public void setIsActive(Boolean isActive);
	
	public String getClasseName();	
	public void setClasseName(String classeName);
	
	public String getStudentName();	
	public void setStudentName(String studentName);
	
	public String getYear();
	public void setYear(String year);
	
	public String getProgrammeName();
	public void setProgrammeName(String programmeName);
	
	public String getRemarqueDirection();
	public void setRemarqueDirection(String remarqueDirection);
	
	public Boolean getIsFinished();
	public void setIsFinished(Boolean isFinished);
	
	public Long getStudentId();
	public Long getClassId();
}
