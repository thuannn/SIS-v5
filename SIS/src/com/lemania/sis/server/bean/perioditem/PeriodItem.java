package com.lemania.sis.server.bean.perioditem;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;

@Entity
@Index
public class PeriodItem extends DatastoreObject implements Comparable<PeriodItem> {
	
	private int fromHour;
	private int fromMinute;
	private int toHour;
	private int toMinute;
	
	private String period;
	private String note;
	private boolean isActive;

	public String getPeriod() {
		this.period = ((fromHour<10)?"0":"") + fromHour + ":" + ((fromMinute<10)?"0":"") + fromMinute 
				+ " - "
				+ ((toHour<10)?"0":"") + toHour + ":" + ((toMinute<10)?"0":"") + toMinute;
		return this.period;
	}

	public void setPeriod(String period) {
		this.period = period;
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

	public int getFromHour() {
		return fromHour;
	}

	public void setFromHour(int fromHour) {
		this.fromHour = fromHour;
	}

	public int getFromMinute() {
		return fromMinute;
	}

	public void setFromMinute(int fromMinute) {
		this.fromMinute = fromMinute;
	}

	public int getToHour() {
		return toHour;
	}

	public void setToHour(int toHour) {
		this.toHour = toHour;
	}

	public int getToMinute() {
		return toMinute;
	}

	public void setToMinute(int toMinute) {
		this.toMinute = toMinute;
	}

	@Override
	public int compareTo(PeriodItem o) {
		//
		if (this.getFromHour() < o.getFromHour())
			return -1;
		if (this.getFromHour() == o.getFromHour()) {
			if (this.getFromMinute() < o.getFromMinute())
				return -1;
			else
				return 1;
		}
		if (this.getFromHour() > o.getFromHour())
			return 1;
		return 0;
	}

}
