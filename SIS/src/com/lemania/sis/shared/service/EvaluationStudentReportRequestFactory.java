package com.lemania.sis.shared.service;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.lemania.sis.server.bean.evaluations.EvaluationStudentReportDao;
import com.lemania.sis.server.service.DaoServiceLocator;
import com.lemania.sis.shared.EvaluationStudentReportProxy;

public interface EvaluationStudentReportRequestFactory extends RequestFactory {
	//
	@Service(value=EvaluationStudentReportDao.class, locator=DaoServiceLocator.class)
	interface EvaluationStudentReportRequestContext extends RequestContext {
		//
		Request<List<EvaluationStudentReportProxy>> listAll();		
		//
		Request<Void> save( EvaluationStudentReportProxy evaluationHeader );		
		Request<Void> save( String bulletinId, String evaluationHeaderId, String commentaire );
		//
		Request<EvaluationStudentReportProxy> load( String bulletinId, String evaluationHeaderId );
		//
		Request<EvaluationStudentReportProxy> saveAndReturn( EvaluationStudentReportProxy newEvaluationReport );			
	}
	
	EvaluationStudentReportRequestContext evaluationStudentReportRequest();
}
