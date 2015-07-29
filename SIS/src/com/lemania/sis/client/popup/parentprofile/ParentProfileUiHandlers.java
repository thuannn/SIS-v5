package com.lemania.sis.client.popup.parentprofile;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.student.StudentProxy;

public interface ParentProfileUiHandlers extends UiHandlers {
	//
	void saveParent( String title,
		 String firstName,
		 String lastName,
		 String eMail,
		 String phoneMobile,
		 String phoneHome,
		 String phoneWork,
		 boolean acceptSMS,
		 boolean acceptEmail,
		 List<StudentProxy> children );
}
