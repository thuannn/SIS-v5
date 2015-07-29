package com.lemania.sis.client.form.bulletincreation;

import java.util.List;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.lemania.sis.client.event.LoadNonAttribuedStudentEvent;
import com.lemania.sis.client.event.LoadNonAttribuedStudentEvent.LoadNonAttribuedStudentHandler;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
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
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.CoursProxy;
import com.lemania.sis.shared.EcoleProxy;
import com.lemania.sis.shared.ProfileProxy;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory.BulletinRequestContext;
import com.lemania.sis.shared.service.ClasseRequestFactory;
import com.lemania.sis.shared.service.CoursRequestFactory;
import com.lemania.sis.shared.service.EcoleRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ProfileRequestFactory;
import com.lemania.sis.shared.service.ClasseRequestFactory.ClasseRequestContext;
import com.lemania.sis.shared.service.CoursRequestFactory.CoursRequestContext;
import com.lemania.sis.shared.service.EcoleRequestFactory.EcoleRequestContext;
import com.lemania.sis.shared.service.ProfileRequestFactory.ProfileRequestContext;
import com.lemania.sis.shared.student.StudentProxy;
import com.lemania.sis.shared.student.StudentRequestFactory;
import com.lemania.sis.shared.student.StudentRequestFactory.StudentRequestContext;

public class FrmBulletinCreationPresenter
		extends
		Presenter<FrmBulletinCreationPresenter.MyView, FrmBulletinCreationPresenter.MyProxy> 
		implements 
		FrmBulletinCreationUiHandler, LoginAuthenticatedHandler, LoadNonAttribuedStudentHandler {
	
	//
	private CurrentUser currentUser;
	

	public interface MyView extends View, HasUiHandlers<FrmBulletinCreationUiHandler> {
		//
		void resetForm();
		//
		void initializeTables();
		//
		void setStudentTableData(List<StudentProxy> students);
		void setBulletinTableData(List<BulletinProxy> bulletins);
		//
		void setProfileListData(List<ProfileProxy> profiles);
		void setEcoleList(List<EcoleProxy> ecoles);
		void setCoursList(List<CoursProxy> programmes);
		void setClasseList(List<ClasseProxy> classes);
		//
		void addNewBulletinToTable(BulletinProxy bulletin);
		//
		void removeStudentWithBulletin();
		//
		void removeDeletedBulletinFromTable();
		void refreshBulletinTable( BulletinProxy bp );
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.bulletincreation)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<FrmBulletinCreationPresenter> {
	}

	@Inject
	public FrmBulletinCreationPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
	}

	/*
	 * */
	@Override
	protected void onBind() {
		super.onBind();
		
		// Thuan
		getView().setUiHandlers(this);
		getView().initializeTables();
	}
	
	/*
	 * */
	@Override
	protected void onReset() {
		//
		super.onReset();
		// Thuan
		getView().resetForm();
		//
		loadActiveStudentList();
		//
		loadEcoleList();
	}
	
	
	private void loadEcoleList() {
		// 
		EcoleRequestFactory rf = GWT.create(EcoleRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		EcoleRequestContext rc = rf.ecoleRequest();
		rc.listAll().fire(new Receiver<List<EcoleProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<EcoleProxy> response) {
				getView().setEcoleList(response);
			}
		});
	}

	/**/
	private void loadActiveProfileList(String classId) {
		//
		ProfileRequestFactory rf = GWT.create(ProfileRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		
		ProfileRequestContext rc = rf.profileRequest();
		rc.listAllActiveByClass(classId).fire(new Receiver<List<ProfileProxy>>(){
			@Override
			public void onSuccess(List<ProfileProxy> response){
				getView().setProfileListData( response );
			}
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
		});
		//		
	}
	

	/*
	 * Load active student list
	 * */
	public void loadActiveStudentList(){
		StudentRequestContext rc = getStudentRequestContext();
		rc.listAllActiveWithoutBulletin().fire(new Receiver<List<StudentProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<StudentProxy> response) {
				getView().setStudentTableData(response);
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
	

	/**/
	@Override
	public void onEcoleSelected(String ecoleId) {
		//
		if (ecoleId.isEmpty()){
			return;
		}
		
		CoursRequestFactory rf = GWT.create(CoursRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		CoursRequestContext rc = rf.coursRequest();
		rc.listAllActive(ecoleId).fire(new Receiver<List<CoursProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<CoursProxy> response) {
				getView().setCoursList(response);
			}
		});
	}

	
	/**/
	@Override
	public void onProgrammeSelected(String coursId) {
		//
		ClasseRequestFactory rf = GWT.create(ClasseRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ClasseRequestContext rc = rf.classeRequest();
		rc.listAllActive(coursId).fire(new Receiver<List<ClasseProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<ClasseProxy> response) {
				getView().setClasseList(response);
			}
		});
	}

	
	/**/
	@Override
	public void createBulletin(String studentId, String classId, String year, String profileId) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		if (studentId.isEmpty()){
			Window.alert( NotificationValues.invalid_input + " - Elève");
			return;
		}
		if (classId.isEmpty()){
			Window.alert( NotificationValues.invalid_input + " - Classe");
			return;
		}
		if (year.isEmpty()){
			Window.alert( NotificationValues.invalid_input + " - Année");
			return;
		}
		if (profileId.isEmpty()){
			Window.alert( NotificationValues.invalid_input + " - Profil");
			return;
		}
		//
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		rc.createBulletin(studentId, classId, year, profileId).fire(new Receiver<BulletinProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(BulletinProxy response) {
				getView().addNewBulletinToTable(response);
				getView().removeStudentWithBulletin();
			}
		});
	}

	
	/**/
	@Override
	public void onClassChanged(final String classId) {
		//
		if (classId.isEmpty()){
			Window.alert(NotificationValues.invalid_input + " - Classe");
			return;
		}
		// 
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		rc.listAllByClass( classId ).fire(new Receiver<List<BulletinProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<BulletinProxy> response) {
				getView().setBulletinTableData(response);
				loadActiveProfileList(classId);
			}
		});
	}

	//
	@Override
	public void removeBulletin(BulletinProxy bp) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		rc.removeBulletin(bp).fire(new Receiver<Boolean>() {
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(Boolean response) {
				if (response) {
					getView().removeDeletedBulletinFromTable();
					getEventBus().fireEvent( new LoadNonAttribuedStudentEvent() );
				} else
					Window.alert("Une erreur s'est produite.");
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
	public void updateBulletinFinishedStatus(BulletinProxy bp,
			Boolean isFinished) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		BulletinProxy editBP = rc.edit(bp);
		editBP.setIsFinished(isFinished);
		rc.saveAndReturn(editBP).fire(new Receiver<BulletinProxy>() {
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(BulletinProxy response) {
					getEventBus().fireEvent( new LoadNonAttribuedStudentEvent() );
					getView().refreshBulletinTable(response);
			}
		});	
	}
	

	/*
	 * */
	@ProxyEvent
	@Override
	public void onLoadNonAttribuedStudent(LoadNonAttribuedStudentEvent event) {
		//
		loadActiveStudentList();
	}

	
	/*
	 * */
	@Override
	public void onYearChanged(String year) {
		// TODO Auto-generated method stub
		
	}
}
