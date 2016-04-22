package com.lemania.sis.server.bean.coursesubscription;

import java.util.Collections;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Cours;
import com.lemania.sis.server.Subject;
import com.lemania.sis.server.bean.professor.Professor;
import com.lemania.sis.server.bean.student.Student;
import com.lemania.sis.server.service.MyDAOBase;


public class CourseSubscriptionDao extends MyDAOBase {
	//
	public void initialize(){
		return;
	}
	
	/*
	 * */
	public List<CourseSubscription> listAll(){
		//
		Query<CourseSubscription> q = ofy().load().type(CourseSubscription.class);
		List<CourseSubscription> returnList = q.list();
		//
		populateUnsavedData( returnList );
		//
		Collections.sort( returnList );
		//
		return returnList;
	}
	
	/*
	 * */
	public List<CourseSubscription> listAllByDate( String date ){
		//
		Query<CourseSubscription> q = ofy().load().type(CourseSubscription.class)
				.filter("date", date);
		List<CourseSubscription> returnList = q.list();
		//
		populateUnsavedData( returnList );
		//
		Collections.sort( returnList );
		//
		return returnList;
	}
	
	/*
	 * */
	public List<CourseSubscription> listAllByStudent( CourseSubscription subscription ){
		//
		Query<CourseSubscription> q = ofy().load().type(CourseSubscription.class)
				.filter("student", subscription.getStudent() )
				.order("-date");
		List<CourseSubscription> returnList = q.list();
		//
		populateUnsavedData( returnList );
		//
		return returnList;
	}
	
	
	/*
	 * 
	 * */
	public void populateUnsavedData( List<CourseSubscription> subscriptions ) {
		//
		Student student;
		Professor prof, prof1;
		for ( CourseSubscription subscription : subscriptions ) {
			//
			student = ofy().load().key( subscription.getStudent() ).now();
			prof = ofy().load().key( subscription.getProf() ).now();
			
			if ( subscription.getProf1() != null ) {
				prof1 = ofy().load().key( subscription.getProf1() ).now();
				subscription.setProfessor1Name( prof1.getProfName() );
			}
			else 
				subscription.setProfessor1Name( "" );	
			
			subscription.setStudentName( student.getLastName() + " " + student.getFirstName() );
			subscription.setProfessorName( prof.getProfName() );
			
			if ( subscription.getSubject() != null )
				subscription.setSubjectName( ofy().load().key( subscription.getSubject() ).now().getSubjectName() );
			else
				subscription.setSubjectName("");
			
		}
	}
	

	/*
	 * */
	public void save(CourseSubscription subscription) {
		//
		ofy().save().entities( subscription );
	}
	
	
	/*
	 * */
	public CourseSubscription saveAndReturn(CourseSubscription subscription) {
		//
		Key<CourseSubscription> key = ofy().save().entities( subscription ).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public CourseSubscription saveAndReturn(CourseSubscription subscription, String profId) {
		//
		subscription.setProf1( Key.create( Professor.class, Long.parseLong(profId)) );
		//
		Key<CourseSubscription> key = ofy().save().entities( subscription ).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public CourseSubscription saveAndReturn(CourseSubscription subscription, String note1, String subjectId, 
			boolean isR, boolean isES ) {
		//
		subscription.setSubject( Key.create( Subject.class, Long.parseLong( subjectId )) );
		subscription.setNote1( note1 );
		subscription.setR(isR);
		subscription.setES(isES);
		//
		Key<CourseSubscription> key = ofy().save().entities( subscription ).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	/*
	 * */
	public CourseSubscription saveAndReturn( String studentID, String profID, String date,
			boolean R, boolean ES, String note1, String subjectID ) {
		//
		// Check if this student is already in the list for this date
		Query<CourseSubscription> q = ofy().load().type(CourseSubscription.class)
				.filter("date", date)
				.filter( "student", Key.create( Student.class, Long.parseLong(studentID) ));
		if (q.count()>0)
			return null;
		//
		CourseSubscription subscription = new CourseSubscription();
		subscription.setStudent( Key.create( Student.class, Long.parseLong(studentID)) );
		subscription.setProf( Key.create(Professor.class, Long.parseLong(profID)) );
		//
		if (!subjectID.equals(""))
			subscription.setSubject( Key.create(Subject.class, Long.parseLong(subjectID)) );
		else
			subscription.setSubject(null);
		//
		subscription.setDate( date );
		subscription.setNote1(note1);
		subscription.setR(R);
		subscription.setES(ES);
		//
		Key<CourseSubscription> key = ofy().save().entities( subscription ).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public void removeCourseSubscription(CourseSubscription subscription) {
		//
		ofy().delete().entities( subscription ).now();
	}
}
