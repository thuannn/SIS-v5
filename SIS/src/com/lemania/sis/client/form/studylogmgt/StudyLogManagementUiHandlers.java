package com.lemania.sis.client.form.studylogmgt;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.studylog.StudyLogProxy;

interface StudyLogManagementUiHandlers extends UiHandlers {
	
	//
	void onProfessorSelected(String profsId);
	//
	void onLstAssignmentsChange( String profId, String subjectId );
	//
	void onLstClassChange( String profId, String subjectId, String classId, String dateFrom, String dateTo );
	//
	void onStudyLogAdd( String profId, String subjectId, String classId, 
			String title, String content, String editLogId, String logFileName, String logEntryDate );
	//
	void deleteLog( StudyLogProxy log );
}