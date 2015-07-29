package com.lemania.sis.shared.masteragendaitem;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.masteragendaitem.MasterAgendaItem;

@ProxyFor( value=MasterAgendaItem.class, locator=ObjectifyLocator.class )
public interface MasterAgendaItemProxy extends EntityProxy {
	//
	Long getId();

	public String getJourCode();
	public void setJourCode(String jourCode);
	
	public int getDuration();
	public void setDuration(int duration);
	
	public String getPeriodDescription();
	public String getSubjectName();
	public String getProfName();
	public String getClassroomName();
	public String getPeriodId();
}
