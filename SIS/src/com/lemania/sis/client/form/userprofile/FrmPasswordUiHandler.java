package com.lemania.sis.client.form.userprofile;

import com.gwtplatform.mvp.client.UiHandlers;

public interface FrmPasswordUiHandler extends UiHandlers {
	//
	void changePassword(String current, String new1, String new2);
	//
	void changeUserName( String currentUserName, String newUserName );
}
