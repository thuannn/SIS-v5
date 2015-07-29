package com.lemania.sis.client.event;

/*
 * This event is fired when a user add or remove an absence item in the Absence Input popup in Absence Management form
 * 
 * */

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class AbsenceAfterInputEvent extends
		GwtEvent<AbsenceAfterInputEvent.AbsenceAfterInputHandler> {

	public static Type<AbsenceAfterInputHandler> TYPE = new Type<AbsenceAfterInputHandler>();

	public interface AbsenceAfterInputHandler extends EventHandler {
		void onAbsenceAfterInput(AbsenceAfterInputEvent event);
	}

	public interface AbsenceAfterInputHasHandlers extends HasHandlers {
		HandlerRegistration addAbsenceAfterInputHandler(
				AbsenceAfterInputHandler handler);
	}

	public AbsenceAfterInputEvent() {
	}

	@Override
	protected void dispatch(AbsenceAfterInputHandler handler) {
		handler.onAbsenceAfterInput(this);
	}

	@Override
	public Type<AbsenceAfterInputHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AbsenceAfterInputHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new AbsenceAfterInputEvent());
	}
}
