package com.lemania.sis.client.form.bulletincreation;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.bulletin.BulletinProxy;

public interface FrmBulletinCreationUiHandler extends UiHandlers {
	//
	void onEcoleSelected(String ecoleId);
	//
	void onProgrammeSelected(String coursId);
	//
	void onClassChanged(String classId);
	//
	void onYearChanged(String year);
	//
	void createBulletin(String studentId, String classId, String year, String profileId);
	//
	void removeBulletin( BulletinProxy bp );
	//
	void updateBulletinFinishedStatus( BulletinProxy bp, Boolean isFinished );
}
