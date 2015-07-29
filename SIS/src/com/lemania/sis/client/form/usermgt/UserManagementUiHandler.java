package com.lemania.sis.client.form.usermgt;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.user.UserProxy;

public interface UserManagementUiHandler extends UiHandlers {
	//
	public void addNewUser(String fullName, String userName, String password, String email);
	//
	public void updateUserStatus(UserProxy user, Boolean active, Boolean admin, Boolean isProf, Boolean isStudent, Boolean isParent, String password);
	//
	public void loadUsersByType(String type);	
}
