package com.lemania.sis.server.bean.classroom;

import java.util.List;
import java.util.Collections;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.service.MyDAOBase;

public class ClassroomDao extends MyDAOBase {
	//
	public void initialize(){
		return;
	}
	
	/*
	 * */
	public List<Classroom> listAll(){
		//
		Query<Classroom> q = ofy().load().type(Classroom.class);
		List<Classroom> returnList = q.list();
		Collections.sort(returnList);
		return returnList;
	}

	/*
	 * */
	public void save(Classroom classroom){
		ofy().save().entities( classroom );
	}
	
	/*
	 * */
	public Classroom saveAndReturn(Classroom classroom){
		Key<Classroom> key = ofy().save().entities( classroom ).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * */
	public Classroom addClassroom(String name, int capacity, String note, boolean isActive) {
		//
		Classroom classroom = new Classroom(name, capacity, note, isActive);
		Key<Classroom> key = ofy().save().entities( classroom ).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * */
	public void removeClasse(Classroom classroom){
		ofy().delete().entities(classroom);
	}
	
}
