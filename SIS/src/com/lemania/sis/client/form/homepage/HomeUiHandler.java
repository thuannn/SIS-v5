package com.lemania.sis.client.form.homepage;

import com.gwtplatform.mvp.client.UiHandlers;

public interface HomeUiHandler extends UiHandlers {
	//
	void authenticateUser(String userName, String password);
	//
	void loginWithGoogle();
}
