package com.lemania.sis.client.popup.periodlistpopup;

import com.gwtplatform.mvp.client.UiHandlers;

public interface PeriodListPopupUiHandlers extends UiHandlers {
	//
	void addPeriod(int fromHour, int fromMinute, int toHour, int toMinute, String note, boolean isActive);
	//
	void hidePopup();
}
