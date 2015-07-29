package com.lemania.sis.client.form.mainpage;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.event.ActionCompletedEvent;
import com.lemania.sis.client.event.ActionCompletedEvent.ActionCompletedHandler;
import com.lemania.sis.client.event.ActionInProgressEvent;
import com.lemania.sis.client.event.ActionInProgressEvent.ActionInProgressHandler;
import com.lemania.sis.client.event.AfterUserLogOutEvent;
import com.lemania.sis.client.event.DrawSchoolInterfaceEvent;
import com.lemania.sis.client.event.DrawSchoolInterfaceEvent.DrawSchoolInterfaceHandler;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.user.UserRequestFactory;
import com.lemania.sis.shared.user.UserRequestFactory.UserRequestContext;

public class MainPagePresenter extends
		Presenter<MainPagePresenter.MyView, MainPagePresenter.MyProxy>
		implements 	MainPageUiHandler, LoginAuthenticatedHandler, 
					ActionInProgressHandler, ActionCompletedHandler,
					DrawSchoolInterfaceHandler {
	/**
	   * Child presenters can fire a RevealContentEvent with TYPE_SetMainContent to set themselves
	   * as children of this presenter.
	   */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetMainContent = new Type<RevealContentHandler<?>>();
	
	private CurrentUser currentUser;
	private String strGoogleLogoutURL = "";

	public interface MyView extends View, HasUiHandlers<MainPageUiHandler> {
		//
		void showUserInfo(CurrentUser currentUser);
		void initializeUi(CurrentUser currentUser);
		void showProgressBar(boolean visible);
		void enableMainPanel(Boolean disabled);
//		void showCurrentPageOnMenu( String tokenName );
		//
		void drawSchoolInterface(String schoolCode);
		void setWindowEventHanlder();
	}
	
	@ProxyStandard
	public interface MyProxy extends Proxy<MainPagePresenter> {
	}

	@Inject
	public MainPagePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);	
	}

	@Override
	protected void revealInParent() {
		//
		RevealRootContentEvent.fire(this, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		// Thuan: attach Ui handler
		getView().setUiHandlers(this);
		
		//
		getView().setWindowEventHanlder();
	}

	
	/*
	 * */
	@Override
	protected void onReset() {
		//
		getView().initializeUi(currentUser);
	}

	
	/*
	 * */
	@Override
	public void showHomepage() {
		// 
		History.newItem(NameTokens.homepage, true);
	}

	
	/*
	 * */
	@Override
	public void showEcoleList() {
		// 
		History.newItem(NameTokens.ecolepage, true);
	}

	@Override
	public void showEcoleAdd() {
		// 
		History.newItem(NameTokens.addecole, true);
	}

	@Override
	public void showCoursList() {
		//
		History.newItem(NameTokens.cours, true);
	}

	@Override
	public void showCoursAdd() {
		// 
		History.newItem(NameTokens.coursadd, true);
	}

	@Override
	public void showProfessorList() {
		// 
		History.newItem(NameTokens.profs, true);
	}

	@Override
	public void showProfessorAdd() {
		// 
		History.newItem(NameTokens.profsadd, true);
	}

	@Override
	public void showTypeList() {
		//
		History.newItem(NameTokens.types, true);
	}

	@Override
	public void showTypeAdd() {
		//
		History.newItem(NameTokens.typesadd, true);
	}

	@Override
	public void showTimeInput() {
		// 
		History.newItem(NameTokens.timeinput, true);
	}

	@Override
	public void showContact() {
		//
		History.newItem(NameTokens.contact);
	}
	
	@Override
	public void showUserManagement() {
		History.newItem(NameTokens.usermanagement);
	}

	@Override
	public void logOut() {
		//
		if (currentUser == null)
			currentUser = new CurrentUser();
		currentUser.setLoggedIn(false);
		//
		this.getEventBus().fireEvent(new LoginAuthenticatedEvent(currentUser));
		this.getEventBus().fireEvent(new AfterUserLogOutEvent());
		//
		if (strGoogleLogoutURL != "")
			Window.Location.replace( strGoogleLogoutURL );
	}

	/*
	 * */
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		currentUser = event.getCurrentUser();
		getView().initializeUi(currentUser);
		//
		getGoogleLogoutURL();
	}
	
	/*
	 * */
	public void getGoogleLogoutURL() {
		//
		// Get Google logout URL
		UserRequestFactory rf = GWT.create(UserRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		UserRequestContext rc = rf.userRequest();
		
		rc.getGoogleLogoutURL( currentUser.getUserEmail() ).fire( new Receiver<String>(){
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(String response) {
				if (response!= null)
					strGoogleLogoutURL = response;
			}
		} );
	}
	

	@Override
	public void showExtractDataForm() {
		History.newItem(NameTokens.extractdata);
	}

	@Override
	public void showRptByDept() {
		History.newItem(NameTokens.rptbydept);		
	}
	
	@Override
	public void showRptBySchool() {
		History.newItem(NameTokens.rptbyschool);		
	}

	@Override
	public void showRptByMonth() {
		// 
		History.newItem(NameTokens.rpttimebymonth);
	}

	@ProxyEvent
	@Override
	public void onActionInProgress(ActionInProgressEvent event) {
		// 
		getView().showProgressBar(true);
		// getView().enableMainPanel(false);
	}

	@ProxyEvent
	@Override
	public void onActionCompleted(ActionCompletedEvent event) {
		// 
		getView().showProgressBar(false);
		// getView().enableMainPanel(true);
	}

	@Override
	public void showSettingsScreen() {
		History.newItem(NameTokens.settings);		
	}

	@Override
	public void showFrmPassword() {
		History.newItem(NameTokens.password);		
	}

	@Override
	public void showFrmStudents() {
		History.newItem(NameTokens.students);		
	}

	@Override
	public void showFrmCreateStudents() {
		History.newItem(NameTokens.studentadd);
	}

	@Override
	public void showBrancheList() {
		History.newItem(NameTokens.branchelist);
	}

	@Override
	public void showBrancheAdd() {
		History.newItem(NameTokens.brancheadd);		
	}

	@Override
	public void showFrmSubjectList() {
		History.newItem(NameTokens.subjectlist);		
	}

	@Override
	public void showFrmSubjectAdd() {
		History.newItem(NameTokens.subjectadd);		
	}

	@Override
	public void showClasseList() {
		History.newItem(NameTokens.classlist);	
	}

	@Override
	public void showClasseAdd() {
		History.newItem(NameTokens.classeadd);	
	}

	@Override
	public void showProfileManagement() {
		History.newItem(NameTokens.profilemgt);		
	}

	@Override
	public void showCreateBulletins() {
		History.newItem(NameTokens.bulletincreation);
	}

	@Override
	public void showFrmMarkInput() {
		History.newItem(NameTokens.markinput);
	}

	@Override
	public void showFrmBulletinViewDetail() {
		History.newItem(NameTokens.bulletindetail);
	}

	@Override
	public void showFrmBulletinManagement() {
		History.newItem(NameTokens.bulletinmanagement);
	}
		
//	/*
//	 * */
//	@ProxyEvent
//	@Override
//	public void onPageAfterSelect(PageAfterSelectEvent event) {
//		//
//		getView().showCurrentPageOnMenu( event.getTokenName() );
//	}

	/*
	 * */
	@ProxyEvent
	@Override
	public void onDrawSchoolInterface(DrawSchoolInterfaceEvent event) {
		//
		getView().drawSchoolInterface(event.getSchoolCode());
	}
}
