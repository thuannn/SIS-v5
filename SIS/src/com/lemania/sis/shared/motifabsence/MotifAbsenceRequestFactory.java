package com.lemania.sis.shared.motifabsence;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.motifabsence.MotifAbsenceDao;
import com.lemania.sis.server.service.DaoServiceLocator;

public interface MotifAbsenceRequestFactory extends RequestFactory {
	
	@Service ( value=MotifAbsenceDao.class, locator=DaoServiceLocator.class )
	interface MotifAbsenceRequestContext extends RequestContext {
		//
		Request<List<MotifAbsenceProxy>> listAll();
		Request<Void> save(MotifAbsenceProxy motif); 
		Request<MotifAbsenceProxy> saveAndReturn(MotifAbsenceProxy motif);
		Request<Void> removeParent(MotifAbsenceProxy motif);
	}
	
	MotifAbsenceRequestContext motifAbsenceRequestContext();
}
