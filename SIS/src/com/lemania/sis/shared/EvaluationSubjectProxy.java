package com.lemania.sis.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.evaluations.EvaluationSubject;

@ProxyFor(value=EvaluationSubject.class, locator=ObjectifyLocator.class)
public interface EvaluationSubjectProxy extends EntityProxy {
	//
	Long getId();
	
	public String getObjective1();
	public void setObjective1(String objective1);
	
	public String getObjective2();
	public void setObjective2(String objective2);
	
	public String getObjective3();
	public void setObjective3(String objective3);
	
	public String getObjective4();
	public void setObjective4(String objective4);
	
	public String getObjective5();
	public void setObjective5(String objective5);
	
	public String getObjective6();
	public void setObjective6(String objective6);
	
	public String getStudentName();
	public void setStudentName(String studentName);
	
	public String getSubjectName();
	public void setSubjectName(String subjectName);
	
	public String getProfessorName();
	public void setProfessorName(String professorName);
}
