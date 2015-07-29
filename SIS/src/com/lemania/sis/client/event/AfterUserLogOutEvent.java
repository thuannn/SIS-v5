package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class AfterUserLogOutEvent extends
		GwtEvent<AfterUserLogOutEvent.AfterUserLogOutHandler> {

	public static Type<AfterUserLogOutHandler> TYPE = new Type<AfterUserLogOutHandler>();

	public interface AfterUserLogOutHandler extends EventHandler {
		void onAfterUserLogOut(AfterUserLogOutEvent event);
	}

	public interface AfterUserLogOutHasHandlers extends HasHandlers {
		HandlerRegistration addAfterUserLogOutHandler(AfterUserLogOutHandler handler);
	}

	public AfterUserLogOutEvent() {
	}

	@Override
	protected void dispatch(AfterUserLogOutHandler handler) {
		handler.onAfterUserLogOut(this);
	}

	@Override
	public Type<AfterUserLogOutHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AfterUserLogOutHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new AfterUserLogOutEvent());
	}
}
