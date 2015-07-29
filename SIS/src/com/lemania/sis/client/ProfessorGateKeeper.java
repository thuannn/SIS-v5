package com.lemania.sis.client;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;

public class ProfessorGateKeeper implements Gatekeeper {
	
	private final EventBus eventBus;
	private CurrentUser currentUser = null;
	
	@Inject
	  public ProfessorGateKeeper (final EventBus eventBus) {
		  this.eventBus = eventBus;
		  this.eventBus.addHandler(LoginAuthenticatedEvent.getType(), new LoginAuthenticatedHandler() {
			  @Override
			  public void onLoginAuthenticated(LoginAuthenticatedEvent event){
				  currentUser = event.getCurrentUser();
			  }
		  });
	  }

	@Override
	public boolean canReveal() {
		 boolean isProf = false;
		  if (currentUser != null) {
			  isProf = ( (currentUser.isProf() || currentUser.isAdmin()) && currentUser.isLoggedIn());
		  }
		  return isProf;
	}
}