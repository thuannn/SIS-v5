package com.lemania.sis.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.professor.Professor;

@ProxyFor(value=Professor.class, locator=ObjectifyLocator.class)
public interface ProfessorProxy extends EntityProxy {
	Long getId();
	public String getProfName();
	public void setProfName(String profName);
	
	public Boolean getProfActive();
	public void setProfActive(Boolean profActive);
	
	public String getLogModifyDate();
	public void setLogModifyDate(String date);
	
	public String getProfEmail();
	public void setProfEmail(String profEmail);
	
	public double getTotal_01();
	public double getTotal_02();
	public double getTotal_03();
	public double getTotal_04();
	public double getTotal_05();
	public double getTotal_06();
	public double getTotal_07();
	public double getTotal_08();
	public double getTotal_09();
	public double getTotal_10();
	public double getTotal_11();
	public double getTotal_12();
	
	public double getFee_01();
	public double getFee_02();
	public double getFee_03();
	public double getFee_04();
	public double getFee_05();
	public double getFee_06();
	public double getFee_07();
	public double getFee_08();
	public double getFee_09();
	public double getFee_10();
	public double getFee_11();
	public double getFee_12();
}