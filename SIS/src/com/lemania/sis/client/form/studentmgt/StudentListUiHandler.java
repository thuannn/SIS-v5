package com.lemania.sis.client.form.studentmgt;

import com.gwtplatform.mvp.client.UiHandlers;
import com.lemania.sis.shared.student.StudentProxy;

public interface StudentListUiHandler extends UiHandlers {
	//
	void updateStudentStatus(StudentProxy student, Boolean value);
	void updateStudentFirstName(StudentProxy student, String firstName);
	void updateStudentLastName(StudentProxy student, String lastName);
	void updateStudentEmail(StudentProxy student, String email);
	//
	void listAllStudentActive();
	void listAllStudentInactive();
	void listAllStudent();
}
