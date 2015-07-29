package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class CoursesLoadedEvent extends
		GwtEvent<CoursesLoadedEvent.CoursesLoadedHandler> {

	public static Type<CoursesLoadedHandler> TYPE = new Type<CoursesLoadedHandler>();

	public interface CoursesLoadedHandler extends EventHandler {
		void onCoursesLoaded(CoursesLoadedEvent event);
	}

	public interface CoursesLoadedHasHandlers extends HasHandlers {
		HandlerRegistration addCoursesLoadedHandler(CoursesLoadedHandler handler);
	}

	public CoursesLoadedEvent() {
	}

	@Override
	protected void dispatch(CoursesLoadedHandler handler) {
		handler.onCoursesLoaded(this);
	}

	@Override
	public Type<CoursesLoadedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CoursesLoadedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new CoursesLoadedEvent());
	}
}
