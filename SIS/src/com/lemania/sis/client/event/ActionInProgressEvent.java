package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class ActionInProgressEvent extends
		GwtEvent<ActionInProgressEvent.ActionInProgressHandler> {

	public static Type<ActionInProgressHandler> TYPE = new Type<ActionInProgressHandler>();

	public interface ActionInProgressHandler extends EventHandler {
		void onActionInProgress(ActionInProgressEvent event);
	}

	public interface ActionInProgressHasHandlers extends HasHandlers {
		HandlerRegistration addActionInProgressHandler(
				ActionInProgressHandler handler);
	}

	public ActionInProgressEvent() {
	}

	@Override
	protected void dispatch(ActionInProgressHandler handler) {
		handler.onActionInProgress(this);
	}

	@Override
	public Type<ActionInProgressHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ActionInProgressHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ActionInProgressEvent());
	}
}
