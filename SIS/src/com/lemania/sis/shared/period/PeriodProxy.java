package com.lemania.sis.shared.period;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.period.Period;

@ProxyFor( value=Period.class, locator=ObjectifyLocator.class )
public interface PeriodProxy extends EntityProxy {
	//
	Long getId();

	public String getDescription();
	public void setDescription(String description);
	
	public String getNote();
	public void setNote(String note);
	
	public boolean isActive();
	public void setActive(boolean isActive);

	public int getOrder();
	public void setOrder(int order);
	
	public String getPeriodText();
	public void setPeriodText(String periodText);
}
