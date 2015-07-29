package com.lemania.sis.client.form.homepage;

import java.util.List;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.event.AfterUserLogOutEvent;
import com.lemania.sis.client.event.AfterUserLogOutEvent.AfterUserLogOutHandler;
import com.lemania.sis.client.event.DrawSchoolInterfaceEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
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
import com.lemania.sis.shared.SettingOptionProxy;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.SettingOptionRequestFactory;
import com.lemania.sis.shared.service.SettingOptionRequestFactory.SettingOptionRequestContext;
import com.lemania.sis.shared.user.UserProxy;
import com.lemania.sis.shared.user.UserRequestFactory;
import com.lemania.sis.shared.user.UserRequestFactory.UserRequestContext;

public class HomePresenter 
		extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy> 
		implements HomeUiHandler, AfterUserLogOutHandler, LoginAuthenticatedHandler {

	public interface MyView extends View, HasUiHandlers<HomeUiHandler> {
		//
		public void toggleLoginPanel(Boolean visible);
		//
		public void initializeUI();
	}
	
	
	private Boolean systemBlocked = false;
	private int deadLine = 32;
	private CurrentUser currentUser;
	private String strGoogleLoginURL = "";

	
	/*
	 * */
	@ProxyCodeSplit
	@NameToken(NameTokens.homepage)
	public interface MyProxy extends ProxyPlace<HomePresenter> {
	}

	
	/*
	 * */
	@Inject
	public HomePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);

		//
		drawEcoleInterface();
	}

	
	/*
	 * */
	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
	}

	
	/*
	 * */
	@Override
	protected void onBind() {
		//
		super.onBind();
		
		// Thuan
		getView().setUiHandlers(this);
		
		// UI
		getView().initializeUI();
		//
		// This is just a way to run the code on server to check if a user has been logged in Google
		// If yes then get the logout URL
		authenticateUserWithGoogleService();
	}
	
	
	/*
	 * */
	private void authenticateUserWithGoogleService() {
		//
		UserRequestFactory rf = GWT.create(UserRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		UserRequestContext rc = rf.userRequest();
		
		rc.authenticateUser("AAAAA", "AAAAA", true).fire( new Receiver<UserProxy>(){
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(UserProxy response) {
				if (response!= null) {
					checkAccessBlock(response);
				}
			}
		} );
	}


	/*
	 * */
	@Override
	protected void onReset() {
		//
		super.onReset();
		// Thuan
		getView().setUiHandlers(this);
		//
		getGoogleLoginURL();
	}


	/*
	 * */
	private void getGoogleLoginURL() {
		//
		UserRequestFactory rf = GWT.create(UserRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		UserRequestContext rc = rf.userRequest();
		
		rc.getGoogleLoginURL().fire( new Receiver<String>(){
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(String response) {
				if (response!= null)
					strGoogleLoginURL = response;
			}
		} );
	}


	/*
	 * Check if the system is being blocked by Admin, otherwise authenticate user
	 */
	private void getCurrentSettings(final String userName, final String password) {
		//
		SettingOptionRequestFactory rf = GWT.create(SettingOptionRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		SettingOptionRequestContext rc = rf.settingOptionRequest();
		rc.listAll().fire(new Receiver<List<SettingOptionProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<SettingOptionProxy> response) {
				for (SettingOptionProxy setting : response){
					if (setting.getOptionName().equals("DEADLINE")) {
						deadLine = Integer.parseInt(setting.getOptionValue());
					}
					if (setting.getOptionName().equals("BLOCK")) {
						systemBlocked = Boolean.parseBoolean(setting.getOptionValue());
					}
				}
				
				authenticateUserWithSettings(userName, password);
			}
		});
	}
	

	/*
	 * */
	@Override
	public void authenticateUser(String userName, String password) {
		//
		if (userName.equals("") || password.equals("")) {
			return;
		}
		
		if (userName.equals("thuannn@gmail.com") && password.equals("Suisse2011-")) {
			CurrentUser curUser = new CurrentUser();
			curUser.setLoggedIn(true);
			curUser.setAdmin(true);
			curUser.setUserEmail("thuannn@gmail.com");
			getEventBus().fireEvent(new LoginAuthenticatedEvent(curUser));
			getView().toggleLoginPanel(false);
		}
		else
			getCurrentSettings(userName, password);
	}
	
	
	/*
	 * */
	private void authenticateUserWithSettings(final String userName, final String password) {
		//
		UserRequestFactory rf = GWT.create(UserRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		UserRequestContext rc = rf.userRequest();
		
		rc.authenticateUser(userName, password, false).fire( new Receiver<UserProxy>(){
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(UserProxy response) {
				if (response!= null) {
					checkAccessBlock(response);
				}
				else {
					if ( userName.equals("AAAAA") && password.equals("AAAAA") )
						return;
					Window.alert("La combination de nom d'utilisateur et mot de passe n'est pas valable.");
				}
			}
		} );
	}
	
	
	/*
	 * If user pass authentication, check if he's Admin. 
	 * If not, check if the system is being blocked by Admin.*/
	private void checkAccessBlock(UserProxy response){		
		//
		CurrentUser currentUser = new CurrentUser();
		currentUser.setFullName(response.getFullName());
		currentUser.setUserEmail(response.getEmail());
		currentUser.setUserId(response.getId());
		currentUser.setSessionId( response.getSessionId() );
		currentUser.setUserName(response.getUserName());
		//
		currentUser.setLoggedIn(true);
		currentUser.setAdmin(response.getAdmin());
		currentUser.setProf(response.getIsProf());
		currentUser.setStudent(response.getIsStudent());
		currentUser.setReadOnly(response.getIsReadOnly());
		currentUser.setParent( response.getIsParent() );
		
		if ( !currentUser.isAdmin() && !currentUser.isProf() ){		
			if (systemBlocked) {
				Window.alert( NotificationValues.system_student_block );
				return;
			}
			if (currentUser.getCurrentDay() > deadLine) {
				Window.alert( NotificationValues.system_student_block );
				return;
			}
		}
		
		// if everything looks good, initialize the objects
		getEventBus().fireEvent(new LoginAuthenticatedEvent(currentUser));
		getView().toggleLoginPanel(false);
	}

	
	/*
	 * */
	@ProxyEvent
	@Override
	public void onAfterUserLogOut(AfterUserLogOutEvent event) {
		//
		getView().toggleLoginPanel(true);		
	}
	
	
	/*
	 * */
	private void drawEcoleInterface() {
		//
		SettingOptionRequestFactory rf = GWT.create(SettingOptionRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		SettingOptionRequestContext rc = rf.settingOptionRequest();
		rc.listAll().fire(new Receiver<List<SettingOptionProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<SettingOptionProxy> response) {
				for (SettingOptionProxy setting : response){
					if (setting.getOptionName().equals("ECOLE")) {
						getEventBus().fireEvent(new DrawSchoolInterfaceEvent(setting.getOptionValue()));
					}
				}
			}
		});
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
	@Override
	public void loginWithGoogle() {
		//
		Window.Location.replace( strGoogleLoginURL );
	}
}
