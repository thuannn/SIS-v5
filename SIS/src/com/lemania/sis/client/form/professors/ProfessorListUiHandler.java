package com.lemania.sis.client.form.professors;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.assignment.AssignmentProxy;

public interface ProfessorListUiHandler extends UiHandlers {
	
	void updateProfessorStatus(ProfessorProxy prof, Boolean status);
	void updateProfessorName(ProfessorProxy prof, String name);
	
	void professorSelected(ProfessorProxy prof);
	
	void onEcoleSelected(String ecoleId);
	void onProgrammeSelected(String coursId);
	void onClassSelected();
	
	void saveAssignment(String professorId, String classId, String subjectId, Boolean isActive);
	void updateAssignmentStatus(AssignmentProxy assignment, Boolean value);
}
