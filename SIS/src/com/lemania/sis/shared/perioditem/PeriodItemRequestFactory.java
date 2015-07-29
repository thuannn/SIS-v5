package com.lemania.sis.shared.perioditem;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.perioditem.PeriodItemDao;
import com.lemania.sis.server.service.DaoServiceLocator;

public interface PeriodItemRequestFactory extends RequestFactory {

	@Service(value=PeriodItemDao.class, locator=DaoServiceLocator.class)
	interface PeriodItemRequestContext extends RequestContext {
		//	
		Request<Void> initialize();
		Request<List<PeriodItemProxy>> listAll();
		Request<List<PeriodItemProxy>> listAllByClassAndStatus(String classId, boolean active);
		Request<List<PeriodItemProxy>> listAllByClass(String classId);

		Request<Void> save(PeriodItemProxy c);
		Request<PeriodItemProxy> saveAndReturn(PeriodItemProxy pp);
		
		Request<PeriodItemProxy> addPeriodItem( int fromHour, int fromMinute, int toHour, int toMinute, String note, boolean isActive );
		
		Request<Void> removePeriodItem(PeriodItemProxy classroom);
	}
	
	PeriodItemRequestContext periodItemRequestContext();

}
