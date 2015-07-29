package com.lemania.sis.client.form.usermgt;

import java.util.List;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.event.ParentAfterAddEvent;
import com.lemania.sis.client.event.ParentAfterAddEvent.ParentAfterAddHandler;
import com.lemania.sis.client.event.ProfessorAfterAddEvent;
import com.lemania.sis.client.event.ProfessorAfterAddEvent.ProfessorAfterAddHandler;
import com.lemania.sis.client.event.StudentAfterAddEvent;
import com.lemania.sis.client.event.StudentAfterAddEvent.StudentAfterAddHandler;
import com.lemania.sis.client.event.StudentAfterStatusChangeEvent;
import com.lemania.sis.client.event.StudentAfterStatusChangeEvent.StudentAfterStatusChangeHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.client.values.NotificationValues;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.CurrentUser;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.parent.ParentProxy;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.student.StudentProxy;
import com.lemania.sis.shared.user.UserProxy;
import com.lemania.sis.shared.user.UserRequestFactory;
import com.lemania.sis.shared.user.UserRequestFactory.UserRequestContext;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class UserManagementPresenter
		extends Presenter<UserManagementPresenter.MyView, UserManagementPresenter.MyProxy> 
		implements UserManagementUiHandler, 
				StudentAfterAddHandler, 
				ProfessorAfterAddHandler, 
				StudentAfterStatusChangeHandler, 
				LoginAuthenticatedHandler,
				ParentAfterAddHandler {
	
	//
	private CurrentUser currentUser;
	

	public interface MyView extends View, HasUiHandlers<UserManagementUiHandler> {
		//
		public void initializeTables();
		public void initilizeUI();
		//
		public void populateSelectedUserInfo();
		//		
		public void refreshTable(UserProxy updatedUser);
		//
		public void addNewUser(UserProxy newUser);
		public void setUserData(List<UserProxy> list);
		//
		public void showAddPanel();
		//
		public void hideCodesAcces(boolean hide);
	}
	

	@ProxyCodeSplit
	@NameToken(NameTokens.usermanagement)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<UserManagementPresenter> {
	}

	@Inject
	public UserManagementPresenter(final EventBus eventBus, final MyView view,
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
		getView().initializeTables();
		getView().initilizeUI();
	}
	
	@Override
	protected void onReset() {	
		//
		if (this.currentUser.getUserEmail().equals("thuannn@gmail.com"))
			getView().showAddPanel();
		//
		if (this.currentUser.isReadOnly())
			getView().hideCodesAcces(true);
		else
			getView().hideCodesAcces(false);
	}

	private void loadUsers() {
		UserRequestFactory rf = GWT.create(UserRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		UserRequestContext rc = rf.userRequest();
		rc.listAll().fire(new Receiver<List<UserProxy>>() {
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<UserProxy> response) {
				getView().setUserData(response);
			}
		});
	}

	@Override
	public void addNewUser(String fullName, String userName, String password, String email) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		UserRequestFactory rf = GWT.create(UserRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		UserRequestContext rc = rf.userRequest();
		
		final UserProxy newUser = rc.create(UserProxy.class);
		newUser.setFullName(fullName);
		newUser.setUserName(userName);
		newUser.setPassword(password);
		newUser.setEmail(email);
		newUser.setActive(true);
		
		rc.save(newUser).fire(new Receiver<Void>() {
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(Void response) {
				getView().addNewUser(newUser);
			}
		});
	}

	@Override
	public void updateUserStatus(UserProxy user, Boolean active, Boolean admin, Boolean isProf, Boolean isStudent, Boolean isParent, String password) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		UserRequestFactory rf = GWT.create(UserRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		UserRequestContext rc = rf.userRequest();
		final UserProxy updatedUser = rc.edit(user);
		updatedUser.setActive(active);
		updatedUser.setAdmin(admin);
		updatedUser.setIsProf(isProf);
		updatedUser.setIsStudent(isStudent);
		updatedUser.setIsParent(isParent);
		
		if (!password.equals(""))
			updatedUser.setPassword(password);
		
		rc.save(updatedUser).fire( new Receiver<Void>(){
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(Void response) {
				getView().refreshTable(updatedUser);
			}
		} );	
	}

	@ProxyEvent
	@Override
	public void onStudentAfterAdd(StudentAfterAddEvent event) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		UserRequestFactory rf = GWT.create(UserRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		UserRequestContext rc = rf.userRequest();
		
		StudentProxy student = event.getStudent();
		UserProxy updatedUser = rc.create( UserProxy.class );
		updatedUser.setFullName(student.getFirstName() + " " + student.getLastName() );
		updatedUser.setActive( student.getIsActive() );
		updatedUser.setAdmin( false );
		updatedUser.setIsProf( false );
		updatedUser.setIsStudent( true );
		updatedUser.setEmail( student.getEmail() );
		updatedUser.setUserName( student.getEmail() );
		updatedUser.setPassword( Long.toHexString(Double.doubleToLongBits(Math.random())).substring(8) );
		
		rc.save(updatedUser).fire( new Receiver<Void>(){
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(Void response) {
				Window.alert( NotificationValues.student_code_access_created );
			}
		} );	
	}

	/*
	 * Create a new access code for a newly created professor.
	 * */
	@ProxyEvent
	@Override
	public void onProfessorAfterAdd(ProfessorAfterAddEvent event) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		UserRequestFactory rf = GWT.create(UserRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		UserRequestContext rc = rf.userRequest();
		
		ProfessorProxy prof = event.getProf();
		UserProxy updatedUser = rc.create( UserProxy.class );
		updatedUser.setFullName( prof.getProfName() );
		updatedUser.setActive( prof.getProfActive() );
		updatedUser.setAdmin( false );
		updatedUser.setIsProf( true );
		updatedUser.setIsStudent( false );
		updatedUser.setEmail( prof.getProfEmail() );
		updatedUser.setUserName( prof.getProfEmail() );
		updatedUser.setPassword( Long.toHexString(Double.doubleToLongBits(Math.random())).substring(8) );
		
		rc.save(updatedUser).fire( new Receiver<Void>(){
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(Void response) {
				Window.alert( NotificationValues.prof_code_access_created );
			}
		} );		
	}

	@Override
	public void loadUsersByType(String type) {
		//
		if (type.equals(""))
			return;
		//
		if (type.equals("tout")) {
			loadUsers();
			return;
		}
		//
		UserRequestFactory rf = GWT.create(UserRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		UserRequestContext rc = rf.userRequest();
		rc.listAllByType(type).fire(new Receiver<List<UserProxy>>() {
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<UserProxy> response) {
				getView().setUserData(response);
			}
		});
	}

	@ProxyEvent
	@Override
	public void onStudentAfterDesactivate(StudentAfterStatusChangeEvent event) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//		
		UserRequestFactory rf = GWT.create(UserRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		UserRequestContext rc = rf.userRequest();		
		rc.updateUserActiveStatus(event.getStudentEmail(), event.getStudentStatus()).fire( new Receiver<Void>(){
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(Void response) {
				//
			}
		} );	
	}


	/*
	 * */
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		this.currentUser = event.getCurrentUser();
	}

	
	/*
	 * */
	@ProxyEvent
	@Override
	public void onParentAfterAdd(ParentAfterAddEvent event) {
		//
		UserRequestFactory rf = GWT.create(UserRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		UserRequestContext rc = rf.userRequest();
		
		ParentProxy parent = event.getAddedParent();
		UserProxy updatedUser = rc.create( UserProxy.class );
		updatedUser.setFullName( parent.getLastName() + " " + parent.getFirstName() );
		updatedUser.setActive( true );
		updatedUser.setAdmin( false );
		updatedUser.setIsProf( false );
		updatedUser.setIsStudent( false );
		updatedUser.setIsParent( true );
		updatedUser.setEmail( parent.geteMail() );
		updatedUser.setUserName( parent.geteMail() );
		updatedUser.setPassword( Long.toHexString(Double.doubleToLongBits(Math.random())).substring(8) );
		
		rc.save(updatedUser).fire( new Receiver<Void>(){
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(Void response) {
				Window.alert( NotificationValues.user_created );
			}
		} );		
	}	
}
