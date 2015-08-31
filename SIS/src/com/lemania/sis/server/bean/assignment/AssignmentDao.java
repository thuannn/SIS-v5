package com.lemania.sis.server.bean.assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.classe.ClassNameComparator;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.bean.professor.Professor;
import com.lemania.sis.server.service.MyDAOBase;

public class AssignmentDao extends MyDAOBase {
	//
	public void initialize(){
		return;
	}
	
	
	public List<Assignment> listAll(){
		Query<Assignment> q = ofy().load().type(Assignment.class).order("classe");
		List<Assignment> returnList = new ArrayList<Assignment>();
		for (Assignment a : q){
			if (a.getClass() != null) {
				a.setProgrammeName( ofy().load().key(ofy().load().key(a.getClasse()).now().getProgramme()).now().getCoursNom());
				a.setClasseName(ofy().load().key(a.getClasse()).now().getClassName());
				a.setClassId( ofy().load().key(a.getClasse()).now().getId().toString() );
				a.setSubjectId( ofy().load().key( a.getSubject() ).now().getId().toString() );
			}
			if (a.getSubject() != null)
				a.setSubjectName(ofy().load().key(a.getSubject()).now().getSubjectName());
			returnList.add(a);
		}
		return returnList;
	}
	
	
	/**/
	public List<Assignment> listAll(String profId){
		Query<Assignment> q = ofy().load().type(Assignment.class)
				.filter("prof", Key.create(Professor.class, Long.parseLong(profId)))
				.order("subject")
				.order("classe");
		List<Assignment> returnList = new ArrayList<Assignment>();
		for (Assignment a : q){
			if (a.getClass() != null) {
				a.setProgrammeName( ofy().load().key(ofy().load().key(a.getClasse()).now().getProgramme()).now().getCoursNom());
				a.setClasseName(ofy().load().key(a.getClasse()).now().getClassName());
				a.setClassId( ofy().load().key(a.getClasse()).now().getId().toString() );
				a.setSubjectId( ofy().load().key( a.getSubject() ).now().getId().toString() );
			}
			if (a.getSubject() != null)
				a.setSubjectName(ofy().load().key(a.getSubject()).now().getSubjectName());
			returnList.add(a);
		}
		return returnList;
	}
	
	
	/**/
	public List<Professor> listAllProfessorBySubject(String subjectId){
		Query<Assignment> q = ofy().load().type(Assignment.class)
				.filter("subject", Key.create(Subject.class, Long.parseLong(subjectId)))
				.order("prof");
		List<Professor> returnList = new ArrayList<Professor>();
		Professor prof = new Professor();
		for (Assignment a : q){
			if (a.getActive()) {
				prof = ofy().load().key(a.getProf()).now();
				if (prof.getProfActive())
					returnList.add(prof);
			}
		}
		return returnList;
	}
	
	
	/*
	 * */
	public List<Professor> listAllProfessorBySubject(String subjectId, String classId) {
		Query<Assignment> q = ofy().load().type(Assignment.class)
				.filter("classe", Key.create(Classe.class, Long.parseLong(classId)))
				.filter("subject", Key.create(Subject.class, Long.parseLong(subjectId)))
				.order("prof");
		List<Professor> returnList = new ArrayList<Professor>();
		Professor prof = new Professor();
		for (Assignment a : q){
			if (a.getActive()) {
				prof = ofy().load().key(a.getProf()).now();
				if (prof.getProfActive())
					returnList.add(prof);
			}
		}
		return returnList;
	}
	
	
	/*
	 * List all the subjects that a professor takes care of
	 * */
	public List<Subject> listAllSubjectByProfessor(String profId) {
		//
		Query<Assignment> q = ofy().load().type(Assignment.class)
				.filter("prof", Key.create( Professor.class, Long.parseLong(profId) ))
				.order("subject");
		List<Subject> returnList = new ArrayList<Subject>();
		// Goes through the list of assignments and populate the subject list
		Subject sub = new Subject();
		Key<Subject> subKey = null;
		for (Assignment a : q) {
			if (a.getActive()) {
				 if ( (subKey == null) || (subKey.getId() != a.getSubject().getId()) ) {
					sub = ofy().load().key( a.getSubject() ).now();
					returnList.add( sub );
				 }
				subKey = a.getSubject();
			}
		}
		return returnList;
	}
	
	
	/*
	 * List all the subjects that a professor takes care of
	 * */
	public List<Classe> listAllClassByProfAndSubject(String profId, String subjectId) {
		//
		Query<Assignment> q = ofy().load().type(Assignment.class)
				.filter("prof", Key.create( Professor.class, Long.parseLong(profId) ))
				.filter("subject", Key.create(Subject.class, Long.parseLong(subjectId)))
				.order("classe");
		List<Classe> returnList = new ArrayList<Classe>();
		// Goes through the list of assignments and populate the subject list
		Classe cl = new Classe();
		for (Assignment a : q){
			if (a.getActive()) {				
				cl = ofy().load().key( a.getClasse() ).now();
				returnList.add( cl );
			}
		}
		//
		Collections.sort( returnList, new ClassNameComparator() );
		return returnList;
	}
	
	
	/**/
	public List<Assignment> listAllActive(){
		Query<Assignment> q = ofy().load().type(Assignment.class)
				.filter("schoolActive", true)
				.order("classe");
		List<Assignment> returnList = new ArrayList<Assignment>();
		for (Assignment a : q){
			returnList.add(a);
		}
		return returnList;
	}
	
	
	
