package com.lemania.sis.server.bean.evaluations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.bean.assignment.Assignment;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.bean.professor.Professor;
import com.lemania.sis.server.service.MyDAOBase;

public class EvaluationHeaderDao extends MyDAOBase {
	/*
	 * */
	public List<EvaluationHeader> listAll(){
		//
		Query<EvaluationHeader> q = ofy().load().type(EvaluationHeader.class);
		List<EvaluationHeader> returnList = new ArrayList<EvaluationHeader>();
		for (EvaluationHeader evaluationHeader : q){
			evaluationHeader.setClassMasterName( ofy().load().key(evaluationHeader.getClassMaster()).now().getProfName() );
			returnList.add( evaluationHeader );
		}
		Collections.sort( returnList );
		return returnList;
	}
	
	
	/*
	 * */
	public List<EvaluationHeader> listAllByClass(String classId){
		//
		Query<EvaluationHeader> q = ofy().load().type(EvaluationHeader.class)
				.filter("classe", Key.create(Classe.class, Long.parseLong(classId)));
		List<EvaluationHeader> returnList = new ArrayList<EvaluationHeader>();
		for (EvaluationHeader evaluationHeader : q){
			evaluationHeader.setClassMasterName( ofy().load().key(evaluationHeader.getClassMaster()).now().getProfName() );
			returnList.add( evaluationHeader );
		}
		Collections.sort( returnList );
		return returnList;
	}
	
	
	/*
	 * */
	public List<EvaluationHeader> listAllByAssignment(String assignmentId){
		// Look for the Class key in the assignment
		Assignment assingment = ofy().load().key( Key.create(Assignment.class, Long.parseLong(assignmentId))).now();
		//
		List<EvaluationHeader> returnList = new ArrayList<EvaluationHeader>();
		if (assingment != null) {
			Query<EvaluationHeader> q = ofy().load().type(EvaluationHeader.class)
					.filter("classe", assingment.getClasse());		
			for (EvaluationHeader evaluationHeader : q){
				evaluationHeader.setClassMasterName( ofy().load().key(evaluationHeader.getClassMaster()).now().getProfName() );
				returnList.add( evaluationHeader );
			}
		}
		Collections.sort( returnList );
		return returnList;
	}
	
	
	/*
	 * */
	public void save(EvaluationHeader evaluationHeader){
		ofy().save().entities(evaluationHeader);
	}
	
	
	/*
	 * */
	public EvaluationHeader saveAndReturn(EvaluationHeader evaluationHeader){
		Key<EvaluationHeader> key = ofy().save().entities(evaluationHeader).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public EvaluationHeader updateEvaluationHeader(EvaluationHeader ehUpdate, String dateFrom, String dateTo, String classMasterId, String objective){
		//
		ehUpdate.setFromDate(dateFrom);
		ehUpdate.setToDate(dateTo);
		ehUpdate.setObjective(objective);
		ehUpdate.setClassMaster( Key.create(Professor.class, Long.parseLong(classMasterId)));
		ofy().save().entities(ehUpdate);
		//
		return ehUpdate;
	}
	
	
	/*
	 * */
	public EvaluationHeader saveAndReturn(String fromDate, String toDate, String objective, String schoolYear, String classMasterId, String classId) {
		EvaluationHeader evaluationHeader = new EvaluationHeader();
		evaluationHeader.setFromDate(fromDate);
		evaluationHeader.setToDate(toDate);
		evaluationHeader.setObjective(objective);
		evaluationHeader.setSchoolYear(schoolYear);
		evaluationHeader.setClasse(Key.create(Classe.class, Long.parseLong(classId)));
		evaluationHeader.setClassMaster( Key.create(Professor.class, Long.parseLong(classMasterId)));
		
		Key<EvaluationHeader> key = ofy().save().entities(evaluationHeader).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public void removeEvaluationHeader(EvaluationHeader evaluationHeader){
		ofy().delete().entities(evaluationHeader);
	}	
}
