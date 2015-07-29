package com.lemania.sis.shared.service;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.service.CoursDao;
import com.lemania.sis.server.service.DaoServiceLocator;
import com.lemania.sis.shared.CoursProxy;

public interface CoursRequestFactory extends RequestFactory {
	
	@Service(value=CoursDao.class, locator=DaoServiceLocator.class)
	interface CoursRequestContext extends RequestContext {
		Request<List<CoursProxy>> listAll();
		
		Request<List<CoursProxy>> listAll(String ecoleId);
		Request<List<CoursProxy>> listAllActive(String ecoleId);
		
		Request<Void> save(CoursProxy cours);
		Request<Void> save(CoursProxy cours, String ecoleId);
		
		Request<CoursProxy> saveAndReturn(CoursProxy newCours);
		Request<Void> removeCours(CoursProxy cours);
		
		Request<Void> initialize();
	}
	
	CoursRequestContext coursRequest();
}
