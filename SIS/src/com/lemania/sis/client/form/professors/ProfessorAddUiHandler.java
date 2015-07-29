package com.lemania.sis.client.form.professors;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ProfessorAddUiHandler extends UiHandlers {
	
	void professorAddCancelled();
	void professorAdd(String profName, String profEmail, Boolean profStatus);
}
