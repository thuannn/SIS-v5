package com.lemania.sis.client.form.periodmanagement;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.period.PeriodProxy;

public interface PeriodManagementUiHandlers extends UiHandlers {
	//
	void onClassSelected( String classId );
	//
	void addPeriod(String periodItemId, String classId, String description, int order, String note, boolean isActive);
	//
	void updatePeriod(PeriodProxy pp, String description, int order, String note, boolean isActive);
	//
	void showPeriodItemPopup();
}
