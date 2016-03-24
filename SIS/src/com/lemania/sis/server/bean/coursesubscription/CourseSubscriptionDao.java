package com.lemania.sis.server.bean.coursesubscription;

import java.util.Collections;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
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
		Professor prof;
		for ( CourseSubscription subscription : subscriptions ) {
			student = ofy().load().key( subscription.getStudent() ).now();
			prof = ofy().load().key( subscription.getProf() ).now();
			subscription.setStudentName( student.getLastName() + " " + student.getFirstName() );
			subscription.setProfessorName( prof.getProfName() );
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
	public CourseSubscription saveAndReturn( String studentID, String profID, String date ) {
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
		subscription.setDate( date );
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
