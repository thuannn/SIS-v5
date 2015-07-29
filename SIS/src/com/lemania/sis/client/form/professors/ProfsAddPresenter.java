package com.lemania.sis.client.form.professors;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.event.ProfessorAfterAddEvent;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ProfessorRequestFactory;
import com.lemania.sis.shared.service.ProfessorRequestFactory.ProfessorRequestContext;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

public class ProfsAddPresenter 
	extends Presenter<ProfsAddPresenter.MyView, ProfsAddPresenter.MyProxy> 
	implements ProfessorAddUiHandler, LoginAuthenticatedHandler {
	
	//
	private CurrentUser currentUser;
	private ProfessorProxy prof;

	public interface MyView extends View, HasUiHandlers<ProfessorAddUiHandler> {
		void disableUiAfterAdd();
		void initializeUi();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.profsadd)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<ProfsAddPresenter> {
	}

	@Inject
	public ProfsAddPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
		
		// Thuan
		getView().setUiHandlers(this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset(){
		//
		getView().initializeUi();
	}

	@Override
	public void professorAddCancelled() {
		
		History.newItem(NameTokens.profs);
	}

	/*
	 * Create a new professor, fire the ProfessorAfterAddEvent to create a new access code.
	 * */
	@Override
	public void professorAdd(String profName, String profEmail, Boolean profStatus) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		if (profName.isEmpty()){
			Window.alert("Veuillez saissir le nom du professeur !");
			return;
		}
		
		if ( ! FieldValidation.isValidEmailAddress( profEmail ) ){
			Window.alert("Adresse email invalid !");
			return;
		}
		
		//
		ProfessorRequestFactory rf = GWT.create(ProfessorRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		
		ProfessorRequestContext rc = rf.professorRequest();
		prof = rc.create(ProfessorProxy.class);
		prof.setProfName(profName);
		prof.setProfActive(profStatus);
		prof.setProfEmail( profEmail );
		
		rc.saveAndReturn(prof).fire(new Receiver<ProfessorProxy>(){
			@Override
			public void onSuccess(ProfessorProxy response){
				getEventBus().fireEvent( new ProfessorAfterAddEvent(prof) );
				getView().initializeUi();
			}
			@Override
			public void onFailure(ServerFailure error) {
				AlertMessageBox msg = new AlertMessageBox( "Alert", error.getMessage() );
				msg.show();
			}
		});
	}

	
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		this.currentUser = event.getCurrentUser();
	}
}
