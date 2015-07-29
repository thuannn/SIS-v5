package com.lemania.sis.client.form.markinput;

import java.util.List;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.lemania.sis.client.event.CheckDeadlineEvent;
import com.lemania.sis.client.event.CheckDeadlineEvent.CheckDeadlineHandler;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.client.values.NotificationValues;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.ProfessorGateKeeper;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.SettingOptionProxy;
import com.lemania.sis.shared.assignment.AssignmentProxy;
import com.lemania.sis.shared.assignment.AssignmentRequestFactory;
import com.lemania.sis.shared.assignment.AssignmentRequestFactory.AssignmentRequestContext;
import com.lemania.sis.shared.bulletinbranche.BulletinBrancheProxy;
import com.lemania.sis.shared.bulletinbranche.BulletinBrancheRequestFactory;
import com.lemania.sis.shared.bulletinbranche.BulletinBrancheRequestFactory.BulletinBrancheRequestContext;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectRequestFactory;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectRequestFactory.BulletinSubjectRequestContext;
import com.lemania.sis.shared.service.SettingOptionRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ProfessorRequestFactory;
import com.lemania.sis.shared.service.ProfessorRequestFactory.ProfessorRequestContext;
import com.lemania.sis.shared.service.SettingOptionRequestFactory.SettingOptionRequestContext;

