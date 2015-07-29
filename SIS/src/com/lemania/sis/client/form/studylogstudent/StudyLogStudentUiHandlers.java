package com.lemania.sis.client.form.studylogstudent;

import com.gwtplatform.mvp.client.UiHandlers;

interface StudyLogStudentUiHandlers extends UiHandlers {
	//
	void onClassChange(String classId);
	//
	void onBulletinChange( String classId, String dateFrom, String dateTo );
	//
	void onSubjectChanged( String bulletinId, String subjectId, String dateFrom, String dateTo );
}