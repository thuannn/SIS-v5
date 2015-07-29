package com.lemania.sis.client.form.parentmgt;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.parent.ParentProxy;

public interface ParentManagementUiHandlers extends UiHandlers {
	//
	void showParentProfilePopup();
	
	void editParent(ParentProxy parent);
}
