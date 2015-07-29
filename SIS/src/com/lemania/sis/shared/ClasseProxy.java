package com.lemania.sis.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.bean.classe.Classe;

@ProxyFor(value=Classe.class, locator=ObjectifyLocator.class)
public interface ClasseProxy extends EntityProxy {
	//
	Long getId();
	
	public String getClassName();
	public void setClassName(String className);
	
	public Boolean getIsActive();
	public void setIsActive(Boolean isActive);

	public String getProgrammeName();
	public void setProgrammeName(String programmeName);
}
