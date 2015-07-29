package com.lemania.sis.client.popup.absenceinput;

import com.gwtplatform.mvp.client.UiHandlers;

public interface AbsenceInputUiHandlers extends UiHandlers {
	//
	void saveAbsenceItem(
			String strAbsenceDate,
			String studentId,
			String periodId,
			String profId,
			String classId,
			String subjectId, 
			String motifId,
			String codeAbsence,
			String profComment,
			int lateMinute,
			boolean justified,
			boolean parentNotified );
	//
	void removeAbsenceItem( String aiID );
	//
	void closePopup();
}
