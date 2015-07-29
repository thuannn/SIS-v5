package com.lemania.sis.server.bean.evaluations;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.bean.bulletin.Bulletin;
import com.lemania.sis.server.service.MyDAOBase;

public class EvaluationStudentReportDao extends MyDAOBase {
	/*
	 * */
	public List<EvaluationStudentReport> listAll(){
		//
		Query<EvaluationStudentReport> q = ofy().load().type(EvaluationStudentReport.class);
		List<EvaluationStudentReport> returnList = new ArrayList<EvaluationStudentReport>();
		for (EvaluationStudentReport evaluationReport : q){
			returnList.add( evaluationReport );
		}
		return returnList;
	}
	
	/*
	 * */
	public void save(EvaluationStudentReport evaluationReport){
		ofy().save().entities( evaluationReport );
	}
	
	/*
	 * */
	public void save( String bulletinId, String evaluationHeaderId, String commentaire ) {
		//
		Query<EvaluationStudentReport> q = ofy().load().type(EvaluationStudentReport.class)
				.filter("student", ofy().load().key( Key.create(Bulletin.class, Long.parseLong(bulletinId))).now().getStudent())
				.filter("evaluationHeader",  Key.create(EvaluationHeader.class, Long.parseLong(evaluationHeaderId)));
		EvaluationStudentReport report;
		if (q.count()>0) {
			report = q.list().get(0);
			report.setEvaluationNote(commentaire);
		} else {
			report = new EvaluationStudentReport();
			report.setEvaluationHeader( Key.create(EvaluationHeader.class, Long.parseLong(evaluationHeaderId)) );
			report.setStudent( ofy().load().key( Key.create(Bulletin.class, Long.parseLong(bulletinId))).now().getStudent() );
			report.setEvaluationNote(commentaire);
		}
		ofy().save().entities(report);
	}
	
	/*
	 * */
	public EvaluationStudentReport load( String bulletinId, String evaluationHeaderId ){
		//
		Query<EvaluationStudentReport> q = ofy().load().type(EvaluationStudentReport.class)
				.filter("student", ofy().load().key( Key.create(Bulletin.class, Long.parseLong(bulletinId))).now().getStudent())
				.filter("evaluationHeader",  Key.create(EvaluationHeader.class, Long.parseLong(evaluationHeaderId)));		
		if (q.count()>0)
			return q.list().get(0);
		else
			return null;
	}
	
	/*
	 * */
	public EvaluationStudentReport saveAndReturn(EvaluationStudentReport evaluationReport){
		Key<EvaluationStudentReport> key = ofy().save().entities( evaluationReport ).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
