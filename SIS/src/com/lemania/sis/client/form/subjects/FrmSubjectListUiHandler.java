package com.lemania.sis.client.form.subjects;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.SubjectProxy;

public interface FrmSubjectListUiHandler extends UiHandlers {
	//
	public void updateSubject(SubjectProxy subject, String subjectName, String subjectCoef, Boolean isActive);
	//
	public void updateSubjectName2( SubjectProxy subject, String subjectName2 );
}
