package com.lemania.sis.shared.motifabsence;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.motifabsence.MotifAbsence;

@ProxyFor( value=MotifAbsence.class, locator=ObjectifyLocator.class )
public interface MotifAbsenceProxy extends EntityProxy {
	
	Long getId();
	
	public String getMotifCode();
	public void setMotifCode(String motifCode);
	
	public String getMotifLabel();
	public void setMotifLabel(String motifLabel);

	public boolean isReceivable();
	public void setReceivable(boolean isReceivable);

	public boolean isOutside();
	public void setOutside(boolean isOutside);

	public boolean isHealth();
	public void setHealth(boolean isHealth);

	public boolean isDispensable();
	public void setDispensable(boolean isDispensable);

	public String getTextLetter();
	public void setTextLetter(String textLetter);

	public String getTextSMS();
	public void setTextSMS(String textSMS);
}
