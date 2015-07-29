/**
 * 
 */
package com.lemania.sis.client.form.mainpage;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * @author Thuan
 *
 */
public interface MainPageUiHandler extends UiHandlers {
	void showHomepage();
	void showTimeInput();
	
	void showFrmPassword();
	
	void showFrmStudents();
	void showFrmCreateStudents();
	
	void showEcoleList();
	void showEcoleAdd();

	void showCoursList();
	void showCoursAdd();
	
	void showProfessorList();
	void showProfessorAdd();
	
	void showTypeList();
	void showTypeAdd();
	
	void showSettingsScreen();
	void showContact();
	
	void showBrancheList();
	void showBrancheAdd();
	
	void showFrmSubjectList();
	void showFrmSubjectAdd();
	
	void showClasseList();
	void showClasseAdd();
	
	void showExtractDataForm();
	
	void showUserManagement();
	
	void showProfileManagement();
	
	void showCreateBulletins();
	void showFrmBulletinManagement();
	
	void showFrmBulletinViewDetail();
	
	void showRptByDept();
	void showRptBySchool();
	void showRptByMonth();
	
	void showFrmMarkInput();
	
	void logOut();
}
