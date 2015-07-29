package com.lemania.sis.shared;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.lemania.sis.server.ObjectifyLocator;
import com.lemania.sis.server.ProfileBranche;

@ProxyFor(value=ProfileBranche.class, locator=ObjectifyLocator.class)
public interface ProfileBrancheProxy extends EntityProxy {
	//
	Long getId();
	
	public Double getBrancheCoef();
	public void setBrancheCoef(Double brancheCoef);
	
	public String getProfileBrancheName();
	public void setProfileBrancheName(String profileBrancheName);
}
