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
			//
			// Get the Bulletin list by class
			Query<Bulletin> qBulletin = ofy().load().type(Bulletin.class)
					.filter("classe", assignment.getClasse())
					.filter("isActive", true);
			//
			// Get the Bulletin Subject list by professor's asssignments
			List<BulletinSubject> fullBulletinSubjectList = new ArrayList<BulletinSubject>();
			// Get the Bulletin Subject list
			Query<BulletinSubject> q = ofy().load().type(BulletinSubject.class)
					.filter("subject", assignment.getSubject())
					.filter("professor", assignment.getProf())
					.order("subjectName");
			fullBulletinSubjectList.addAll( q.list() );
			
			// If found nothing with the first professor, look by the second professor
			q = ofy().load().type(BulletinSubject.class)
					.filter("subject", assignment.getSubject())
					.filter("professor1", assignment.getProf())
					.order("subjectName");
			fullBulletinSubjectList.addAll( q.list() );
			
			// If found nothing with the first professor, look by the third professor
			q = ofy().load().type(BulletinSubject.class)
					.filter("subject", assignment.getSubject())
					.filter("professor2", assignment.getProf())
					.order("subjectName");
			fullBulletinSubjectList.addAll( q.list() );
			
			//
			Query<EvaluationSubject> currentES = null;
			List<EvaluationSubject> returnList = new ArrayList<EvaluationSubject>();
			EvaluationSubject curES = null;
			Key<EvaluationSubject> key = null;
			//
			// First, populate Evaluation Subject as usual using Bulletin Subject of the student's main class
			for ( BulletinSubject bulletinSubject : fullBulletinSubjectList ) {
				// Check if this Bulletin Subject belongs to Bulletin list of the class
				for (Bulletin bulletin : qBulletin){
					//
					if (bulletin.getIsFinished().equals(true))
						continue;
					//
					if (bulletinSubject.getBulletin().getId() == bulletin.getId()) {
						//					
						currentES = ofy().load().type(EvaluationSubject.class)
								.filter("subject", bulletinSubject.getSubject())
								.filter("classe", assignment.getClasse())
								.filter("prof", assignment.getProf())
								.filter("student", bulletin.getStudent())
								.filter("evaluationHeader",  Key.create(EvaluationHeader.class, Long.parseLong(evaluationHeaderId)));
						if ( currentES.count() > 0 )
							curES = currentES.list().get(0);
						else {
							//
							curES = new EvaluationSubject();
							//
							populateEvaluationSubjectData( curES, bulletinSubject, assignment, bulletin, evaluationHeaderId );
						}
						//
						key = ofy().save().entities( curES ).now().keySet().iterator().next();
						//
						returnList.add( ofy().load().key(key).now() );						
					}
				}				
			}			
			//
			// Second, get all the student from other classes who participate in this course
			Bulletin extraBulletin;
			q = ofy().load().type(BulletinSubject.class)
					.filter("extraClasse", assignment.getClasse())
					.filter("subject", assignment.getSubject())
					.filter("professor", assignment.getProf())
					.order("subjectName");
			for ( BulletinSubject bulletinSubject : q ) {
				//
				extraBulletin = ofy().load().key(bulletinSubject.getBulletin()).now();
				//
				// Check if this Bulletin Subject belong to a finished Bulletin, if yes skip it
				if ( extraBulletin.getIsFinished().equals(true) )
					continue;
				//				
				currentES = ofy().load().type(EvaluationSubject.class)
						.filter("subject", bulletinSubject.getSubject())
						.filter("classe", extraBulletin.getClasse() )
						.filter("prof", assignment.getProf() )
						.filter("student", extraBulletin.getStudent() )
						.filter("evaluationHeader",  Key.create(EvaluationHeader.class, Long.parseLong(evaluationHeaderId)));
				if ( currentES.count() > 0 )
					curES = currentES.list().get(0);
				else {
					//
					curES = new EvaluationSubject();
					//
					populateEvaluationSubjectData( curES, bulletinSubject, assignment, extraBulletin, evaluationHeaderId );
				}
				//
				key = ofy().save().entities( curES ).now().keySet().iterator().next();
				//
				returnList.add( ofy().load().key(key).now() );			
			}
			//
			return returnList;
		}
		return null;		
	}
	
	
	/* 
	 * */
	public void populateEvaluationSubjectData( EvaluationSubject curES, BulletinSubject bulletinSubject, Assignment assignment, 
			Bulletin bulletin, String evaluationHeaderId ) {
		//
		curES.setSubject( bulletinSubject.getSubject() );
		curES.setClasse( bulletin.getClasse() );
		curES.setProf( assignment.getProf() );
		curES.setStudent( bulletin.getStudent() );
		curES.setEvaluationHeader(  Key.create(EvaluationHeader.class, Long.parseLong(evaluationHeaderId)) );
		
		curES.setStudentName( bulletin.getStudentName() );
		curES.setSubjectName( bulletinSubject.getSubjectName() );
		curES.setProfessorName( ofy().load().key(assignment.getProf()).now().getProfName() );
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
		EvaluationHeader eh = ofy().load().key(Key.create(EvaluationHeader.class, Long.parseLong(evaluationHeaderId))).now();
		//
		Query<EvaluationSubject> q = ofy().load().type(EvaluationSubject.class)
				.filter("classe", Key.create(Classe.class, Long.parseLong(classId)))
				.filter("student", ofy().load().key( Key.create(Bulletin.class, Long.parseLong(bulletinId))).now().getStudent())
				.order("subjectName");
		//
		List<EvaluationSubject> returnList = new ArrayList<EvaluationSubject>();
		EvaluationHeader curEH;
		//
		for (EvaluationSubject evaluationSubject : q) {
			//
			// Compare the dates of the current evaluation header with the selected header
			curEH = ofy().load().key( evaluationSubject.getEvaluationHeader() ).now();
			if ( curEH.getFromDate().equals(eh.getFromDate()) && curEH.getToDate().equals(eh.getToDate()) )
				returnList.add( evaluationSubject );
		}
		return returnList;		
	}
}
