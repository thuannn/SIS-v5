package com.lemania.sis.server.bean.motifabsence;

import java.util.Collections;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.service.MyDAOBase;

public class MotifAbsenceDao extends MyDAOBase {
	
	public void initialize(){
		return;
	}
	
	/*
	 * */
	public List<MotifAbsence> listAll(){
		//
		Query<MotifAbsence> q = ofy().load().type(MotifAbsence.class);
		List<MotifAbsence> returnList = q.list();
		Collections.sort(returnList);
		return returnList;
	}

	/*
	 * */
	public void save(MotifAbsence motif){
		ofy().save().entities( motif );
	}
	
	/*
	 * */
	public MotifAbsence saveAndReturn(MotifAbsence motif){
		//
		MotifAbsence returnMotif;
		Key<MotifAbsence> key = ofy().save().entities( motif ).now().keySet().iterator().next();
		try {
			returnMotif = ofy().load().key(key).now();
			return returnMotif;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public void removeParent(MotifAbsence motif){
		ofy().delete().entities(motif);
	}
	
}
