package com.lemania.sis.server.service;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Profile;
import com.lemania.sis.server.bean.classe.Classe;

public class ProfileDao extends MyDAOBase {
	//
	public void initialize(){
		return;
	}
	
	public List<Profile> listAll(){
		Query<Profile> q = ofy().load().type(Profile.class).order("profileName");
		List<Profile> returnList = new ArrayList<Profile>();
		for (Profile profile : q){
			returnList.add(profile);
		}
		return returnList;
	}
	
	public List<Profile> listAllActive(){
		Query<Profile> q = ofy().load().type(Profile.class)
				.filter("isActive", true)
				.order("profileName");
		List<Profile> returnList = new ArrayList<Profile>();
		for (Profile profile : q){
			returnList.add(profile);
		}
		return returnList;
	}
	
	
	/**/
	public List<Profile> listAllActiveByClass(String classId){
		Query<Profile> q = ofy().load().type(Profile.class)
				.filter("classe", Key.create(Classe.class, Long.parseLong(classId)))
				.filter("isActive", true)
				.order("profileName");
		List<Profile> returnList = new ArrayList<Profile>();
		for (Profile profile : q){
			returnList.add(profile);
		}
		return returnList;
	}
	
	
	/**/
	public void save(Profile profile){
		ofy().save().entities(profile);
	}
	
	
	/**/
	public Profile saveAndReturn(Profile profile){
		Key<Profile> key = ofy().save().entities(profile).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**/
	public Profile saveAndReturn(String profileName, String classId){
		Profile profile = new Profile();
		profile.setProfileName( profileName );
		profile.setClasse( Key.create(Classe.class, Long.parseLong(classId)));
		
		Key<Profile> key = ofy().save().entities(profile).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**/
	public void removeProfile(Profile profile){
		ofy().delete().entities(profile);
	}

}
