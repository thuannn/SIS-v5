package com.lemania.sis.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.SettingOption;

@ProxyFor(value=SettingOption.class, locator=ObjectifyLocator.class)
public interface SettingOptionProxy extends EntityProxy {
	
	public String getOptionName();
	public void setOptionName(String optionName);
	
	public String getOptionValue();
	public void setOptionValue(String optionValue);
}
