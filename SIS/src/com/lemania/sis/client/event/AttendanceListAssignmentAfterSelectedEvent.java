package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class AttendanceListAssignmentAfterSelectedEvent
		extends
		GwtEvent<AttendanceListAssignmentAfterSelectedEvent.AttendanceListAssignmentAfterSelectedHandler> {

	public static Type<AttendanceListAssignmentAfterSelectedHandler> TYPE = new Type<AttendanceListAssignmentAfterSelectedHandler>();
	
	private String assignmentId;

	public interface AttendanceListAssignmentAfterSelectedHandler extends
			EventHandler {
		void onAttendanceListAssignmentAfterSelected(
				AttendanceListAssignmentAfterSelectedEvent event);
	}

	public interface AttendanceListAssignmentAfterSelectedHasHandlers extends
			HasHandlers {
		HandlerRegistration addAttendanceListAssignmentAfterSelectedHandler(
				AttendanceListAssignmentAfterSelectedHandler handler);
	}

	public AttendanceListAssignmentAfterSelectedEvent() {
	}
	
	public AttendanceListAssignmentAfterSelectedEvent(String assignmentId) {
		//
		this.assignmentId = assignmentId;
	}

	@Override
	protected void dispatch(AttendanceListAssignmentAfterSelectedHandler handler) {
		handler.onAttendanceListAssignmentAfterSelected(this);
	}

	@Override
	public Type<AttendanceListAssignmentAfterSelectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AttendanceListAssignmentAfterSelectedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new AttendanceListAssignmentAfterSelectedEvent());
	}

	public String getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}
}
