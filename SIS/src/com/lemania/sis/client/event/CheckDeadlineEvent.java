package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class CheckDeadlineEvent extends
		GwtEvent<CheckDeadlineEvent.CheckDeadlineHandler> {

	public static Type<CheckDeadlineHandler> TYPE = new Type<CheckDeadlineHandler>();

	public interface CheckDeadlineHandler extends EventHandler {
		void onCheckDeadline(CheckDeadlineEvent event);
	}

	public interface CheckDeadlineHasHandlers extends HasHandlers {
		HandlerRegistration addCheckDeadlineHandler(CheckDeadlineHandler handler);
	}

	public CheckDeadlineEvent() {
	}

	@Override
	protected void dispatch(CheckDeadlineHandler handler) {
		handler.onCheckDeadline(this);
	}

	@Override
	public Type<CheckDeadlineHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CheckDeadlineHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new CheckDeadlineEvent());
	}
}
