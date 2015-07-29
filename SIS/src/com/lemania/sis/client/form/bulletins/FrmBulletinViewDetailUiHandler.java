package com.lemania.sis.client.form.bulletins;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.bulletin.BulletinProxy;

public interface FrmBulletinViewDetailUiHandler extends UiHandlers {
	//
	void onStudentSelected();
	//
	void onClassChange(String classId);
	//
	void onBulletinChange(BulletinProxy bulletin);
}
