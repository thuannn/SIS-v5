package com.lemania.sis.client.form.classroom;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.classroom.ClassroomProxy;

public interface ClassroomUiHandlers extends UiHandlers {
	//
	void addClassroom(String name, int capacity, String note, boolean isActive);
	
	void updateClassroom( ClassroomProxy cp, String name, int capacity, String note, boolean isActive );
}
