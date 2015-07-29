package com.lemania.sis.server.bean.period;

import java.util.Collections;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.bean.perioditem.PeriodItem;
import com.lemania.sis.server.service.MyDAOBase;

public class PeriodDao extends MyDAOBase {
	//
	public void initialize(){
		return;
	}
	
	/*
	 * */
	public List<Period> listAll(){
		//
		Query<Period> q = ofy().load().type(Period.class);
		List<Period> returnList = q.list();
		for (Period p :  returnList)
			p.setPeriodText( ofy().load().key( p.getPeriodItem() ).now().getPeriod() );
		Collections.sort(returnList);
		return returnList;
	}
	
	/*
	 * */
	public List<Period> listAllByClassAndStatus(String classId, boolean active){
		//
		Query<Period> q = ofy().load().type(Period.class)
				.filter("classe", Key.create(Classe.class, Long.parseLong(classId)))
				.filter("isActive", active);
		List<Period> returnList = q.list();
		for (Period p :  returnList)
			p.setPeriodText( ofy().load().key( p.getPeriodItem() ).now().getPeriod() );
		Collections.sort(returnList);
		return returnList;
	}
	
	
	/*
	 * */
	public List<Period> listAllByClass(String classId){
		//
		Query<Period> q = ofy().load().type(Period.class)
				.filter("classe", Key.create(Classe.class, Long.parseLong(classId)));
		List<Period> returnList = q.list();
		for (Period p :  returnList)
			p.setPeriodText( ofy().load().key( p.getPeriodItem() ).now().getPeriod() );
		Collections.sort(returnList);
		return returnList;
	}
	

	/*
	 * */
	public void save(Period classroom){
		ofy().save().entities( classroom );
	}
	
	/*
	 * */
	public Period saveAndReturn(Period classroom){
		Key<Period> key = ofy().save().entities( classroom ).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * */
	public Period addPeriod( String periodItemId, String classId, String description, int order, String note, boolean isActive ) {
		//
		Period period = new Period();
		period.setPeriodItem( Key.create(PeriodItem.class, Long.parseLong(periodItemId)) );
		period.setClasse( Key.create(Classe.class, Long.parseLong(classId)));
		period.setDescription(description);
		period.setOrder(order);
		period.setNote(note);
		period.setActive(isActive);
		//
		Key<Period> key = ofy().save().entities( period ).now().keySet().iterator().next();
		try {
			Period returnPeriod = ofy().load().key(key).now();
			returnPeriod.setPeriodText( ofy().load().key( returnPeriod.getPeriodItem() ).now().getPeriod() );
			return returnPeriod;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * */
	public void removePeriod(Period period){
		ofy().delete().entities(period);
	}
}
