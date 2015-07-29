package com.lemania.sis.shared.service;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.service.DaoServiceLocator;
import com.lemania.sis.server.service.ProfileBrancheDao;
import com.lemania.sis.shared.ProfileBrancheProxy;

public interface ProfileBrancheRequestFactory extends RequestFactory {
	//
	@Service(value=ProfileBrancheDao.class, locator=DaoServiceLocator.class)
	interface ProfileBrancheRequestContext extends RequestContext {
		//
		//
		Request<List<ProfileBrancheProxy>> listAll();		
		Request<List<ProfileBrancheProxy>> listAll( String profileSubjectId );	
		//
		Request<List<ProfileBrancheProxy>> listAllActive();
		
		Request<Void> save(ProfileBrancheProxy profile);
		Request<ProfileBrancheProxy> saveAndReturn(ProfileBrancheProxy newProfileSubject);
		Request<ProfileBrancheProxy> saveAndReturn(String profileSubjectId, String brancheId, String brancheCoef );
		
		Request<Void> removeProfileBranche(ProfileBrancheProxy profileBranche);
		
		Request<Void> initialize();
	}
	
	ProfileBrancheRequestContext profileBrancheRequest();
}
