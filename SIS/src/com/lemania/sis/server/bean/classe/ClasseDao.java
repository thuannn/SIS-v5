package com.lemania.sis.server.bean.classe;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Cours;
import com.lemania.sis.server.service.MyDAOBase;

public class ClasseDao extends MyDAOBase {
	//
	public void initialize(){
		return;
	}
	
	public List<Classe> listAll(){
		Query<Classe> q = ofy().load().type(Classe.class).order("className");
		List<Classe> returnList = new ArrayList<Classe>();
		for (Classe classe : q){
			classe.setProgrammeName( ofy().load().key(classe.getProgramme()).now().getCoursNom());
			returnList.add(classe);
		}
		return returnList;
	}
	
	public List<Classe> listAll(String coursId){
		List<Classe> returnList = new ArrayList<Classe>();
		
		if (coursId.isEmpty())
			return returnList;
		
		Key<Cours> cours = Key.create(Cours.class, Long.parseLong(coursId));
		Query<Classe> q = ofy().load().type(Classe.class)
				.filter("programme", cours)
				.order("className");
		
		for (Classe classe : q){
			classe.setProgrammeName( ofy().load().key(classe.getProgramme()).now().getCoursNom());
			returnList.add( classe );
		}
		
		return returnList;
	}
	
	
	public List<Classe> listAllActive(String coursId){
		//
		List<Classe> returnList = new ArrayList<Classe>();
		
		if (coursId.isEmpty())
			return returnList;
		
		Key<Cours> cours = Key.create(Cours.class, Long.parseLong( coursId ));
		Query<Classe> q = ofy().load().type(Classe.class)
				.filter("programme", cours)
				.order("className");
		
		for (Classe classe : q){
			if (classe.getIsActive().equals(true)) {
				classe.setProgrammeName( ofy().load().key(classe.getProgramme()).now().getCoursNom());
				returnList.add( classe );
			}
		}
		
		return returnList;
	}
	
	/*
	 * */
	public List<Classe> listAllActive(){
		//
		List<Classe> returnList = new ArrayList<Classe>();
		Query<Classe> q = ofy().load().type(Classe.class)				
				.order("className");
		
		for (Classe classe : q){
			if (classe.getIsActive().equals(true)) {
				classe.setProgrammeName( ofy().load().key(classe.getProgramme()).now().getCoursNom());
				returnList.add( classe );
			}
		}
		
		return returnList;
	}
	
	public void save(Classe classe){
		ofy().save().entities( classe );
	}
	
	
	public void save(Classe classe, String coursId){
		Key<Cours> coursKey = Key.create(Cours.class, Long.parseLong(coursId));
		classe.setProgramme(coursKey);
		ofy().save().entities( classe );
	}
	
	
	public Classe saveAndReturn(Classe classe){
		Key<Classe> key = ofy().save().entities(classe).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void removeClasse(Classe classe){
		ofy().delete().entities(classe);
	}
}
