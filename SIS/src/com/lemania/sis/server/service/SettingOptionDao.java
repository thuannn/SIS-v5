package com.lemania.sis.server.service;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.lemania.sis.server.SettingOption;

public class SettingOptionDao extends MyDAOBase {
	
	public void initialize(){
		return;
	}
	
	public List<SettingOption> listAll(){
		Query<SettingOption> q = ofy().load().type(SettingOption.class);
		List<SettingOption> returnList = new ArrayList<SettingOption>();
		for (SettingOption SettingOption : q){
			returnList.add(SettingOption);
		}
		return returnList;
	}
	
	public void save(String optionName, String optionValue){
		// try finding the existing option by name, if not found, save as new
		Query<SettingOption> q = ofy().load().type(SettingOption.class).filter("optionName", optionName);
		List<SettingOption> returnList = new ArrayList<SettingOption>();
		for (SettingOption so : q){
			returnList.add(so);
		}
		
		// ideally, only save the first object found
		if (returnList.size() > 0) {
			SettingOption currentSO = returnList.get(0);
			currentSO.setOptionValue(optionValue);
			ofy().save().entities(currentSO);
		}
		else {
			SettingOption newSO = new SettingOption();
			newSO.setOptionName(optionName);
			newSO.setOptionValue(optionValue);
			ofy().save().entities(newSO);
		}
	}
	
	public SettingOption saveAndReturn(SettingOption SettingOption){
		Key<SettingOption> key = ofy().save().entities(SettingOption).now().keySet().iterator().next();
		try {
			return ofy().load().key(key).now();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void removeSettingOption(SettingOption SettingOption){
		ofy().delete().entities(SettingOption);
	}
}