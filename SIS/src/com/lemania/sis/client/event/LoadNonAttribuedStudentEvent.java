package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class LoadNonAttribuedStudentEvent extends
		GwtEvent<LoadNonAttribuedStudentEvent.LoadNonAttribuedStudentHandler> {

	public static Type<LoadNonAttribuedStudentHandler> TYPE = new Type<LoadNonAttribuedStudentHandler>();

	public interface LoadNonAttribuedStudentHandler extends EventHandler {
		void onLoadNonAttribuedStudent(LoadNonAttribuedStudentEvent event);
	}

	public interface LoadNonAttribuedStudentHasHandlers extends HasHandlers {
		HandlerRegistration addLoadNonAttribuedStudentHandler(
				LoadNonAttribuedStudentHandler handler);
	}

	public LoadNonAttribuedStudentEvent() {
	}

	@Override
	protected void dispatch(LoadNonAttribuedStudentHandler handler) {
		handler.onLoadNonAttribuedStudent(this);
	}

	@Override
	public Type<LoadNonAttribuedStudentHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadNonAttribuedStudentHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new LoadNonAttribuedStudentEvent());
	}
}