public class FrmMarkInputPresenter extends
		Presenter<FrmMarkInputPresenter.MyView, FrmMarkInputPresenter.MyProxy> 
		implements 
		FrmMarkInputUiHandler, LoginAuthenticatedHandler, CheckDeadlineHandler {
	
	// Thuan
	private CurrentUser currentUser;
	
	
	public interface MyView extends View, HasUiHandlers<FrmMarkInputUiHandler> {
		//
		void resetForm();
		void initializeUi();
		void modifyUiByProgramme();
		//
		void setProfListData(List<ProfessorProxy> profs);
		//
		void setAssignmentTableData(List<AssignmentProxy> assignments);
		//
		void setBulletinSubjectTableData(List<BulletinSubjectProxy> bulletinSubjects);
		//
		void setBulletinBrancheTableData(List<BulletinBrancheProxy> bulletinBranches);
		//
		void showCurrentNotes();
		void showCurrentRemarques();
		//
		void showUpdatedBulletinDetails(BulletinBrancheProxy bulletinBranche, BulletinSubjectProxy bulletinSubject);
		//
		void modifyUiByDeadline(List<SettingOptionProxy> settings, CurrentUser currentUser);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.markinput)
	@UseGatekeeper( ProfessorGateKeeper.class )
	public interface MyProxy extends ProxyPlace<FrmMarkInputPresenter> {
	}

	
	/**/
	@Inject
	public FrmMarkInputPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	
	/**/
	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
	}

	
	/**/
	@Override
	protected void onBind() {
		super.onBind();
		
		// Thuan
		getView().setUiHandlers(this);
		getView().initializeUi();
	}
	
	
	/**/
	@Override
	protected void onReset() {
		//
		super.onReset();
		// Thuan
		getView().resetForm();
		loadProfessorList();
	}

	
	/*
	 * Load professor list, if the current user is a professor, show only him/her.
	 * If current user is admin, show all the professors. */
	public void loadProfessorList(){
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
	 * Get the current user info when user is logged in */
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		this.currentUser = event.getCurrentUser();
	}
	

	/**/
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
		BulletinSubjectRequestFactory rf = GWT.create(BulletinSubjectRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinSubjectRequestContext rc = rf.bulletinSubjectRequest();
		rc.listAllByAssignment( assignmentId ).fire(new Receiver<List<BulletinSubjectProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<BulletinSubjectProxy> response) {				
				//
				getView().setBulletinSubjectTableData(response);
				getView().modifyUiByProgramme();				
				//
				getEventBus().fireEvent(new CheckDeadlineEvent());
			}
		});
	}

	
	/*
	 * 
	 * */
	protected void checkDeadLine() {
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
				getView().modifyUiByDeadline(response, currentUser);
			}
		});
	}


	/*
	 * 
	 * */
	@Override
	public void onBulletinSubjectSelected(BulletinSubjectProxy bulletinSubject) {
		BulletinBrancheRequestFactory rf = GWT.create(BulletinBrancheRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinBrancheRequestContext rc = rf.bulletinBrancheRequest();
		rc.listAll( bulletinSubject.getId().toString() ).fire(new Receiver<List<BulletinBrancheProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<BulletinBrancheProxy> response) {
				getView().setBulletinBrancheTableData(response);
				getView().showCurrentRemarques();
			}
		});
	}


	/*
	 * */
	@Override
	public void onBulletinBrancheSelected(BulletinBrancheProxy bulletinBranche) {
		//
		if (bulletinBranche != null)
			getView().showCurrentNotes();
	}


	/*
	 * */
	@Override
	public void saveNotes( BulletinBrancheProxy bulletinBranche, final BulletinSubjectProxy bulletinSubject, 
			String t_1_1, String t_1_2,	String t_1_3, String t_1_4, String t_1_5, 
			String t_2_1, String t_2_2, String t_2_3, String t_2_4, String t_2_5,
			String t_3_1, String t_3_2, String t_3_3, String t_3_4,	String t_3_5, 
			String t_4_1, String t_4_2, String t_4_3, String t_4_4,	String t_4_5, 
			final String remarque1, final String remarque2, final String remarque3, final String remarque4 ) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		if (bulletinSubject == null){
			Window.alert( NotificationValues.invalid_input + " - Merci de choisir l'élève à saisir les notes.");
			return;
		}
		//
		if (bulletinBranche == null){
			Window.alert( NotificationValues.invalid_input + " - Merci de choisir une branche à choisir les notes.");
			return;
		}
		// 
		BulletinBrancheRequestFactory rfBranche = GWT.create(BulletinBrancheRequestFactory.class);
		rfBranche.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinBrancheRequestContext rcBranche = rfBranche.bulletinBrancheRequest();
		//
		bulletinBranche = rcBranche.edit(bulletinBranche);
		bulletinBranche.setT1_1(t_1_1);
		bulletinBranche.setT1_2(t_1_2);
		bulletinBranche.setT1_3(t_1_3);
		bulletinBranche.setT1_4(t_1_4);
		bulletinBranche.setT1_5(t_1_5);
		bulletinBranche.setT1( calculateAverage(t_1_1, t_1_2, t_1_3, t_1_4, t_1_5));
		//
		bulletinBranche.setT2_1(t_2_1);
		bulletinBranche.setT2_2(t_2_2);
		bulletinBranche.setT2_3(t_2_3);
		bulletinBranche.setT2_4(t_2_4);
		bulletinBranche.setT2_5(t_2_5);
		bulletinBranche.setT2( calculateAverage(t_2_1, t_2_2, t_2_3, t_2_4, t_2_5));
		//
		bulletinBranche.setT3_1(t_3_1);
		bulletinBranche.setT3_2(t_3_2);
		bulletinBranche.setT3_3(t_3_3);
		bulletinBranche.setT3_4(t_3_4);
		bulletinBranche.setT3_5(t_3_5);
		bulletinBranche.setT3( calculateAverage(t_3_1, t_3_2, t_3_3, t_3_4, t_3_5));
		//
		bulletinBranche.setT4_1(t_4_1);
		bulletinBranche.setT4_2(t_4_2);
		bulletinBranche.setT4_3(t_4_3);
		bulletinBranche.setT4_4(t_4_4);
		bulletinBranche.setT4_5(t_4_5);
		bulletinBranche.setT4( calculateAverage(t_4_1, t_4_2, t_4_3, t_4_4, t_4_5));
		//
		rcBranche.saveAndReturn(bulletinBranche).fire(new Receiver<BulletinBrancheProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(BulletinBrancheProxy response) {
				saveBulletinSubject(response, bulletinSubject, remarque1, remarque2, remarque3, remarque4);
			}
		});
	}

	/*
	 * */
	private String calculateAverage(String t1, String t2, String t3, String t4, String t5) {
		//
		Double average = 0.0;
		Double total = 0.0;
		Integer count = 0;
		if (!t1.isEmpty()) { total = total + Double.parseDouble(t1); count++; }
		if (!t2.isEmpty()) { total = total + Double.parseDouble(t2); count++; }
		if (!t3.isEmpty()) { total = total + Double.parseDouble(t3); count++; }
		if (!t4.isEmpty()) { total = total + Double.parseDouble(t4); count++; }
		if (!t5.isEmpty()) { total = total + Double.parseDouble(t5); count++; }
		if (count>0) {
			average = ((double)Math.round(total / count * 10)) / 10;
			return Double.toString(average);
		}
		return "";
	}


	/*
	 * */
	public void saveBulletinSubject(final BulletinBrancheProxy bulletinBranche, BulletinSubjectProxy bulletinSubject, 
			String remarque1, String remarque2, String remarque3, String remarque4) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		BulletinSubjectRequestFactory rfSubject = GWT.create(BulletinSubjectRequestFactory.class);
		rfSubject.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinSubjectRequestContext rcSubject = rfSubject.bulletinSubjectRequest();
		//
		bulletinSubject = rcSubject.edit(bulletinSubject);
		bulletinSubject.setRemarqueT1(remarque1);
		bulletinSubject.setRemarqueT2(remarque2);
		bulletinSubject.setRemarqueT3(remarque3);
		bulletinSubject.setRemarqueT4(remarque4);
		//
		rcSubject.saveAndReturn(bulletinSubject).fire(new Receiver<BulletinSubjectProxy>() {
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(BulletinSubjectProxy response) {
				getView().showUpdatedBulletinDetails(bulletinBranche, response);
			}
		});
	}

	
	@ProxyEvent
	@Override
	public void onCheckDeadline(CheckDeadlineEvent event) {
		// 
		checkDeadLine();
	}
}
