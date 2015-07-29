package com.lemania.sis.server.bean.evaluations;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.bean.assignment.Assignment;
import com.lemania.sis.server.bean.bulletin.Bulletin;
import com.lemania.sis.server.bean.bulletinsubject.BulletinSubject;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.service.MyDAOBase;

public class EvaluationSubjectDao extends MyDAOBase {
	
	/*
	 * */
	public List<EvaluationSubject> listAll(){
		//
		Query<EvaluationSubject> q = ofy().load().type(EvaluationSubject.class);
		List<EvaluationSubject> returnList = new ArrayList<EvaluationSubject>();
		for (EvaluationSubject evaluationSubject : q){
			returnList.add( evaluationSubject );
		}
		return returnList;
	}
	
	
	/*
	 * */
	public void save(EvaluationSubject evaluationSubject){
		//
		ofy().save().entities( evaluationSubject );
	}
	
	
	/*
	 * */
	public EvaluationSubject saveAndReturn(EvaluationSubject evaluationSubject){
		Key<EvaluationSubject> key = ofy().save().entities(evaluationSubject).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public List<EvaluationSubject> populateEvaluationSubjects(String profId, String assignmentId, String evaluationHeaderId) {
		// Get the assignment object
		Assignment assignment = ofy().load().key( Key.create(Assignment.class, Long.parseLong(assignmentId))).now();
		//
		if (assignment != null) {
			// Get the Bulletin list by class
			Query<Bulletin> qBulletin = ofy().load().type(Bulletin.class)
					.filter("classe", assignment.getClasse())
					.filter("isActive", true);
			
			// Get the Bulletin Subject list
			Query<BulletinSubject> q = ofy().load().type(BulletinSubject.class)
					.filter("subject", assignment.getSubject())
					.filter("professor", assignment.getProf())
					.order("subjectName");
			//
			Query<EvaluationSubject> currentES = null;
			List<EvaluationSubject> returnList = new ArrayList<EvaluationSubject>();
			EvaluationSubject curES = null;
			Key<EvaluationSubject> key = null;
			//
			for ( BulletinSubject bulletinSubject : q ){
				// Check if this Bulletin Subject belongs to Bulletin list of the class
				for (Bulletin bulletin : qBulletin){
					//
					if (bulletin.getIsFinished().equals(true))
						continue;
					//
					if (bulletinSubject.getBulletin().getId() == bulletin.getId()) {
												
						currentES = ofy().load().type(EvaluationSubject.class)
								.filter("subject", bulletinSubject.getSubject())
								.filter("classe", assignment.getClasse())
								.filter("prof", assignment.getProf())
								.filter("student", bulletin.getStudent())
								.filter("evaluationHeader",  Key.create(EvaluationHeader.class, Long.parseLong(evaluationHeaderId)));
						if ( currentES.count() > 0 )
							curES = currentES.list().get(0);
						else {
							curES = new EvaluationSubject();
							curES.setSubject( bulletinSubject.getSubject() );
							curES.setClasse( assignment.getClasse() );
							curES.setProf( assignment.getProf() );
							curES.setStudent( bulletin.getStudent() );
							curES.setEvaluationHeader(  Key.create(EvaluationHeader.class, Long.parseLong(evaluationHeaderId)) );
							
							curES.setStudentName( bulletin.getStudentName() );
							curES.setSubjectName( bulletinSubject.getSubjectName() );
							curES.setProfessorName( ofy().load().key(assignment.getProf()).now().getProfName() );
						}
						//
						key = ofy().save().entities( curES ).now().keySet().iterator().next();
						returnList.add( ofy().load().key(key).now() );						
					}
				}				
			}			
			//
			return returnList;
		}
		return null;		
	}
	
	
	/*
	 * */
	public void removeEvaluationSubject(EvaluationSubject evaluationSubject){
		ofy().delete().entities(evaluationSubject);
	}	
	

	/*
	 * */
	public List<EvaluationSubject> listAllByStudent(String classId, String bulletinId, String evaluationHeaderId) {
		//
		Query<EvaluationSubject> q = ofy().load().type(EvaluationSubject.class)
				.filter("classe", Key.create(Classe.class, Long.parseLong(classId)))
				.filter("student", ofy().load().key( Key.create(Bulletin.class, Long.parseLong(bulletinId))).now().getStudent())
				.filter("evaluationHeader",  Key.create(EvaluationHeader.class, Long.parseLong(evaluationHeaderId)))
				.order("subjectName");
		List<EvaluationSubject> returnList = new ArrayList<EvaluationSubject>();
		for (EvaluationSubject evaluationSubject : q){
			returnList.add( evaluationSubject );
		}
		return returnList;		
	}
}
