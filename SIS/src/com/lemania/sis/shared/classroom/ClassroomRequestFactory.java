package com.lemania.sis.shared.classroom;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.classroom.ClassroomDao;
import com.lemania.sis.server.service.DaoServiceLocator;

public interface ClassroomRequestFactory extends RequestFactory {
	
	@Service(value=ClassroomDao.class, locator=DaoServiceLocator.class)
	interface ClassroomRequestContext extends RequestContext {
		//	
		Request<Void> initialize();
		Request<List<ClassroomProxy>> listAll();

		Request<Void> save(ClassroomProxy c);
		Request<ClassroomProxy> saveAndReturn(ClassroomProxy c);
		
		Request<ClassroomProxy> addClassroom(String name, int capacity, String note, boolean isActive);
		
		Request<Void> removeClasse(ClassroomProxy classroom);
	}
	
	ClassroomRequestContext classroomRequestContext();
}
