package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.lemania.sis.shared.motifabsence.MotifAbsenceProxy;

public class MotifAbsenceAfterAddEvent extends
		GwtEvent<MotifAbsenceAfterAddEvent.MotifAbsenceAfterAddHandler> {

	public static Type<MotifAbsenceAfterAddHandler> TYPE = new Type<MotifAbsenceAfterAddHandler>();
	
	//
	MotifAbsenceProxy motif;

	public MotifAbsenceProxy getMotif() {
		return motif;
	}

	public void setMotif(MotifAbsenceProxy motif) {
		this.motif = motif;
	}

	public interface MotifAbsenceAfterAddHandler extends EventHandler {
		void onMotifAbsenceAfterAdd(MotifAbsenceAfterAddEvent event);
	}

	public interface MotifAbsenceAfterAddHasHandlers extends HasHandlers {
		HandlerRegistration addMotifAbsenceAfterAddHandler(
				MotifAbsenceAfterAddHandler handler);
	}

	public MotifAbsenceAfterAddEvent() {
	}

	@Override
	protected void dispatch(MotifAbsenceAfterAddHandler handler) {
		handler.onMotifAbsenceAfterAdd(this);
	}

	@Override
	public Type<MotifAbsenceAfterAddHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MotifAbsenceAfterAddHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MotifAbsenceAfterAddEvent());
	}
}
