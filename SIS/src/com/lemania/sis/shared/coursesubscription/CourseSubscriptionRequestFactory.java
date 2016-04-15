package com.lemania.sis.shared.coursesubscription;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.coursesubscription.CourseSubscription;
import com.lemania.sis.server.bean.coursesubscription.CourseSubscriptionDao;
import com.lemania.sis.server.service.DaoServiceLocator;

public interface CourseSubscriptionRequestFactory extends RequestFactory {

	@Service(value=CourseSubscriptionDao.class, locator=DaoServiceLocator.class)
	interface CourseSubscriptionRequestContext extends RequestContext {
		//
		Request<List<CourseSubscriptionProxy>> listAll();
		Request<List<CourseSubscriptionProxy>> listAllByDate( String date );
		Request<List<CourseSubscriptionProxy>> listAllByStudent( CourseSubscriptionProxy subscription );
		//
		Request<CourseSubscriptionProxy> saveAndReturn( String studentID, String profID, String date,
				boolean R, boolean ES, String note, String courseID );
		Request<CourseSubscriptionProxy> saveAndReturn( CourseSubscriptionProxy subscription );
		Request<CourseSubscriptionProxy> saveAndReturn( CourseSubscriptionProxy subscription, String profId );
		Request<CourseSubscriptionProxy> saveAndReturn( CourseSubscriptionProxy subscription, String note1, String subjectId, 
				boolean isR, boolean isES ); 
		//
		Request<Void> removeCourseSubscription( CourseSubscriptionProxy subscription );
	}
	
	CourseSubscriptionRequestContext courseSubscriptionRequestContext();
}
