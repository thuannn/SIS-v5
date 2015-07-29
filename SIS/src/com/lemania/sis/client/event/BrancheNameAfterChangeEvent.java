package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class BrancheNameAfterChangeEvent extends
		GwtEvent<BrancheNameAfterChangeEvent.BrancheNameAfterChangeHandler> {

	public static Type<BrancheNameAfterChangeHandler> TYPE = new Type<BrancheNameAfterChangeHandler>();

	public interface BrancheNameAfterChangeHandler extends EventHandler {
		void onBrancheNameAfterChange(BrancheNameAfterChangeEvent event);
	}

	public interface BrancheNameAfterChangeHasHandlers extends HasHandlers {
		HandlerRegistration addBrancheNameAfterChangeHandler(
				BrancheNameAfterChangeHandler handler);
	}

	public BrancheNameAfterChangeEvent() {
	}

	@Override
	protected void dispatch(BrancheNameAfterChangeHandler handler) {
		handler.onBrancheNameAfterChange(this);
	}

	@Override
	public Type<BrancheNameAfterChangeHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<BrancheNameAfterChangeHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new BrancheNameAfterChangeEvent());
	}
}
