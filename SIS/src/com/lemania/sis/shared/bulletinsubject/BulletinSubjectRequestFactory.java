package com.lemania.sis.shared.bulletinsubject;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.bulletinsubject.BulletinSubjectDao;
import com.lemania.sis.server.service.DaoServiceLocator;

public interface BulletinSubjectRequestFactory extends RequestFactory {
	//
	@Service(value=BulletinSubjectDao.class, locator=DaoServiceLocator.class)
	interface BulletinSubjectRequestContext extends RequestContext {
		//
		Request<List<BulletinSubjectProxy>> listAll();		
		Request<List<BulletinSubjectProxy>> listAll( String bulletinId );	
		Request<List<BulletinSubjectProxy>> listAllPositiveCoef( String bulletinId );	
		Request<List<BulletinSubjectProxy>> listAllForPublic( String bulletinId );	
		//
		Request<List<BulletinSubjectProxy>> listAllActive();		
		//
		Request<List<BulletinSubjectProxy>> listAllByAssignment( String assignmentId );
		Request<List<BulletinSubjectProxy>> listAllByStudent( String studentId );
		//
		Request<Void> save(BulletinSubjectProxy profile);
		Request<BulletinSubjectProxy> saveAndReturn(BulletinSubjectProxy newBulletinSubject);
		Request<BulletinSubjectProxy> saveAndReturn(String bulletinId, String subjectId, String professorId, String professorId1, String professorId2, String subjectCoef );
		//
		Request<BulletinSubjectProxy> updateBulletinSubjectProf( BulletinSubjectProxy bs, String profId, String prof1Id, String prof2Id );
		//
		Request<Boolean> removeProfileSubject(BulletinSubjectProxy bulletinSubject);
		//
		Request<Void> initialize();
		//
		Request<BulletinSubjectProxy> calculateTotalBrancheCoef( String bulletinSubjectId );
		//
	}
	
	BulletinSubjectRequestContext bulletinSubjectRequest();
}
