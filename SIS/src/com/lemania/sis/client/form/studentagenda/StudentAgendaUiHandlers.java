package com.lemania.sis.client.form.studentagenda;

import com.gwtplatform.mvp.client.UiHandlers;

public interface StudentAgendaUiHandlers extends UiHandlers {
	//
	void onClassChanged( String classId );
	//
	void onProfileChanged( String classId, String profileId );
	//
	void onStudentChange( String classId, String profileId, String bulletinId );
}
