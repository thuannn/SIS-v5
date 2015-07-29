package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class StudentAfterStatusChangeEvent extends
		GwtEvent<StudentAfterStatusChangeEvent.StudentAfterStatusChangeHandler> {
	// Thuan
	private String studentId = "";
	private String studentEmail = "";
	private Boolean status; 

	public static Type<StudentAfterStatusChangeHandler> TYPE = new Type<StudentAfterStatusChangeHandler>();

	public interface StudentAfterStatusChangeHandler extends EventHandler {
		void onStudentAfterDesactivate(StudentAfterStatusChangeEvent event);
	}

	public interface StudentAfterDesactivateHasHandlers extends HasHandlers {
		HandlerRegistration addStudentAfterDesactivateHandler(
				StudentAfterStatusChangeHandler handler);
	}

	public StudentAfterStatusChangeEvent() {		
	}
	
	public StudentAfterStatusChangeEvent( String studentId, String studentEmail, Boolean status ) {
		this.studentId = studentId;
		this.studentEmail = studentEmail;
		this.status = status;
	}

	@Override
	protected void dispatch(StudentAfterStatusChangeHandler handler) {
		handler.onStudentAfterDesactivate(this);
	}

	@Override
	public Type<StudentAfterStatusChangeHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<StudentAfterStatusChangeHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new StudentAfterStatusChangeEvent());
	}
	
	public String getStudentId(){
		return this.studentId;
	}
	
	public Boolean getStudentStatus(){
		return this.status;
	}

	public String getStudentEmail() {
		return this.studentEmail;
	}

	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}
}
