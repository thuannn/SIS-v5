package com.lemania.sis.client.uihandler;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.EcoleProxy;

public interface EcoleListUiHandler extends UiHandlers {
	void updateEcoleStatus(EcoleProxy ecole, Boolean value);
}
