package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.lemania.sis.shared.ProfessorProxy;

public class ProfessorAfterAddEvent extends
		GwtEvent<ProfessorAfterAddEvent.ProfessorAfterAddHandler> {

	public static Type<ProfessorAfterAddHandler> TYPE = new Type<ProfessorAfterAddHandler>();
	
	private ProfessorProxy prof;

	public interface ProfessorAfterAddHandler extends EventHandler {
		void onProfessorAfterAdd(ProfessorAfterAddEvent event);
	}

	public ProfessorAfterAddEvent() {
	}
	
	public ProfessorAfterAddEvent( ProfessorProxy prof ) {
		this.prof = prof;
	}

	@Override
	protected void dispatch(ProfessorAfterAddHandler handler) {
		handler.onProfessorAfterAdd(this);
	}

	@Override
	public Type<ProfessorAfterAddHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ProfessorAfterAddHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ProfessorAfterAddEvent());
	}

	public ProfessorProxy getProf() {
		return prof;
	}

	public void setProf(ProfessorProxy prof) {
		this.prof = prof;
	}
}
