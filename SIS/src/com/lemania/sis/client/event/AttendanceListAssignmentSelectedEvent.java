package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.lemania.sis.shared.assignment.AssignmentProxy;

public class AttendanceListAssignmentSelectedEvent
		extends
		GwtEvent<AttendanceListAssignmentSelectedEvent.AttendanceListAssignmentSelectedHandler> {

	public static Type<AttendanceListAssignmentSelectedHandler> TYPE = new Type<AttendanceListAssignmentSelectedHandler>();
	
	//
	private AssignmentProxy a;

	public AssignmentProxy getAssignment() {
		return a;
	}

	public void setAssignment(AssignmentProxy a) {
		this.a = a;
	}

	public interface AttendanceListAssignmentSelectedHandler extends EventHandler {
		void onAttendanceListAssignmentSelected(
				AttendanceListAssignmentSelectedEvent event);
	}

	public interface AttendanceListAssignmentSelectedHasHandlers extends
			HasHandlers {
		HandlerRegistration addAttendanceListAssignmentSelectedHandler(
				AttendanceListAssignmentSelectedHandler handler);
	}

	public AttendanceListAssignmentSelectedEvent() {
	}
	
	public AttendanceListAssignmentSelectedEvent (AssignmentProxy aa) {
		this.a = aa;
	}

	@Override
	protected void dispatch(AttendanceListAssignmentSelectedHandler handler) {
		handler.onAttendanceListAssignmentSelected(this);
	}

	@Override
	public Type<AttendanceListAssignmentSelectedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AttendanceListAssignmentSelectedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new AttendanceListAssignmentSelectedEvent());
	}
}
