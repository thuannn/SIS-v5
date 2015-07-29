package com.lemania.sis.server.service;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Cours;
import com.lemania.sis.server.Ecole;

public class CoursDao extends MyDAOBase {
	
	public void initialize(){
		return;
	}
	
	public List<Cours> listAll(){
		Query<Cours> q = ofy().load().type(Cours.class).order("coursNom");
		List<Cours> returnList = new ArrayList<Cours>();
		for (Cours ecole : q){
			returnList.add(ecole);
		}
		return returnList;
	}
	
	public List<Cours> listAll(String ecoleId){
		List<Cours> returnList = new ArrayList<Cours>();
		
		if (ecoleId.isEmpty())
			return returnList;
		
		Key<Ecole> ecole = Key.create(Ecole.class, Long.parseLong(ecoleId));
		Query<Cours> q = ofy().load().type(Cours.class)
				.filter("ecole", ecole)
				.order("coursNom");
		
		for (Cours cours : q){
			returnList.add(cours);
		}
		
		return returnList;
	}
	
	public List<Cours> listAllActive(String ecoleId){
		List<Cours> returnList = new ArrayList<Cours>();
		
		if (ecoleId.isEmpty())
			return returnList;
		
		Key<Ecole> ecole = Key.create(Ecole.class, Long.parseLong(ecoleId));
		Query<Cours> q = ofy().load().type(Cours.class)
				.filter("ecole", ecole)
				.order("coursNom");
		
		for (Cours cours : q){
			if (cours.getCoursActif().equals(true))
				returnList.add(cours);
		}
		
		return returnList;
	}
	
	public void save(Cours cours){
		ofy().save().entities(cours);
	}
	
	public void save(Cours cours, String ecoleId){
		Key<Ecole> ecoleKey = Key.create(Ecole.class, Long.parseLong(ecoleId));
		cours.setEcole(ecoleKey);
		ofy().save().entities(cours);
	}
	
	public Cours saveAndReturn(Cours cours){
		Key<Cours> key = ofy().save().entities(cours).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void removeCours(Cours cours){
		ofy().delete().entities(cours);
	}	
}