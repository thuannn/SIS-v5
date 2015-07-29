package com.lemania.sis.client.uihandler;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ContactUiHandler extends UiHandlers {
	void sendMessage(String lastName, String firstName, String email, String message);
}
