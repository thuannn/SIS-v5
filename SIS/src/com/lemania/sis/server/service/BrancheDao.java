package com.lemania.sis.server.service;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.Branche;

public class BrancheDao extends MyDAOBase {
	
	public void initialize(){
		return;
	}
	
	public List<Branche> listAll(){
		Query<Branche> q = ofy().load().type(Branche.class).order("brancheName");
		List<Branche> returnList = new ArrayList<Branche>();
		for (Branche branche : q){
			returnList.add(branche);
		}
		return returnList;
	}
	
	public void save(Branche branche){
		ofy().save().entities(branche);
	}
	
	public Branche saveAndReturn(Branche branche){
		Key<Branche> key = ofy().save().entities(branche).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void removeBranche(Branche branche){
		ofy().delete().entities(branche);
	}

}
