package com.lemania.sis.server.bean.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.bean.bulletin.Bulletin;
import com.lemania.sis.server.bean.user.User;
import com.lemania.sis.server.service.MyDAOBase;

public class StudentDao extends MyDAOBase {
	
	public void initialize(){
		return;
	}
	
	public void save(Student student){
		ofy().save().entities(student);
	}
	
	
	/*
	 * */
	public Student saveAndReturn(Student student){
		Key<Student> key = ofy().save().entities(student).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public Student saveAndReturn( String firstName, String lastName, String email, Boolean active ) throws Exception{
		//
		Student student = new Student();
		student.setFirstName(firstName);
		student.setLastName(lastName);
		student.setEmail(email);
		student.setIsActive(active);
		//
		// Check if email address already exists
		Query<User> q = ofy().load().type(User.class)
				.filter("email", email);
		if ( q.count() > 0 ) {
			throw new Exception("Cette adresse email existe déjà.");
		}
		// If not, save the new user
		Key<Student> key = ofy().save().entities(student).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**/
	public void removeStudent( Student student ){
		ofy().delete().entities(student).now();
	}
	
	
	/* List all the student both active and inactive. */
	public List<Student> listAll(){
		Query<Student> q = ofy().load().type(Student.class)
				.order("LastName")
				.order("FirstName");
		List<Student> returnList = new ArrayList<Student>();
		for ( Student student : q.list() ){
			returnList.add(student);
		}
		Collections.sort( returnList );
		return returnList;
	}
	
	
	/* List all the active students */
	public List<Student> listAllActive() {
		Query<Student> q = ofy().load().type(Student.class)
				.filter("isActive", true)
				.order("LastName")
				.order("FirstName");
		List<Student> returnList = new ArrayList<Student>();
		for (Student student : q){
			returnList.add(student);
		}
		Collections.sort( returnList );
		return returnList;
	}
	
	
	/* List all the active students */
	public List<Student> listAllInactive() {
		Query<Student> q = ofy().load().type(Student.class)
				.filter("isActive", false)
				.order("LastName")
				.order("FirstName");
		List<Student> returnList = new ArrayList<Student>();
		for (Student student : q){
			returnList.add(student);
		}
		Collections.sort( returnList );
		return returnList;
	}
		
	
	/* List all the active students */
	public List<Student> listAllActiveByClassAndProfile(){
		Query<Student> q = ofy().load().type(Student.class)
				.filter("isActive", true)
				.order("LastName")
				.order("FirstName");
		List<Student> returnList = new ArrayList<Student>();
		for (Student student : q){
			returnList.add(student);
		}
		Collections.sort( returnList );
		return returnList;
	}
	
	
	
	/* List all the active students */
	public List<Student> listByEmail(String email){
		Query<Student> q = ofy().load().type(Student.class).filter("Email", email)
				.order("LastName")
				.order("FirstName");
		List<Student> returnList = new ArrayList<Student>();
		for (Student student : q){
			returnList.add(student);
		}
		Collections.sort( returnList );
		return returnList;
	}
	
	
	/*
	 * 
	 * */
	public List<Student> listAllActiveWithoutBulletin(){
		// Get the list of student IDs in the list of bulletins that are not finished
		Query<Bulletin> q = ofy().load().type(Bulletin.class)
				.order("student");				
		List<Long> studentIds = new ArrayList<Long>();
		Long prevId = Long.MIN_VALUE;
		Long curId = Long.MIN_VALUE;
		for (Bulletin bulletin : q){
			//
			if (bulletin.getIsFinished().equals(true))
				continue;
			//
			curId = ofy().load().key(bulletin.getStudent()).now().getId();
			if ( prevId != curId ) {
				prevId = curId;
				studentIds.add( prevId );
			}
		}
		//
		Query<Student> qStudent = ofy().load().type(Student.class).filter("isActive", true)
				.order("LastName")
				.order("FirstName");
		List<Student> returnList = new ArrayList<Student>();
		Boolean notThisStudent = false;
		for (Student student : qStudent){
			notThisStudent = false;
			for (Long id : studentIds){
				if (student.getId().equals(id)){
					notThisStudent = true;
					break;
				}
			}
			if (notThisStudent)
				continue;
			else
				returnList.add(student);
		}
		Collections.sort( returnList );
		return returnList;
	}
}
