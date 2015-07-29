package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.lemania.sis.shared.parent.ParentProxy;

/*
 * Presenters :
 * 		- ParentManagementPresenter : show newly added Parent
 * 		- UserPresenter : create new User for newly added Parent
 * */

public class ParentAfterAddEvent extends
		GwtEvent<ParentAfterAddEvent.ParentAfterAddHandler> {

	public static Type<ParentAfterAddHandler> TYPE = new Type<ParentAfterAddHandler>();
	
	private ParentProxy addedParent;

	public interface ParentAfterAddHandler extends EventHandler {
		void onParentAfterAdd(ParentAfterAddEvent event);
	}

	public interface ParentAfterAddHasHandlers extends HasHandlers {
		HandlerRegistration addParentAfterAddHandler(ParentAfterAddHandler handler);
	}

	public ParentAfterAddEvent() {
	}
	
	public ParentAfterAddEvent(ParentProxy addedParent) {
		this.addedParent = addedParent;
	}

	@Override
	protected void dispatch(ParentAfterAddHandler handler) {
		handler.onParentAfterAdd(this);
	}

	@Override
	public Type<ParentAfterAddHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ParentAfterAddHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ParentAfterAddEvent());
	}

	public ParentProxy getAddedParent() {
		return addedParent;
	}
}
