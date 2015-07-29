package com.lemania.sis.shared.bulletinsubject;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.bulletinsubject.BulletinSubject;

@ProxyFor(value=BulletinSubject.class, locator=ObjectifyLocator.class)
public interface BulletinSubjectProxy extends EntityProxy {
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
	
	//
	public String getT1();
	public void setT1(String t1);

	public String getT2();
	public void setT2(String t2);

	public String getT3();
	public void setT3(String t3);
	
	public String getT4();
	public void setT4(String t4);

	//
	public String getAn();
	public void setAn(String an);

	public String getRemarqueT1();
	public void setRemarqueT1(String remarqueT1);

	public String getRemarqueT2();
	public void setRemarqueT2(String remarqueT2);

	public String getRemarqueT3();
	public void setRemarqueT3(String remarqueT3);

	public String getRemarqueT4();
	public void setRemarqueT4(String remarqueT4);
	
	public String getExamT1();
	public void setExamT1(String examT1);

	public String getExamT2();
	public void setExamT2(String examT2);

	public String getExamT3();
	public void setExamT3(String examT3);
	
	public String getExamT4();
	public void setExamT4(String examT4);
	
	//
	public String getStudentName();
	public String getStudentId();
	//
	public String getProfId();
	public String getProf1Id();
	public String getProf2Id();
	//
	public String getClassId();
	public String getSubjectId();
}
