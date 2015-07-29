package com.lemania.sis.client.form.masteragenda;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.masteragendaitem.MasterAgendaItemProxy;

public interface MasterAgendaUiHandlers extends UiHandlers {
	//
	void onClassChanged( String classId );
	//
	void onProfileChanged( String profileId );
	//
	void addMasterAgendaItem( String jourCode, String periodId, String profileId, String profileSubjectId, String classroomId, int duration );
	//
	void removeMasterAgendaItem(MasterAgendaItemProxy mai);
}
