package com.lemania.sis.shared.coursesubscription;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.coursesubscription.CourseSubscriptionDao;
import com.lemania.sis.server.service.DaoServiceLocator;

public interface CourseSubscriptionRequestFactory extends RequestFactory {

	@Service(value=CourseSubscriptionDao.class, locator=DaoServiceLocator.class)
	interface CourseSubscriptionRequestContext extends RequestContext {
		//
		Request<List<CourseSubscriptionProxy>> listAll();
		Request<List<CourseSubscriptionProxy>> listAllByDate( String date );
		//
		Request<CourseSubscriptionProxy> saveAndReturn( String studentID, String profID, String date );
		//
		Request<Void> removeCourseSubscription( CourseSubscriptionProxy subscription );
	}
	
	CourseSubscriptionRequestContext courseSubscriptionRequestContext();
}
