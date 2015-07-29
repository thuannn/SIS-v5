package com.lemania.sis.server.bean.period;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;
import com.lemania.sis.server.bean.classe.Classe;
import com.lemania.sis.server.bean.perioditem.PeriodItem;

@Entity
@Index
public class Period extends DatastoreObject implements Comparable<Period> {
	//
	private Key<PeriodItem> periodItem;
	private Key<Classe> classe;
	private String description = "";
	private int order;
	private String note = "";
	private boolean isActive = true;
	
	@IgnoreSave
	private String periodText;
	
	
	public String getPeriodText() {
		return periodText;
	}

	public void setPeriodText(String periodText) {
		this.periodText = periodText;
	}

	public Key<PeriodItem> getPeriodItem() {
		return periodItem;
	}

	public void setPeriodItem(Key<PeriodItem> periodItem) {
		this.periodItem = periodItem;
	}

	public Key<Classe> getClasse() {
		return classe;
	}
	
	public void setClasse(Key<Classe> classe) {
		this.classe = classe;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int compareTo(Period o) {
		//
		return (this.order > o.getOrder()) ? 1: -1;
	}
}
