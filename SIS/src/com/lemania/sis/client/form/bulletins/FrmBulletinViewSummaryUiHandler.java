package com.lemania.sis.client.form.bulletins;

import com.gwtplatform.mvp.client.UiHandlers;

public interface FrmBulletinViewSummaryUiHandler extends UiHandlers {
	//
	void onClassChange(String classId);
	//
	void onBulletinChange( String bulletinId );
	//
	void saveBulletinRemarques( String bulletinId, String remarqueDirection );
}
