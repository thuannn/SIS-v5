package com.lemania.sis.shared.service;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.evaluations.EvaluationSubjectDao;
import com.lemania.sis.server.service.DaoServiceLocator;
import com.lemania.sis.shared.EvaluationSubjectProxy;

public interface EvaluationSubjectRequestFactory extends RequestFactory {	
	//
	@Service(value=EvaluationSubjectDao.class, locator=DaoServiceLocator.class)
	interface EvaluationSubjectRequestContext extends RequestContext {
		//
		Request<List<EvaluationSubjectProxy>> listAll();
		Request<List<EvaluationSubjectProxy>> listAllByStudent(String classId, String bulletinId, String evaluationHeaderId);
		//
		Request<Void> save( EvaluationSubjectProxy evaluationSubject );
		Request<EvaluationSubjectProxy> saveAndReturn( EvaluationSubjectProxy evaluationSubject );	
		//
		Request<List<EvaluationSubjectProxy>> populateEvaluationSubjects(String profId, String assignmentId, String evaluationHeaderId);
		//
		Request<Void> removeEvaluationSubject( EvaluationSubjectProxy ecole );
	}
	
	EvaluationSubjectRequestContext evaluationSubjectRequest();
}
