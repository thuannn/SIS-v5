package com.lemania.sis.client.form.studentmgt;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.event.StudentAfterAddEvent;
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
import com.lemania.sis.client.uihandler.StudentAddUiHandler;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.student.StudentProxy;
import com.lemania.sis.shared.student.StudentRequestFactory;
import com.lemania.sis.shared.student.StudentRequestFactory.StudentRequestContext;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

public class StudentAddPresenter 
	extends Presenter<StudentAddPresenter.MyView, StudentAddPresenter.MyProxy> 
	implements StudentAddUiHandler, LoginAuthenticatedHandler {
	
	//
	private CurrentUser currentUser;

	public interface MyView extends View, HasUiHandlers<StudentAddUiHandler> {
		public void statusMessage(String msg);
		public void resetForm(); 
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.studentadd)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<StudentAddPresenter> {
	}

	@Inject
	public StudentAddPresenter(final EventBus eventBus, final MyView view,
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
	protected void onReset() {
		super.onReset();
	}
	

	@Override
	public void createStudent(String firstName, String lastName, String email, Boolean active) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		if (firstName.isEmpty()) {
			Window.alert( NotificationValues.invalid_input + "Prénom" );
			return;
		}
		if (lastName.isEmpty()) {
			Window.alert( NotificationValues.invalid_input + "Nom");
			return;
		}
		if ( ! FieldValidation.isValidEmailAddress( email )) {
			Window.alert( NotificationValues.invalid_input + "Email");
			return;
		}
		
		//
		StudentRequestContext rc = getStudentRequestContext();
		rc.saveAndReturn(firstName, lastName, email, active).fire(new Receiver<StudentProxy>(){
			@Override
			public void onFailure(ServerFailure error) {
				AlertMessageBox msg = new AlertMessageBox( "Alert", error.getMessage() );
				msg.show();
			}
			@Override
			public void onSuccess(StudentProxy response) {
				getEventBus().fireEvent(new StudentAfterAddEvent(response));
				getView().statusMessage( NotificationValues.student_create_good );
			}
		});
	}
	
	/* Get the request context for StudenProxy.
	 * Used in every function which call to Request Factory */
	public StudentRequestContext getStudentRequestContext() {
		StudentRequestFactory rf = GWT.create(StudentRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		return rf.studentRequest();
	}
	

	/*
	 * 
	 * */
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		this.currentUser = event.getCurrentUser();
	}
}
