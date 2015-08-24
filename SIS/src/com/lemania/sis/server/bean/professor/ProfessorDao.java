package com.lemania.sis.server.bean.professor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Profile;
import com.lemania.sis.server.bean.profilesubject.ProfileSubject;
import com.lemania.sis.server.bean.user.User;
import com.lemania.sis.server.service.MyDAOBase;

public class ProfessorDao extends MyDAOBase {
	
	/*
	 * 
	 * */
	public void initialize(){
		return;
	}
	
	/*
	 * */
	public List<Professor> listAll(){
		Query<Professor> q = ofy().load().type(Professor.class).order("profName");
		List<Professor> returnList = new ArrayList<Professor>();
		for (Professor prof : q){
			returnList.add(prof);
		}
		return returnList;
	}
	
	
	/*
	 * */
	public List<Professor> listAllActive(){
		Query<Professor> q = ofy().load().type(Professor.class).filter("profActive", true).order("profName");
		List<Professor> returnList = new ArrayList<Professor>();
		for (Professor prof : q){
			returnList.add(prof);
		}
		return returnList;
	}
	
	
	
	/*
	 * */
	public List<Professor> listAllByProfileAndSubject(String profileId, String subjectId){
		// Get the selected profile
		List<Key<Professor>> profKeys = new ArrayList<Key<Professor>>();
		Query<ProfileSubject> subjects = ofy().load().type(ProfileSubject.class).filter("profile", Key.create(Profile.class, Long.parseLong(profileId)));
		for ( ProfileSubject ps : subjects ) {
			if (ps.getSubject().getId() == Long.parseLong(subjectId) )
				profKeys.add( ps.getProfessor() );
		}
		//
		Map<Key<Professor>, Professor> q = ofy().load().keys( profKeys );
		List<Professor> returnList = new ArrayList<Professor>();
		for ( Professor prof : q.values() )
			returnList.add( prof );
		return returnList;
	}
	
	
	/**/
	public List<Professor> getByEmail(String email){
		//
		Query<Professor> q = ofy().load().type(Professor.class).filter("profEmail", email);
		List<Professor> returnList = new ArrayList<Professor>();
		for (Professor prof : q){
			returnList.add(prof);
		}
		return returnList;
	}
	
	
	
	/*
	 * 
	 * */
	public void save(Professor prof){
		ofy().save().entities(prof);
	}
	
	/*
	 * 
	 * */
	public Professor saveAndReturn(Professor prof) throws Exception{
		//
		// Check if this email address exists already
		Query<User> q = ofy().load().type(User.class)
				.filter( "email", prof.getProfEmail() );
		if ( q.count() > 0 ) {
			if ( prof.getId() == null ) {
				throw new Exception("Cette adresse email existe déjà.");
			}
		}
		//
		// If not, save this professor
		Key<Professor> key = ofy().save().entities(prof).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * 
	 * */
	public void removeProfessor(Professor prof) {
		ofy().delete().entities(prof);
	}
}
