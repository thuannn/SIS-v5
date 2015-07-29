package com.lemania.sis.shared.bulletin;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.bulletin.BulletinDao;
import com.lemania.sis.server.service.DaoServiceLocator;
import com.lemania.sis.shared.student.StudentProxy;

public interface BulletinRequestFactory extends RequestFactory {
	//
	@Service(value=BulletinDao.class, locator=DaoServiceLocator.class)
	interface BulletinRequestContext extends RequestContext {
		//
		Request<List<BulletinProxy>> listAll();			
		Request<List<BulletinProxy>> listAllActive();
		Request<List<BulletinProxy>> listAllByClass(String classId);
		Request<List<BulletinProxy>> listAllByParentUserId(String parentId);
		Request<List<BulletinProxy>> listAllActiveByClass(String classId);
		Request<List<BulletinProxy>> listAllActiveByClassProfile( String classId, String profileId );
		Request<List<BulletinProxy>> listAllByEmail(String email);
		Request<List<BulletinProxy>> listAllByEmailForPublic(String email);
		//
		Request<List<BulletinProxy>> listAllStudentByProfSubjectClass( String profId, String subjectId, String classId );
		//
		Request<BulletinProxy> getBulletin(String bulletinId);
		//
		Request<Void> save(BulletinProxy bulletin);
		Request<BulletinProxy> saveAndReturn(BulletinProxy newBulletin);
		//
		Request<Boolean> removeBulletin(BulletinProxy bulletin);
		//
		Request<BulletinProxy> createBulletin(String studentId, String classId, String year, String profileId);
		//
		Request<Void> initialize();
		//
		Request<Void> updateBulletinStatus( String studentId, Boolean status );
		//
		Request<BulletinProxy> saveBulletinRemarqueDirection( String bulletinId, String remarqueDirection );
	}
	
	BulletinRequestContext bulletinRequest();
}
