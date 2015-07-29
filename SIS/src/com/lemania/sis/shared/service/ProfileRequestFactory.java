package com.lemania.sis.shared.service;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.service.DaoServiceLocator;
import com.lemania.sis.server.service.ProfileDao;
import com.lemania.sis.shared.ProfileProxy;

public interface ProfileRequestFactory extends RequestFactory {
	//
	@Service(value=ProfileDao.class, locator=DaoServiceLocator.class)
	interface ProfileRequestContext extends RequestContext {
		//
		Request<List<ProfileProxy>> listAll();			
		Request<List<ProfileProxy>> listAllActive();
		Request<List<ProfileProxy>> listAllActiveByClass(String classId);
		
		Request<Void> save(ProfileProxy profile);
		Request<ProfileProxy> saveAndReturn(ProfileProxy newProfile);
		Request<ProfileProxy> saveAndReturn(String profileName, String classId);
		
		Request<Void> removeProfile(ProfileProxy profile);
		
		Request<Void> initialize();
	}
	
	ProfileRequestContext profileRequest();
}
