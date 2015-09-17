package com.lemania.sis.server.bean.absenceitem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.assignment.Assignment;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.bean.motifabsence.MotifAbsence;
import com.lemania.sis.server.bean.period.Period;
import com.lemania.sis.server.bean.professor.Professor;
import com.lemania.sis.server.bean.student.Student;
import com.lemania.sis.server.service.MyDAOBase;

public class AbsenceItemDao extends MyDAOBase {

	public void initialize(){
		return;
	}
	
	/*
	 * */
	public List<AbsenceItem> listAll(){
		//
		Query<AbsenceItem> q = ofy().load().type(AbsenceItem.class);
		List<AbsenceItem> returnList = q.list();
		for ( AbsenceItem ai : q ) {
			populateIgnoreSaveValues( ai );
		}
		return returnList;
	}
	
	
	/*
	 * */
	public List<AbsenceItem> listAllByAssignment( String assignmentId, String strAbsenceDate ){
		//
		Assignment asg = ofy().load().key( Key.create(Assignment.class, Long.parseLong(assignmentId)) ).now();
		//
		Query<AbsenceItem> q = ofy().load().type(AbsenceItem.class)
				.filter("strAbsenceDate", strAbsenceDate)
				.filter("keyProf", asg.getProf())
				.filter("keySubject", asg.getSubject())
				.filter("keyClasse", asg.getClasse());
  		List<AbsenceItem> returnList = q.list();
		for ( AbsenceItem ai : q ) {
			populateIgnoreSaveValues( ai );
		}
		return returnList;
	}
	
	
	/*
	 * */
	public List<AbsenceItem> listAllByStudent( String studentId ){
		//
		Query<AbsenceItem> q = ofy().load().type(AbsenceItem.class)
				.filter("keyStudent", Key.create(Student.class, Long.parseLong(studentId)))
				.order("strAbsenceDate");
  		List<AbsenceItem> returnList = q.list();
		for ( AbsenceItem ai : q ) {
			//
			populateIgnoreSaveValues( ai );
		}
		Collections.sort( returnList );
		return returnList;
	}
	
	
	/*
	 * */
	public List<AbsenceItem> listAllByStudentAndDate( String studentId, String dateFrom, String dateTo ){
		//
		Query<AbsenceItem> q = ofy().load().type(AbsenceItem.class)
				.filter("keyStudent", Key.create(Student.class, Long.parseLong(studentId)))
				.filter("strAbsenceDate >=", dateFrom)
				.filter("strAbsenceDate <=", dateTo)
				.order("strAbsenceDate");
  		List<AbsenceItem> returnList = q.list();
		for ( AbsenceItem ai : q ) {
			//
			populateIgnoreSaveValues( ai );
		}
		Collections.sort( returnList );
		return returnList;
	}
	

	/*
	 * */
	public void save(AbsenceItem ai){
		ofy().save().entities( ai );
	}
	
	
	
