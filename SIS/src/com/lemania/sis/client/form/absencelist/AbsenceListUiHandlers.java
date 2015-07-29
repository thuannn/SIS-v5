package com.lemania.sis.client.form.absencelist;

import com.gwtplatform.mvp.client.UiHandlers;

interface AbsenceListUiHandlers extends UiHandlers {
	//
	void loadAbsenceList( String studentId );
	//
	void onClassChange( String classId );
}