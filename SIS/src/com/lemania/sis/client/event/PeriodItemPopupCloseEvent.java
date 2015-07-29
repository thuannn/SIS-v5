package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class PeriodItemPopupCloseEvent extends
		GwtEvent<PeriodItemPopupCloseEvent.PeriodItemPopupCloseHandler> {

	public static Type<PeriodItemPopupCloseHandler> TYPE = new Type<PeriodItemPopupCloseHandler>();

	public interface PeriodItemPopupCloseHandler extends EventHandler {
		void onPeriodItemPopupClose(PeriodItemPopupCloseEvent event);
	}

	public interface PeriodItemPopupCloseHasHandlers extends HasHandlers {
		HandlerRegistration addPeriodItemPopupCloseHandler(
				PeriodItemPopupCloseHandler handler);
	}

	public PeriodItemPopupCloseEvent() {
	}

	@Override
	protected void dispatch(PeriodItemPopupCloseHandler handler) {
		handler.onPeriodItemPopupClose(this);
	}

	@Override
	public Type<PeriodItemPopupCloseHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<PeriodItemPopupCloseHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new PeriodItemPopupCloseEvent());
	}
}