	/*
	 * */
	public AbsenceItem updateAbsenceLateItem(String aiID, int minutes) {
		AbsenceItem returnAI;
		AbsenceItem ai = ofy().load().key( Key.create(AbsenceItem.class, Long.parseLong(aiID)) ).now();
		ai.setLateMinutes(minutes);
		Key<AbsenceItem> key = ofy().save().entities( ai ).now().keySet().iterator().next();
		try {
			returnAI = ofy().load().key(key).now();
			populateIgnoreSaveValues( returnAI );
			
			return returnAI;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public AbsenceItem updateRemarque( String aiID, String strRemarque ) {
		AbsenceItem returnAI;
		AbsenceItem ai = ofy().load().key( Key.create(AbsenceItem.class, Long.parseLong(aiID)) ).now();
		ai.setProfComment( strRemarque );
		Key<AbsenceItem> key = ofy().save().entities( ai ).now().keySet().iterator().next();
		try {
			returnAI = ofy().load().key(key).now();
			populateIgnoreSaveValues( returnAI );
			
			return returnAI;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	/*
	 * */
	public AbsenceItem saveAbsenceItem(
			String strDate,
			String studentId,
			String periodId,
			String profId,
			String classId,
			String subjectId, 
			String motifId,
			String codeAbsence,
			String profComment,
			int lateMinute,
			boolean justified,
			boolean parentNotified ) {
		//
		AbsenceItem returnAI = new AbsenceItem();
		returnAI.setStrAbsenceDate(strDate);
		returnAI.setKeyStudent( Key.create(Student.class, Long.parseLong(studentId)));
		returnAI.setKeyPeriod( Key.create(Period.class, Long.parseLong(periodId)));
		returnAI.setKeyProf( Key.create(Professor.class, Long.parseLong(profId)));
		returnAI.setKeyClasse( Key.create(Classe.class, Long.parseLong(classId)));
		returnAI.setKeySubject( Key.create(Subject.class, Long.parseLong(subjectId)));
		if ( !motifId.equals("") )
			returnAI.setKeyMotif( Key.create(MotifAbsence.class, Long.parseLong(motifId)));
		returnAI.setCodeAbsenceType(codeAbsence);
		returnAI.setProfComment(profComment);
		returnAI.setLateMinutes(lateMinute);
		returnAI.setJusttified(justified);
		returnAI.setParentNotified(parentNotified);
		//
		Key<AbsenceItem> key = ofy().save().entities( returnAI ).now().keySet().iterator().next();
		try {
			returnAI = ofy().load().key(key).now();
			populateIgnoreSaveValues( returnAI );
			return returnAI;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public AbsenceItem saveAndReturn(AbsenceItem ai){
		//
		AbsenceItem returnAI;
		Key<AbsenceItem> key = ofy().save().entities( ai ).now().keySet().iterator().next();
		try {
			returnAI = ofy().load().key(key).now();
			populateIgnoreSaveValues( returnAI );
			
			return returnAI;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public AbsenceItem updateMotif(AbsenceItem ai, String motifID) {
		//
		AbsenceItem returnAI;
		ai.setKeyMotif( Key.create( MotifAbsence.class, Long.parseLong(motifID)) );
		Key<AbsenceItem> key = ofy().save().entities( ai ).now().keySet().iterator().next();
		try {
			returnAI = ofy().load().key(key).now();
			populateIgnoreSaveValues( returnAI );
			
			return returnAI;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public void removeAbsenceItem(AbsenceItem ai){
		ofy().delete().entities(ai).now();
	}
	
	
	/*
	 * */
	public void removeAbsenceItem(String aiID){
		ofy().delete().entities( ofy().load().key( Key.create(AbsenceItem.class, Long.parseLong(aiID))).now() );
	}
	
	
	/*
	 * */
	private void populateIgnoreSaveValues(AbsenceItem ai) {
		//
		Student student = ofy().load().key( ai.getKeyStudent() ).now();
		ai.setStudentId( student.getId().toString() );
		ai.setStudentName( student.getLastName() + " " + student.getFirstName() );
		//
		Period p = ofy().load().key( ai.getKeyPeriod() ).now();
		ai.setPeriodId( p.getId().toString() );
		ai.setPeriodDesc( p.getDescription() );
		//
		if ( ai.getKeyMotif() != null)
			ai.setMotifId( Long.toString( ai.getKeyMotif().getId() ));
		else
			ai.setMotifId("");
		//
		Subject subject = ofy().load().key( ai.getKeySubject() ).now();
		ai.setSubjectName( subject.getSubjectName() );
		//
		Professor prof = ofy().load().key( ai.getKeyProf() ).now();
		ai.setProfName( prof.getProfName() );
		//
		Classe classe = ofy().load().key( ai.getKeyClasse() ).now();
		ai.setClassName( classe.getClassName() );
	}
	
	
	/*
	 * Load all students that have absences in the date range
	 * Sort student list by last name
	 * */
	public List<AbsenceItem> loadAbsentStudents( String dateFrom, String dateTo ) {
		//
		Query<AbsenceItem> q = ofy().load().type(AbsenceItem.class)
				.filter("strAbsenceDate >=", dateFrom)
				.filter("strAbsenceDate <=", dateTo);
		List<AbsenceItem> sourceList = q.list();
   		List<AbsenceItem> returnList = new ArrayList<AbsenceItem>();
  		//
  		Collections.sort( sourceList, new Comparator<AbsenceItem>() {
  			//
			@Override
			public int compare(AbsenceItem o1, AbsenceItem o2) {
				//
				Student student1 = ofy().load().key( o1.getKeyStudent() ).now();
				Student student2 = ofy().load().key( o2.getKeyStudent() ).now(); 
				return (student1.getLastName() + student1.getFirstName())
						.compareTo( 
								(student2.getLastName() + student2.getFirstName()) );
			}
		});
  		//
  		AbsenceItem prevItem = null;
		for ( AbsenceItem ai : sourceList ) {
			//
			populateIgnoreSaveValues( ai );
			//
			if ( (prevItem == null) || !prevItem.getStudentId().equals( ai.getStudentId() ) ) {
				//
				returnList.add( ai );
				prevItem = ai;
			}
		}
		return returnList;
	}
	
	
	/*
	 * */
	public AbsenceItem saveNotificationDateEmail( String absenceItemID ) {
		//
		AbsenceItem returnAI;
		String date = "";
		Date nn = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Key<AbsenceItem> key = null;
		// Load the item
		AbsenceItem ai = ofy().load().key( Key.create(AbsenceItem.class, Long.parseLong(absenceItemID))).now();
		if (ai != null) {
			date = sdf.format(nn)
					+ "|" 
					+ ((ai.getNotificationDateEmail() == null) ? "":ai.getNotificationDateEmail());
			ai.setNotificationDateEmail( date );
			key = ofy().save().entities( ai ).now().keySet().iterator().next();
		}
		//
		try {
			returnAI = ofy().load().key(key).now();
			populateIgnoreSaveValues( returnAI );
			return returnAI;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public AbsenceItem saveNotificationDateSMS( String absenceItemID ) {
		//
		AbsenceItem returnAI;
		String date = "";
		Date nn = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Key<AbsenceItem> key = null;
		// Load the item
		AbsenceItem ai = ofy().load().key( Key.create(AbsenceItem.class, Long.parseLong(absenceItemID))).now();
		if (ai != null) {
			date = sdf.format(nn)
					+ "|" 
					+ ((ai.getNotificationDateSMS() == null) ? "":ai.getNotificationDateSMS());
			ai.setNotificationDateSMS( date );
			key = ofy().save().entities( ai ).now().keySet().iterator().next();
		}
		//
		try {
			returnAI = ofy().load().key(key).now();
			populateIgnoreSaveValues( returnAI );
			return returnAI;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
