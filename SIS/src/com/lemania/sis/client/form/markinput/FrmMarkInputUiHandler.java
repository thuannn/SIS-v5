package com.lemania.sis.client.form.markinput;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.bulletinbranche.BulletinBrancheProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;

public interface FrmMarkInputUiHandler extends UiHandlers {
	//
	void onProfessorSelected(String profId);
	//
	void onAssignmentSelected(String assignmentId);
	//
	void onBulletinSubjectSelected(BulletinSubjectProxy bulletinSubject);
	//
	void onBulletinBrancheSelected(BulletinBrancheProxy bulletinBranche);
	//
	void saveNotes(BulletinBrancheProxy bulletinBranche, BulletinSubjectProxy bulletinSubject,
			String t_1_1, String t_1_2, String t_1_3, String t_1_4, String t_1_5, 
			String t_2_1, String t_2_2, String t_2_3, String t_2_4, String t_2_5, 
			String t_3_1, String t_3_2, String t_3_3, String t_3_4, String t_3_5,
			String t_4_1, String t_4_2, String t_4_3, String t_4_4, String t_4_5,
			String remarque1, String remarque2, String remarque3, String remarque4 );
}
