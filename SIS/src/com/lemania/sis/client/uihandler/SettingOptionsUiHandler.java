package com.lemania.sis.client.uihandler;

import com.gwtplatform.mvp.client.UiHandlers;

public interface SettingOptionsUiHandler extends UiHandlers{
	//
	void updateSettingOptionDeadline(String dayNumber);
	//
	void updateSettingOptionManualBlock(Boolean blnBlock);
	//
	void updateCurrentEcole( String ecoleCode );
	//
	public void fixStudentName();	
	//
	public void updateDeadlinesMatu(String deadlineT1, String deadlineT2);
	public void updateDeadlinesES(String deadlineT1, String deadlineT2, String deadlineT3);
	public void updateDeadlinesBAC(String deadlineT1, String deadlineT2, String deadlineT3);
}
