package com.lemania.sis.client.uihandler;

import com.gwtplatform.mvp.client.UiHandlers;

public interface StudentAddUiHandler extends UiHandlers {
	void createStudent( String firstName, String lastName, String email, Boolean active );
}
