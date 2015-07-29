package com.lemania.sis.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.Ecole;
import com.lemania.sis.server.ObjectifyLocator;

@ProxyFor(value=Ecole.class, locator=ObjectifyLocator.class)
public interface EcoleProxy extends EntityProxy {
	Long getId();
	
	String getSchoolName();
	void setSchoolName(String name);
	
	String getSchoolAddress();
	void setSchoolAddress(String name);
	
	Boolean getSchoolStatus();
	void setSchoolStatus(Boolean active);
}
