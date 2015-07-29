package com.lemania.sis.shared.profilesubject;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.profilesubject.ProfileSubjectDao;
import com.lemania.sis.server.service.DaoServiceLocator;
import com.lemania.sis.shared.ProfessorProxy;

public interface ProfileSubjectRequestFactory extends RequestFactory {
	//
	@Service(value=ProfileSubjectDao.class, locator=DaoServiceLocator.class)
	interface ProfileSubjectRequestContext extends RequestContext {
		//
		Request<List<ProfileSubjectProxy>> listAll();		
		Request<List<ProfileSubjectProxy>> listAll( String profileId );	
		//
		Request<List<ProfileSubjectProxy>> listAllActive();
		//
		Request<Void> save(ProfileSubjectProxy profile);
		Request<ProfileSubjectProxy> saveAndReturn(ProfileSubjectProxy newProfileSubject);
		Request<ProfileSubjectProxy> saveAndReturn(String profileId, String subjectId, String professorId, String professorId1, String professorId2, String subjectCoef );
		//
		Request<Boolean> removeProfileSubject(ProfileSubjectProxy profileSubject);
		//
		Request<Void> initialize();
		//
		Request<ProfileSubjectProxy> calculateTotalBrancheCoef( String profileSubjectId );
		//
		Request<ProfileSubjectProxy> updateSubjectProf( ProfileSubjectProxy profile, String profId, String profId1, String profId2 );
		//
		Request<List<ProfessorProxy>> listProfessorsByProfileSubject( String subjectId, String classId );
	}
	
	ProfileSubjectRequestContext profileSubjectRequest();
}
