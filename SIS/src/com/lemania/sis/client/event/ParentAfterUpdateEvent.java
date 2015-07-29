package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.lemania.sis.shared.parent.ParentProxy;

public class ParentAfterUpdateEvent extends
		GwtEvent<ParentAfterUpdateEvent.ParentAfterUpdateHandler> {

	public static Type<ParentAfterUpdateHandler> TYPE = new Type<ParentAfterUpdateHandler>();
	
	private ParentProxy updatedParent;

	public ParentProxy getUpdatedParent() {
		return updatedParent;
	}

	public interface ParentAfterUpdateHandler extends EventHandler {
		void onParentAfterUpdate(ParentAfterUpdateEvent event);
	}

	public interface ParentAfterUpdateHasHandlers extends HasHandlers {
		HandlerRegistration addParentAfterUpdateHandler(
				ParentAfterUpdateHandler handler);
	}

	public ParentAfterUpdateEvent() {
	}
	
	public ParentAfterUpdateEvent(ParentProxy updatedParent) {
		this.updatedParent = updatedParent;
	}

	@Override
	protected void dispatch(ParentAfterUpdateHandler handler) {
		handler.onParentAfterUpdate(this);
	}

	@Override
	public Type<ParentAfterUpdateHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ParentAfterUpdateHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ParentAfterUpdateEvent());
	}
}
