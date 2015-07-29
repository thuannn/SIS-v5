package com.lemania.sis.client.form.evaluations;

import java.util.List;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.client.values.NotificationValues;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.ProfessorGateKeeper;
import com.lemania.sis.shared.EvaluationHeaderProxy;
import com.lemania.sis.shared.EvaluationSubjectProxy;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.assignment.AssignmentProxy;
import com.lemania.sis.shared.assignment.AssignmentRequestFactory;
import com.lemania.sis.shared.assignment.AssignmentRequestFactory.AssignmentRequestContext;
import com.lemania.sis.shared.service.EvaluationHeaderRequestFactory;
import com.lemania.sis.shared.service.EvaluationSubjectRequestFactory;
import com.lemania.sis.shared.service.EvaluationSubjectRequestFactory.EvaluationSubjectRequestContext;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ProfessorRequestFactory;
import com.lemania.sis.shared.service.EvaluationHeaderRequestFactory.EvaluationHeaderRequestContext;
import com.lemania.sis.shared.service.ProfessorRequestFactory.ProfessorRequestContext;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class FrmEvaluationInputPresenter
		extends Presenter<FrmEvaluationInputPresenter.MyView, FrmEvaluationInputPresenter.MyProxy> 
		implements FrmEvaluationInputUiHandler, LoginAuthenticatedHandler {
	
	// Thuan
	private CurrentUser currentUser;

	/*
	 * */
	public interface MyView extends View, HasUiHandlers<FrmEvaluationInputUiHandler> {
		//
		void resetForm();
		//
		void setProfListData(List<ProfessorProxy> profs);
		//
		void setAssignmentTableData(List<AssignmentProxy> assignments);
		//
		void setEvaluationHeaderListData(List<EvaluationHeaderProxy> headers);
		//
		void setEvaluationSubjectTableData( List<EvaluationSubjectProxy> evaluationSubjectList );
		//
		void initializeUI();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.evaluationinput)
	@UseGatekeeper( ProfessorGateKeeper.class )
	public interface MyProxy extends ProxyPlace<FrmEvaluationInputPresenter> {		
	}

	/*
	 * */
	@Inject
	public FrmEvaluationInputPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
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
		super.onBind();
		
		//
		getView().setUiHandlers(this);
		getView().initializeUI();
	}

	/*
	 * */
	@Override
	protected void onReset() {
		super.onReset();
		// Thuan
		getView().resetForm();
		loadProfessorList();
	}

	/*
	 * */
	private void loadProfessorList() {		
		//
		ProfessorRequestFactory rf = GWT.create(ProfessorRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ProfessorRequestContext rc = rf.professorRequest();
		if (currentUser.isProf()){
			rc.getByEmail(currentUser.getUserEmail()).fire(new Receiver<List<ProfessorProxy>>(){
				@Override
				public void onFailure(ServerFailure error){
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess(List<ProfessorProxy> response) {
					getView().setProfListData(response);
				}
			});
		}
		if (currentUser.isAdmin()){
			rc.listAll().fire(new Receiver<List<ProfessorProxy>>(){
				@Override
				public void onFailure(ServerFailure error){
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess(List<ProfessorProxy> response) {
					getView().setProfListData(response);
				}
			});
		}
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
	public void onProfessorSelected(String profId) {
		//
		if (profId.isEmpty()){
			getView().resetForm();
			return;
		}
		//
		AssignmentRequestFactory rf = GWT.create(AssignmentRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AssignmentRequestContext rc = rf.assignmentRequest();
		rc.listAllActive( profId ).fire(new Receiver<List<AssignmentProxy>>(){
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

	/*
	 * */
	@Override
	public void onAssignmentSelected(String assignmentId) {		
		//
		EvaluationHeaderRequestFactory rf = GWT.create(EvaluationHeaderRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		EvaluationHeaderRequestContext rc = rf.evaluationHeaderRequest();				
		rc.listAllByAssignment(assignmentId).fire(new Receiver<List<EvaluationHeaderProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<EvaluationHeaderProxy> response) {
				getView().setEvaluationHeaderListData(response);
			}
		});
	}

	/*
	 * */
	@Override
	public void onLstEvaluationHeaderSelected(String profId,
			String assignmentId, String evaluationHeaderId) {
		//
		populateEvaluationSubjectList(profId, assignmentId, evaluationHeaderId);
	}

	/*	
	 * Populate the list of Evaluation Subject for each student in the assignment list 
	 * */
	private void populateEvaluationSubjectList( String profId,
			String assignmentId, String evaluationHeaderId ) {		
		//
		if (currentUser.isReadOnly()){
			Window.alert( NotificationValues.readOnly );
			return;
		}
		//
		EvaluationSubjectRequestFactory rf = GWT.create(EvaluationSubjectRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		EvaluationSubjectRequestContext rc = rf.evaluationSubjectRequest();				
		rc.populateEvaluationSubjects(profId, assignmentId, evaluationHeaderId).fire(new Receiver<List<EvaluationSubjectProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<EvaluationSubjectProxy> response) {
				getView().setEvaluationSubjectTableData(response);
			}
		});
	}

	//
	@Override
	public void updateEvaluation(EvaluationSubjectProxy es, String value, int order) {
		//
		if (currentUser.isReadOnly()){
			Window.alert( NotificationValues.readOnly );
			return;
		}
		//
		EvaluationSubjectRequestFactory rf = GWT.create(EvaluationSubjectRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		EvaluationSubjectRequestContext rc = rf.evaluationSubjectRequest();
		EvaluationSubjectProxy esEdit = rc.edit(es);
		switch (order) {
			case 1 : 
				esEdit.setObjective1(value);
				break;
			case 2 :
				esEdit.setObjective2(value);
				break;
			case 3 : 
				esEdit.setObjective3(value);
				break;
			case 4 :
				esEdit.setObjective4(value);
				break;
			case 5 : 
				esEdit.setObjective5(value);
				break;
			case 6 : 
				esEdit.setObjective6(value);
				break;
		}
		//
		rc.save(esEdit).fire(new Receiver<Void>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(Void response) {
				//
			}
		});		
	}
}
