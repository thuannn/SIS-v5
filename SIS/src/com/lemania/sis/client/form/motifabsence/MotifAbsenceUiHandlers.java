package com.lemania.sis.client.form.motifabsence;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.motifabsence.MotifAbsenceProxy;

public interface MotifAbsenceUiHandlers extends UiHandlers {
	//
	void saveMotifs(
			boolean isEditing,		// if not editing, add new
			MotifAbsenceProxy editingMotif,
			String txtCode,
			String txtLabel,
			boolean chkReceivable,
			boolean chkOutside,
			boolean chkHealth,
			boolean chkDispense,
			String txtLetter,
			String txtSMS );
}
