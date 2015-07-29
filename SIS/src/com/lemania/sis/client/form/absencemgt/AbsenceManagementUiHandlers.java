/**
 * 
 */
package com.lemania.sis.client.form.absencemgt;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.absenceitem.AbsenceItemProxy;

/**
 * @author ngocthuannguyen
 *
 */
public interface AbsenceManagementUiHandlers extends UiHandlers {
	//
	void onStudentSelected( String studentId );
	//
	void updateJustifyStatus(AbsenceItemProxy ai, boolean isJustified);
	//
	void updateParentNotifiedStatus(AbsenceItemProxy ai, boolean parentNotified);
	//
	void updateAdminComment(AbsenceItemProxy ai, String adminComment);
	//
//	void updateMotif(AbsenceItemProxy ai, String motifID);
	//
	void filterDate( String studentId, String dateFrom, String dateTo);
	//
	void loadAbsentStudens( String dateFrom, String dateTo );
	//
	void showAbsenceInputPopup(String studentId, String studentName);
	//
	void removeAbsenceItem( AbsenceItemProxy aip );
	//
	void sendEmail(String absenceItemID, String studentName, String parentName, String parentEmail, String message);
	//
	void sendSMS(String absenceItemID, String number, String message);
}