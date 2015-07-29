package com.lemania.sis.client.form.subjects;

import com.gwtplatform.mvp.client.UiHandlers;

public interface FrmSubjectAddUiHandler extends UiHandlers {
	void addNewSubject(String subjectName, String defaultCoef, Boolean isActive);
}
