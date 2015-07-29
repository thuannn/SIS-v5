package com.lemania.sis.client.uihandler;

import com.gwtplatform.mvp.client.UiHandlers;

public interface EcoleAddUiHandler extends UiHandlers {
	void ecoleAdd(String nom, String adresse, Boolean active);
	void ecoleAddCancel();
}
