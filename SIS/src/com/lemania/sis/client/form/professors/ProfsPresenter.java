package com.lemania.sis.client.form.professors;

import java.util.List;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.LoggedInGatekeeper;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.CoursProxy;
import com.lemania.sis.shared.EcoleProxy;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.SubjectProxy;
import com.lemania.sis.shared.assignment.AssignmentProxy;
import com.lemania.sis.shared.assignment.AssignmentRequestFactory;
import com.lemania.sis.shared.assignment.AssignmentRequestFactory.AssignmentRequestContext;
import com.lemania.sis.shared.service.ClasseRequestFactory;
import com.lemania.sis.shared.service.CoursRequestFactory;
import com.lemania.sis.shared.service.EcoleRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ProfessorRequestFactory;
import com.lemania.sis.shared.service.SubjectRequestFactory;
import com.lemania.sis.shared.service.ClasseRequestFactory.ClasseRequestContext;
import com.lemania.sis.shared.service.CoursRequestFactory.CoursRequestContext;
import com.lemania.sis.shared.service.EcoleRequestFactory.EcoleRequestContext;
import com.lemania.sis.shared.service.ProfessorRequestFactory.ProfessorRequestContext;
import com.lemania.sis.shared.service.SubjectRequestFactory.SubjectRequestContext;

public class ProfsPresenter 
	extends Presenter<ProfsPresenter.MyView, ProfsPresenter.MyProxy> 
	implements ProfessorListUiHandler, LoginAuthenticatedHandler {
	
	
	// keep the current logged in user
	private CurrentUser currentUser;

	
	public interface MyView extends View, HasUiHandlers<ProfessorListUiHandler> {
		//
		void initializeTable();
		void initializeAssignmentTable();
		
		void setEcoleList(List<EcoleProxy> ecoleList);
		void setCoursList(List<CoursProxy> coursList);
		void setClasseList(List<ClasseProxy> classeList);
		void setSubjectList(List<SubjectProxy> subjectList);
		
		void setData(List<ProfessorProxy> profs);
		void setAssignmentTableData(List<AssignmentProxy> assignments);
		
		void refreshTable(ProfessorProxy prof);
		
		void resetForm();
		
		void showAddedAssignment(AssignmentProxy assignment);
		void showUpdatedAssignment(AssignmentProxy assignment);
	}

	
	@ProxyCodeSplit
	@NameToken(NameTokens.profs)
	@UseGatekeeper( AdminGateKeeper.class )
	public interface MyProxy extends ProxyPlace<ProfsPresenter> {
	}

	
	@Inject
	public ProfsPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
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
		getView().initializeTable();
		getView().initializeAssignmentTable();
	}
	
	
	@Override
	protected void onReset(){
		//
		super.onReset();
		
		// Thuan
		getView().resetForm();
		
		getProfessorsList();
		loadEcoleList();
		loadActiveSubjectList();
	}
	
	
	/*
	 * Load list of schools when the form is opened
	 * */
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


	/*
	 * 
	 * */
	private void getProfessorsList() {
		//
		ProfessorRequestFactory rf = GWT.create(ProfessorRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ProfessorRequestContext rc = rf.professorRequest();
		rc.listAll().fire(new Receiver<List<ProfessorProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<ProfessorProxy> response) {
				getView().setData(response);
			}
		});
	}

	
	/*
	 * 
	 * */
	@Override
	public void updateProfessorStatus(ProfessorProxy prof, Boolean status) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		if (!currentUser.isAdmin()) {
			Window.alert("Veuillez vous connecter avec le code d'accès de l'administrateur. La modification n'a pas été effectuée.");
			return;
		}
		
		//
		ProfessorRequestFactory rf = GWT.create(ProfessorRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ProfessorRequestContext rc = rf.professorRequest();
		ProfessorProxy profForUpdate = rc.edit(prof);
		profForUpdate.setProfActive(status);
		rc.saveAndReturn(profForUpdate).fire(new Receiver<ProfessorProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(ProfessorProxy response) {
				getView().refreshTable(response);
			}
		});	
	}


	/*
	 * 
	 * */
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		currentUser = event.getCurrentUser();
	}

	
	/*
	 * 
	 * */
	@Override
	public void updateProfessorName(ProfessorProxy prof, String name) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		if (prof.getProfName().equals(name))
			return;
		
		if (!currentUser.isAdmin()) {
			Window.alert("Veuillez vous connecter avec le code d'accès de l'administrateur. La modification n'a pas été effectuée.");
			getView().refreshTable(prof);
			return;
		}
		
		ProfessorRequestFactory rf = GWT.create(ProfessorRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ProfessorRequestContext rc = rf.professorRequest();
		ProfessorProxy profForUpdate = rc.edit(prof);
		profForUpdate.setProfName(name);
		rc.saveAndReturn(profForUpdate).fire(new Receiver<ProfessorProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(ProfessorProxy response) {
				getView().refreshTable(response);
			}
		});
	}


	@Override
	public void professorSelected(ProfessorProxy prof) {
		//
		AssignmentRequestFactory rf = GWT.create(AssignmentRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AssignmentRequestContext rc = rf.assignmentRequest();
		rc.listAll( prof.getId().toString() ).fire(new Receiver<List<AssignmentProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<AssignmentProxy> response) {
				getView().setAssignmentTableData(response);
			}
		});
	}


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


	/*
	 * Load class list when a program is selected
	 * */
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
	

	/*
	 * Save assignment
	 * */
	@Override
	public void saveAssignment(String professorId, String classId, String subjectId, Boolean isActive) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		AssignmentRequestFactory rf = GWT.create(AssignmentRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AssignmentRequestContext rc = rf.assignmentRequest();
		rc.saveAndReturn( professorId.toString(), classId.toString(), subjectId.toString(), isActive ).fire(new Receiver<AssignmentProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(AssignmentProxy response) {
				getView().showAddedAssignment(response);
			}
		});
	}


	/*
	 * 
	 * */
	@Override
	public void updateAssignmentStatus(AssignmentProxy assignment, Boolean value) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		// 
		AssignmentRequestFactory rf = GWT.create(AssignmentRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AssignmentRequestContext rc = rf.assignmentRequest();
		
		AssignmentProxy a4update = rc.edit( assignment );
		a4update.setActive(value);
		rc.saveAndReturn( a4update ).fire(new Receiver<AssignmentProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(AssignmentProxy response) {
				getView().showUpdatedAssignment(response);
			}
		});
	}


	/*
	 * Load active subject list when a class is selected 
	 * */
	@Override
	public void onClassSelected() {
		//
	}


	/*
	 * 
	 * */
	private void loadActiveSubjectList() {
		//
		SubjectRequestFactory rf = GWT.create(SubjectRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		SubjectRequestContext rc = rf.subjectRequest();
		rc.listAllActive().fire(new Receiver<List<SubjectProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<SubjectProxy> response) {
				getView().setSubjectList(response);
			}
		});
	}
}
