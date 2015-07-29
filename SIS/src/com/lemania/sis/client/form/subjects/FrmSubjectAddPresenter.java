package com.lemania.sis.client.form.subjects;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.CurrentUser;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.SubjectProxy;
import com.lemania.sis.shared.service.SubjectRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.SubjectRequestFactory.SubjectRequestContext;

public class FrmSubjectAddPresenter
		extends Presenter<FrmSubjectAddPresenter.MyView, FrmSubjectAddPresenter.MyProxy> 
		implements FrmSubjectAddUiHandler, LoginAuthenticatedHandler {
	
	//
	private CurrentUser currentUser;
	

	public interface MyView extends View, HasUiHandlers<FrmSubjectAddUiHandler> {
		void resetForm();
		void showStatus(String msg);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.subjectadd)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<FrmSubjectAddPresenter> {
	}

	@Inject
	public FrmSubjectAddPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		// Thuan
		getView().setUiHandlers(this);
	}
	
	@Override
	protected void onReset(){
		//
		super.onReset();	
		// Thuan
		getView().resetForm();
	}
	
	
	/**/
	@Override
	public void addNewSubject(String subjectName, String defaultCoef, Boolean isActive) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		// Validate data
		if ( subjectName.isEmpty() ){
			Window.alert( NotificationValues.invalid_input + " - Nom de la matière.");
			return;
		}
		if ( FieldValidation.isNumeric( defaultCoef) ) {
			if ( Double.parseDouble( defaultCoef ) < 0 ) {
				Window.alert( NotificationValues.invalid_input + " - Coefficient");
				return;
			}
		} else {
			Window.alert( NotificationValues.invalid_input + " - Coefficient");
			return;
		}
		// Save
		SubjectRequestFactory rf = GWT.create(SubjectRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		SubjectRequestContext rc = rf.subjectRequest();
		
		SubjectProxy ep = rc.create(SubjectProxy.class);
		ep.setSubjectName( subjectName );
		ep.setDefaultCoef( Double.parseDouble( defaultCoef ) );
		ep.setIsActive( isActive );
		rc.save(ep).fire( new Receiver<Void>() {
			@Override
			public void onSuccess(Void response){
				getView().showStatus( NotificationValues.subject_create_good );
				getView().resetForm();
			}
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
		} );
		//
	}

	
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		this.currentUser = event.getCurrentUser();
	}
}
