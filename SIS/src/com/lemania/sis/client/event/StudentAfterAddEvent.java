package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.lemania.sis.shared.student.StudentProxy;

/*
 * StudentAfterAddEvent
 * After adding a new student, UserManagementPresenter will create a new user account in the system. */
public class StudentAfterAddEvent extends
		GwtEvent<StudentAfterAddEvent.StudentAfterAddHandler> {

	public static Type<StudentAfterAddHandler> TYPE = new Type<StudentAfterAddHandler>();
	
	private StudentProxy student;

	public interface StudentAfterAddHandler extends EventHandler {
		void onStudentAfterAdd(StudentAfterAddEvent event);
	}

	public interface StudentAfterAddHasHandlers extends HasHandlers {
		HandlerRegistration addStudentAfterAddHandler(StudentAfterAddHandler handler);
	}

	public StudentAfterAddEvent() {
	}
	
	public StudentAfterAddEvent(StudentProxy student){
		this.student = student;
	}

	@Override
	protected void dispatch(StudentAfterAddHandler handler) {
		handler.onStudentAfterAdd(this);
	}

	@Override
	public Type<StudentAfterAddHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<StudentAfterAddHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new StudentAfterAddEvent());
	}
	
	public StudentProxy getStudent() {
		return student;
	}

	public void setStudent(StudentProxy student) {
		this.student = student;
	}
}
