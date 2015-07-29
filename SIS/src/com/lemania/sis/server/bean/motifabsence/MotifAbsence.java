package com.lemania.sis.server.bean.motifabsence;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.lemania.sis.server.DatastoreObject;

@Entity
@Index
public class MotifAbsence extends DatastoreObject implements Comparable<MotifAbsence> {
	
	private String motifCode;
	private String motifLabel;
	private boolean isReceivable = false;
	private boolean isOutside = false;
	private boolean isHealth = false;
	private boolean isDispensable = false;
	private String textLetter = "";
	private String textSMS = "";

	public String getMotifCode() {
		return motifCode;
	}

	public void setMotifCode(String motifCode) {
		this.motifCode = motifCode;
	}

	public String getMotifLabel() {
		return motifLabel;
	}

	public void setMotifLabel(String motifLabel) {
		this.motifLabel = motifLabel;
	}

	public boolean isReceivable() {
		return isReceivable;
	}

	public void setReceivable(boolean isReceivable) {
		this.isReceivable = isReceivable;
	}

	public boolean isOutside() {
		return isOutside;
	}

	public void setOutside(boolean isOutside) {
		this.isOutside = isOutside;
	}

	public boolean isHealth() {
		return isHealth;
	}

	public void setHealth(boolean isHealth) {
		this.isHealth = isHealth;
	}

	public boolean isDispensable() {
		return isDispensable;
	}

	public void setDispensable(boolean isDispensable) {
		this.isDispensable = isDispensable;
	}

	public String getTextLetter() {
		return textLetter;
	}

	public void setTextLetter(String textLetter) {
		this.textLetter = textLetter;
	}

	public String getTextSMS() {
		return textSMS;
	}

	public void setTextSMS(String textSMS) {
		this.textSMS = textSMS;
	}

	@Override
	public int compareTo(MotifAbsence o) {
		//
		return this.motifCode.compareTo( o.getMotifCode() );
	}
	
}
