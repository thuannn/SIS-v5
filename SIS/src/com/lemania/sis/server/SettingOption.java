package com.lemania.sis.server;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class SettingOption extends DatastoreObject {
	//
	private String optionName;
	private String optionValue;
	
	public String getOptionName() {
		return optionName;
	}
	
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	
	public String getOptionValue() {
		return optionValue;
	}
	
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
}