	/**/
	public List<Assignment> listAllActive(String profId){
		Query<Assignment> q = ofy().load().type(Assignment.class)
				.filter("active", true)
				.filter("prof", Key.create(Professor.class, Long.parseLong(profId)))
				.order("classe");
		List<Assignment> returnList = new ArrayList<Assignment>();
		for (Assignment a : q){
			if (a.getClass() != null) {
				a.setProgrammeName( ofy().load().key(ofy().load().key(a.getClasse()).now().getProgramme()).now().getCoursNom());
				a.setClasseName(ofy().load().key(a.getClasse()).now().getClassName());
				a.setClassId( ofy().load().key(a.getClasse()).now().getId().toString() );
				a.setSubjectId( ofy().load().key( a.getSubject() ).now().getId().toString() );
			}
			if (a.getSubject() != null)
				a.setSubjectName(ofy().load().key(a.getSubject()).now().getSubjectName());
			returnList.add(a);
		}
		Collections.sort(returnList);
		return returnList;
	}
	
	
	/**/
	public void save(Assignment a){
		ofy().save().entities(a);
	}
	
	
	public Assignment saveAndReturn(Assignment a){
		Key<Assignment> key = ofy().save().entities(a).now().keySet().iterator().next();
		try {
			Assignment savedA = ofy().load().key(key).now();
			if (savedA.getClass() != null) {
				savedA.setProgrammeName( ofy().load().key(ofy().load().key(savedA.getClasse()).now().getProgramme()).now().getCoursNom());
				savedA.setClasseName(ofy().load().key(savedA.getClasse()).now().getClassName());
				savedA.setClassId( ofy().load().key(a.getClasse()).now().getId().toString() );
				savedA.setSubjectId( ofy().load().key( a.getSubject() ).now().getId().toString() );
			}
			if (savedA.getSubject() != null)
				savedA.setSubjectName(ofy().load().key(savedA.getSubject()).now().getSubjectName());
			return savedA; 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * Save a newly added assigment
	 * */
	public Assignment saveAndReturn(String profId, String classeId, String subjectId, Boolean isActive){
		// if the assignment is already existed, do not add more
		Query<Assignment> q = ofy().load().type(Assignment.class)
				.filter("prof", Key.create(Professor.class, Long.parseLong(profId)))
				.filter("classe", Key.create(Classe.class, Long.parseLong(classeId)))
				.filter("subject", Key.create(Subject.class, Long.parseLong(subjectId)));
		if (q.list().size()>0) {
			return null;
		}
		
		Assignment a = new Assignment();
		a.setClasse(Key.create(Classe.class, Long.parseLong(classeId)));
		a.setProf(Key.create(Professor.class, Long.parseLong(profId)));
		a.setSubject(Key.create(Subject.class, Long.parseLong(subjectId)));
		a.setActive(isActive);
		Key<Assignment> key = ofy().save().entities(a).now().keySet().iterator().next();		
		try {
			Assignment savedA = ofy().load().key(key).now();
			if (savedA.getClass() != null) {
				savedA.setProgrammeName( ofy().load().key(ofy().load().key(savedA.getClasse()).now().getProgramme()).now().getCoursNom());
				savedA.setClasseName(ofy().load().key(savedA.getClasse()).now().getClassName());
				savedA.setClassId( ofy().load().key(a.getClasse()).now().getId().toString() );
				savedA.setSubjectId( ofy().load().key( a.getSubject() ).now().getId().toString() );
			}
			if (savedA.getSubject() != null)
				savedA.setSubjectName(ofy().load().key(savedA.getSubject()).now().getSubjectName());
			return savedA; 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * Update active status
	 * */
	public Assignment updateAssignmentStatus(Long userId, Assignment assignment, Boolean status) {
		//
		assignment.setActive(status);
		Key<Assignment> key = ofy().save().entities(assignment).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * 
	 * */
	public void removeAssignment(Assignment a){
		ofy().delete().entities(a);
	}
}
