package com.lemania.sis.server.bean.perioditem;

import java.util.Collections;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.service.MyDAOBase;

public class PeriodItemDao extends MyDAOBase {
	
	//
	public void initialize(){
		return;
	}
	
	/*
	 * */
	public List<PeriodItem> listAll(){
		//
		Query<PeriodItem> q = ofy().load().type(PeriodItem.class);
		List<PeriodItem> returnList = q.list();
		Collections.sort(returnList);
		return returnList;
	}
	
	/*
	 * */
	public List<PeriodItem> listAllByClassAndStatus(String classId, boolean active){
		//
		Query<PeriodItem> q = ofy().load().type(PeriodItem.class)
				.filter("classe", Key.create(Classe.class, Long.parseLong(classId)))
				.filter("isActive", active);
		List<PeriodItem> returnList = q.list();
		Collections.sort(returnList);
		return returnList;
	}
	
	
	/*
	 * */
	public List<PeriodItem> listAllByClass(String classId){
		//
		Query<PeriodItem> q = ofy().load().type(PeriodItem.class)
				.filter("classe", Key.create(Classe.class, Long.parseLong(classId)));
		List<PeriodItem> returnList = q.list();
		Collections.sort(returnList);
		return returnList;
	}
	

	/*
	 * */
	public void save(PeriodItem classroom){
		ofy().save().entities( classroom );
	}
	
	
	/*
	 * */
	public PeriodItem saveAndReturn(PeriodItem classroom){
		//
		Key<PeriodItem> key = ofy().save().entities( classroom ).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public PeriodItem addPeriodItem( int fromHour, int fromMinute, int toHour, int toMinute, String note, boolean isActive ) {
		//
		PeriodItem pi = new PeriodItem();
		pi.setFromHour(fromHour);
		pi.setFromMinute(fromMinute);
		pi.setToHour(toHour);
		pi.setToMinute(toMinute);
		pi.setNote(note);
		pi.setActive(isActive);
		//
		Key<PeriodItem> key = ofy().save().entities( pi ).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/*
	 * */
	public void removePeriodItem(PeriodItem period){
		ofy().delete().entities(period);
	}

}
