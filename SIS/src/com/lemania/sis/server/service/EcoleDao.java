package com.lemania.sis.server.service;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Ecole;

public class EcoleDao extends MyDAOBase{

	public void initialize(){
		return;
	}
	
	public List<Ecole> listAll(){
		Query<Ecole> q = ofy().load().type(Ecole.class).order("schoolName");
		List<Ecole> returnList = new ArrayList<Ecole>();
		for (Ecole ecole : q){
			returnList.add(ecole);
		}
		return returnList;
	}
	
	public List<Ecole> listAllActive(){
		Query<Ecole> q = ofy().load().type(Ecole.class)
				.filter("schoolActive", true)
				.order("schoolName");
		List<Ecole> returnList = new ArrayList<Ecole>();
		for (Ecole ecole : q){
			returnList.add(ecole);
		}
		return returnList;
	}
	
	public void save(Ecole ecole){
		ofy().save().entities(ecole);
	}
	
	public Ecole saveAndReturn(Ecole ecole){
		Key<Ecole> key = ofy().save().entities(ecole).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void removeEcole(Ecole ecole){
		ofy().delete().entities(ecole);
	}
}
