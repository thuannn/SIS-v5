package com.lemania.sis.shared.perioditem;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.perioditem.PeriodItem;

@ProxyFor( value=PeriodItem.class, locator=ObjectifyLocator.class )
public interface PeriodItemProxy extends EntityProxy {
	
	public Long getId();
	
	public String getPeriod();
	public void setPeriod(String period);

	public String getNote();
	public void setNote(String note);

	public boolean isActive();
	public void setActive(boolean isActive);
	
	public int getFromHour();
	public void setFromHour(int fromHour);

	public int getFromMinute();
	public void setFromMinute(int fromMinute);

	public int getToHour();
	public void setToHour(int toHour);

	public int getToMinute();	
	public void setToMinute(int toMinute);

}
