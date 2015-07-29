package com.lemania.sis.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.EventBus;
import com.lemania.sis.client.CurrentUser;

public class LoginAuthenticatedEvent extends
		GwtEvent<LoginAuthenticatedEvent.LoginAuthenticatedHandler> {

	public static Type<LoginAuthenticatedHandler> TYPE = new Type<LoginAuthenticatedHandler>();

	private final CurrentUser currentUser;
	
	public interface LoginAuthenticatedHandler extends EventHandler {
		void onLoginAuthenticated(LoginAuthenticatedEvent event);
	}

	public interface LoginAuthenticatedHasHandlers extends HasHandlers {
		HandlerRegistration addLoginAuthenticatedHandler(
				LoginAuthenticatedHandler handler);
	}

	public LoginAuthenticatedEvent(CurrentUser currentUser) {
		this.currentUser = currentUser;
	}
	
	public CurrentUser getCurrentUser(){
		return this.currentUser;
	}

	@Override
	protected void dispatch(LoginAuthenticatedHandler handler) {
		handler.onLoginAuthenticated(this);
	}

	@Override
	public Type<LoginAuthenticatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoginAuthenticatedHandler> getType() {
		return TYPE;
	}

	public static void fire(EventBus eventBus, CurrentUser currentUser) {
		eventBus.fireEvent(new LoginAuthenticatedEvent(currentUser));
	}
}
