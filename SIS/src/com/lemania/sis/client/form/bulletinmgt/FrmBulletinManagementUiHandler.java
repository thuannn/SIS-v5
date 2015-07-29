package com.lemania.sis.client.form.bulletinmgt;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.bulletinbranche.BulletinBrancheProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;

public interface FrmBulletinManagementUiHandler extends UiHandlers {
	//
	void onEcoleSelected(String ecoleId);
	//
	void onProgrammeSelected(String programmeId);
	//
	void onBulletinSelected(BulletinProxy bulletin);
	//
	void onClassChange(String classId);
	//
	void removeSubject(BulletinSubjectProxy subject);
	void removeBranche( BulletinBrancheProxy branche );
	//
	void onSubjectSelected(BulletinSubjectProxy subject);
	//
	void updateBrancheCoef(BulletinBrancheProxy branche, String coef);
	void updateBranche( String bulletinBrancheId, String brancheId, String coef );
	//
	void updateSubjectCoef( BulletinSubjectProxy subject, String coef, Integer lastSubjectIndex);
	//
	void updateSubjectProf ( BulletinSubjectProxy subject, String profId, String prof1Id, String prof2Id, Integer lastSubjectIndex );
	//
	void addSubject(String bulletinId, String subjectId, String profId, String profId1, String profId2, String coef);
	void addBranche(String bulletinSubjectId, String brancheId, String coef);
	//
	void loadProfessorList( String subjectId, String classId );
}
