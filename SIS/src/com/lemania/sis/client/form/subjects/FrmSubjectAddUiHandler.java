package com.lemania.sis.client.form.subjects;

import com.gwtplatform.mvp.client.UiHandlers;

public interface FrmSubjectAddUiHandler extends UiHandlers {
	//
	void addNewSubject(String subjectName, String subjectName2, String defaultCoef, Boolean isActive);
}
