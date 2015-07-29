package com.lemania.sis.shared.period;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.period.PeriodDao;
import com.lemania.sis.server.service.DaoServiceLocator;

public interface PeriodRequestFactory extends RequestFactory {
	
	@Service(value=PeriodDao.class, locator=DaoServiceLocator.class)
	interface PeriodRequestContext extends RequestContext {
		//	
		Request<Void> initialize();
		Request<List<PeriodProxy>> listAll();
		Request<List<PeriodProxy>> listAllByClassAndStatus(String classId, boolean active);
		Request<List<PeriodProxy>> listAllByClass(String classId);

		Request<Void> save(PeriodProxy c);
		Request<PeriodProxy> saveAndReturn(PeriodProxy pp);
		
		Request<PeriodProxy> addPeriod( String periodItemId, String classId, String description, int order, String note, boolean isActive );
		
		Request<Void> removePeriod(PeriodProxy classroom);
	}
	
	PeriodRequestContext periodRequestContext();

}
