package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.lemania.sis.shared.CoursProxy;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class CoursAddedEvent extends
		GwtEvent<CoursAddedEvent.CoursAddedHandler> {

	public static Type<CoursAddedHandler> TYPE = new Type<CoursAddedHandler>();
	private CoursProxy newCours;

	public interface CoursAddedHandler extends EventHandler {
		void onCoursAdded(CoursAddedEvent event);
	}

	public interface CoursAddedHasHandlers extends HasHandlers {
		HandlerRegistration addCoursAddedHandler(CoursAddedHandler handler);
	}

	public CoursAddedEvent(CoursProxy newCours) {
		this.newCours = newCours;
	}

	public CoursProxy getNewCours() {
		return newCours;
	}

	@Override
	protected void dispatch(CoursAddedHandler handler) {
		handler.onCoursAdded(this);
	}

	@Override
	public Type<CoursAddedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CoursAddedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, CoursProxy newCours) {
		source.fireEvent(new CoursAddedEvent(newCours));
	}
}
