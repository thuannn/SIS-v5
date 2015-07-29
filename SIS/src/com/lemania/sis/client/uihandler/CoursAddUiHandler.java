package com.lemania.sis.client.uihandler;

import com.gwtplatform.mvp.client.UiHandlers;

public interface CoursAddUiHandler extends UiHandlers {
	void coursAdd(String coursNom, String ecoleNom, Boolean cousActif);
	void coursAddCancel();
}
