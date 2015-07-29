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
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.CoursProxy;
import com.lemania.sis.shared.EcoleProxy;
import com.lemania.sis.shared.EvaluationHeaderProxy;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.service.ClasseRequestFactory;
import com.lemania.sis.shared.service.CoursRequestFactory;
import com.lemania.sis.shared.service.EcoleRequestFactory;
import com.lemania.sis.shared.service.EvaluationHeaderRequestFactory;
import com.lemania.sis.shared.service.EvaluationHeaderRequestFactory.EvaluationHeaderRequestContext;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ProfessorRequestFactory;
import com.lemania.sis.shared.service.ClasseRequestFactory.ClasseRequestContext;
import com.lemania.sis.shared.service.CoursRequestFactory.CoursRequestContext;
import com.lemania.sis.shared.service.EcoleRequestFactory.EcoleRequestContext;
import com.lemania.sis.shared.service.ProfessorRequestFactory.ProfessorRequestContext;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class FrmEvaluationReportListPresenter
		extends Presenter<FrmEvaluationReportListPresenter.MyView, FrmEvaluationReportListPresenter.MyProxy>
		implements FrmEvaluationReportListUiHandler, LoginAuthenticatedHandler {
	
	//
	private CurrentUser currentUser;

	public interface MyView extends View, HasUiHandlers<FrmEvaluationReportListUiHandler> {
		//
		public void initializeUI();
		public void resetEditForm();
		public void resetForm();
		//
		void setEcoleList(List<EcoleProxy> ecoles);
		//
		void setCoursList(List<CoursProxy> programmes);
		//
		void setClasseList(List<ClasseProxy> classes);
		//
		void setProfListData(List<ProfessorProxy> profs);
		//
		void addNewEvaluationHeaderToList(EvaluationHeaderProxy eh);
		//
		void setEvaluationHeaderListData(List<EvaluationHeaderProxy> eHs);
		//
		void updateEvaluationHeader(EvaluationHeaderProxy eh);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.evaluationlist)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<FrmEvaluationReportListPresenter> {
	}

	@Inject
	public FrmEvaluationReportListPresenter(final EventBus eventBus,
			final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		//
		getView().setUiHandlers(this);
		// UI
		getView().initializeUI();
	}

	/*
	 * */
	@Override
	protected void onReset() {
		super.onReset();
		//
		getView().resetForm();
		getView().resetEditForm();
		loadEcoleList();
		loadClassMasterList();
	}

	/*
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
	 * */
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
	 * */
	@Override
	public void onProgrammeSelected(String programmeId) {		
		//
		ClasseRequestFactory rf = GWT.create(ClasseRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ClasseRequestContext rc = rf.classeRequest();
		rc.listAllActive(programmeId).fire(new Receiver<List<ClasseProxy>>(){
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
	 * */
	@Override
	public void loadClassMasterList() {
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
				getView().setProfListData(response);
			}
		});
	}

	/*
	 * */
	@Override
	public void createNewReport(String fromDate, String toDate,
			String objective, String schoolYear, String classId,
			String classMasterId) {
		//
		if (currentUser.isReadOnly()){
			Window.alert( NotificationValues.readOnly );
			return;
		}
		//
		if (classMasterId.isEmpty()){
			Window.alert(NotificationValues.invalid_input + " - Maître de la classe");
			return;
		}
		if (fromDate.isEmpty() || toDate.isEmpty()){
			Window.alert(NotificationValues.invalid_input + " - Les dates");
			return;
		}
		//
		EvaluationHeaderRequestFactory rf = GWT.create(EvaluationHeaderRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		EvaluationHeaderRequestContext rc = rf.evaluationHeaderRequest();				
		rc.saveAndReturn(fromDate, toDate, objective, schoolYear, classMasterId, classId).fire(new Receiver<EvaluationHeaderProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(EvaluationHeaderProxy response) {
				getView().addNewEvaluationHeaderToList(response);
			}
		});
	}

	/*
	 * */
	@Override
	public void onClassSelected(String classId) {
		//
		EvaluationHeaderRequestFactory rf = GWT.create(EvaluationHeaderRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		EvaluationHeaderRequestContext rc = rf.evaluationHeaderRequest();				
		rc.listAllByClass(classId).fire(new Receiver<List<EvaluationHeaderProxy>>(){
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
	public void updateReport(EvaluationHeaderProxy evaluationHeader,
			String dateFrom, String dateTo, String classMasterId,
			String objective) {		
		//
		if (currentUser.isReadOnly()){
			Window.alert( NotificationValues.readOnly );
			return;
		}
		//
		EvaluationHeaderRequestFactory rf = GWT.create(EvaluationHeaderRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		EvaluationHeaderRequestContext rc = rf.evaluationHeaderRequest();
		EvaluationHeaderProxy ehUpdate = rc.edit(evaluationHeader);
		rc.updateEvaluationHeader(ehUpdate, dateFrom, dateTo, classMasterId, objective).fire(new Receiver<EvaluationHeaderProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(EvaluationHeaderProxy response) {
				//
				getView().updateEvaluationHeader(response);
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
}
