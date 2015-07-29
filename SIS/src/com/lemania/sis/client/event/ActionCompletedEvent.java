package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class ActionCompletedEvent extends
		GwtEvent<ActionCompletedEvent.ActionCompletedHandler> {

	public static Type<ActionCompletedHandler> TYPE = new Type<ActionCompletedHandler>();

	public interface ActionCompletedHandler extends EventHandler {
		void onActionCompleted(ActionCompletedEvent event);
	}

	public interface ActionCompletedHasHandlers extends HasHandlers {
		HandlerRegistration addActionCompletedHandler(ActionCompletedHandler handler);
	}

	public ActionCompletedEvent() {
	}

	@Override
	protected void dispatch(ActionCompletedHandler handler) {
		handler.onActionCompleted(this);
	}

	@Override
	public Type<ActionCompletedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ActionCompletedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ActionCompletedEvent());
	}
}
