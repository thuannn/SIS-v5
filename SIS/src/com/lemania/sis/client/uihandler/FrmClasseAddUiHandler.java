package com.lemania.sis.client.uihandler;

import com.gwtplatform.mvp.client.UiHandlers;

public interface FrmClasseAddUiHandler extends UiHandlers {
	//
	void addNewClasse( String className, String programmeId, Boolean isActif );
	
	// On school selected, load the programmes of this school
	void onEcoleSelected( String ecoleId );
}
