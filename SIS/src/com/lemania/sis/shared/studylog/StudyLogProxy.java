package com.lemania.sis.shared.studylog;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.studylog.StudyLog;

@ProxyFor( value = StudyLog.class, locator = ObjectifyLocator.class )
public interface StudyLogProxy extends EntityProxy {
	//
	public Long getId();
	
	public String getLogTitle();
	public void setLogTitle(String logTitle);

	public String getLogContent();
	public void setLogContent(String logContent);

	public String getLogDate();
	public void setLogDate(String logDate);
	
	public String getFileName();
	public void setFileName(String fileName);

	public String getSubjectName();
	public String getStudentName();
	public String getClasseName();
}
