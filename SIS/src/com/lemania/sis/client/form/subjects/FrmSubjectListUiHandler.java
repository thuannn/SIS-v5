package com.lemania.sis.client.form.subjects;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.SubjectProxy;
import com.sencha.gxt.data.shared.Store.Change;

public interface FrmSubjectListUiHandler extends UiHandlers {
	//
	public void updateSubject(SubjectProxy subject, String subjectName, String subjectCoef, Boolean isActive);
	//
	public void updateSubjectName2( SubjectProxy subject, String subjectName2 );
	//
	public void updateSubjectFromStore( Change<SubjectProxy, ?> change, SubjectProxy sp );
}
