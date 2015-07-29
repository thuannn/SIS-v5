package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.lemania.sis.shared.EcoleProxy;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class EcoleAddedEvent extends
		GwtEvent<EcoleAddedEvent.EcoleAddedHandler> {

	public static Type<EcoleAddedHandler> TYPE = new Type<EcoleAddedHandler>();
	private EcoleProxy newEcole;

	public interface EcoleAddedHandler extends EventHandler {
		void onEcoleAdded(EcoleAddedEvent event);
	}

	public interface EcoleAddedHasHandlers extends HasHandlers {
		HandlerRegistration addEcoleAddedHandler(EcoleAddedHandler handler);
	}

	public EcoleAddedEvent(EcoleProxy newEcole) {
		this.newEcole = newEcole;
	}

	public EcoleProxy getNewEcole() {
		return newEcole;
	}

	@Override
	protected void dispatch(EcoleAddedHandler handler) {
		handler.onEcoleAdded(this);
	}

	@Override
	public Type<EcoleAddedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<EcoleAddedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, EcoleProxy newEcole) {
		source.fireEvent(new EcoleAddedEvent(newEcole));
	}
}
