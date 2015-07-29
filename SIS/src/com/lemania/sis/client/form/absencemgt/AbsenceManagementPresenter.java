package com.lemania.sis.client.form.absencemgt;

import java.util.List;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.client.popup.absenceinput.AbsenceInputPresenter;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.AdminGateKeeper;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.lemania.sis.client.event.AbsenceAfterInputEvent;
import com.lemania.sis.client.event.AbsenceAfterInputEvent.AbsenceAfterInputHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.shared.absenceitem.AbsenceItemProxy;
import com.lemania.sis.shared.absenceitem.AbsenceItemRequestFactory;
import com.lemania.sis.shared.absenceitem.AbsenceItemRequestFactory.AbsenceItemRequestContext;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory.BulletinRequestContext;
import com.lemania.sis.shared.parent.ParentProxy;
import com.lemania.sis.shared.parent.ParentRequestFactory;
import com.lemania.sis.shared.parent.ParentRequestFactory.ParentRequestContext;
import com.lemania.sis.shared.service.ContactRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ContactRequestFactory.ContactRequestContext;
import com.lemania.sis.shared.student.StudentRequestFactory;
import com.lemania.sis.shared.student.StudentRequestFactory.StudentRequestContext;

public class AbsenceManagementPresenter
		extends Presenter<AbsenceManagementPresenter.MyView, AbsenceManagementPresenter.MyProxy>
		implements AbsenceManagementUiHandlers, AbsenceAfterInputHandler {

	public interface MyView extends View, HasUiHandlers<AbsenceManagementUiHandlers> {
		//
		public void setStudentTableData( List<AbsenceItemProxy> aip );
		//
		public void setStudentSuggestboxData( List<BulletinProxy> bulleetins );
		//
		public void setAbsenceItemTableData( List<AbsenceItemProxy> absenceItems );
		//
//		void setMotifListData(List<MotifAbsenceProxy> motifs);
		//
		void initializeUI();
		//
		void resetUI();
		//
		void setUpdatedAbsenceItem( AbsenceItemProxy aip );
		//
		void removeDeletedAbsenceItem();
		//
		void setParentData( List<ParentProxy> parents );
		//
		void showNotificationDatesEmail( AbsenceItemProxy ai );
		void showNotificationDatesSMS( AbsenceItemProxy ai );
	}

	//
	private AbsenceInputPresenter popupAbsenceInput;
	//
	String curStudentId;
	String fromDate = "", toDate = "";
	
	
	@ProxyCodeSplit
	@NameToken(NameTokens.absencesmgt)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<AbsenceManagementPresenter> {
	}
	

	@Inject
	public AbsenceManagementPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, AbsenceInputPresenter aip) {
		super(eventBus, view, proxy, MainPagePresenter.TYPE_SetMainContent);
		//
		this.popupAbsenceInput = aip;
	}


	@Override
	protected void onBind() {
		super.onBind();
		//
		getView().setUiHandlers(this);
		//
		getView().initializeUI();
	}

	@Override
	protected void onReset() {
		super.onReset();
		//
		loadStudentList();
		//
		getView().resetUI();
		//
//		loadMotifs();
	}
	
	
//	/*
//	 * */
//	private void loadMotifs() {
//		//
//		MotifAbsenceRequestFactory rf = GWT.create(MotifAbsenceRequestFactory.class);
//		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
//		MotifAbsenceRequestContext rc = rf.motifAbsenceRequestContext();
//		rc.listAll().fire(new Receiver<List<MotifAbsenceProxy>>(){
//			@Override
//			public void onFailure(ServerFailure error){
//				Window.alert(error.getMessage());
//			}
//			@Override
//			public void onSuccess(List<MotifAbsenceProxy> response) {
//				getView().setMotifListData(response);
//			}
//		});
//	}
	

	/* 
	 * Load student list when form is opened.
	 * This data is used for the suggest box.
	 * */
	private void loadStudentList() {
		//
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		rc.listAllActive().fire(new Receiver<List<BulletinProxy>>() {
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<BulletinProxy> response) {
				getView().setStudentSuggestboxData( response );
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
	 * When a student is selected, load his absences history
	 * */
	@Override
	public void onStudentSelected( String studentId ) {
		// 
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		rc.listAllByStudent( studentId ).fire(new Receiver<List<AbsenceItemProxy>>(){
				@Override
				public void onFailure(ServerFailure error){
					//
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess( List<AbsenceItemProxy> response ) {
					//
					getView().setAbsenceItemTableData( response );
				}
			});
	}

	
	/*
	 * */
	@Override
	public void updateJustifyStatus(AbsenceItemProxy ai, boolean isJustified) {
		//
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		//
		AbsenceItemProxy updateAI = rc.edit(ai);
		updateAI.setJusttified(isJustified);
		rc.saveAndReturn(updateAI).fire(new Receiver<AbsenceItemProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				//
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( AbsenceItemProxy response ) {
				//
				getView().setUpdatedAbsenceItem( response );
			}
		});
	}

	
	/*
	 * */
	@Override
	public void updateParentNotifiedStatus(AbsenceItemProxy ai, boolean parentNotified) {
		//
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		//
		AbsenceItemProxy updateAI = rc.edit(ai);
		updateAI.setParentNotified(parentNotified);
		rc.saveAndReturn(updateAI).fire(new Receiver<AbsenceItemProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				//
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( AbsenceItemProxy response ) {
				//
				getView().setUpdatedAbsenceItem( response );
			}
		});
	}

	
	/*
	 * */
	@Override
	public void updateAdminComment(AbsenceItemProxy ai, String adminComment) {
		//
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		//
		AbsenceItemProxy updateAI = rc.edit(ai);
		updateAI.setAdminComment(adminComment);
		rc.saveAndReturn(updateAI).fire(new Receiver<AbsenceItemProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				//
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( AbsenceItemProxy response ) {
				//
				getView().setUpdatedAbsenceItem( response );
			}
		});
	}

	
//	/*
//	 * */
//	@Override
//	public void updateMotif(AbsenceItemProxy ai, String motifID) {
//		//
//		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
//		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
//		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
//		//
//		AbsenceItemProxy updateAI = rc.edit(ai);
//		rc.updateMotif(updateAI, motifID).fire(new Receiver<AbsenceItemProxy>(){
//			@Override
//			public void onFailure(ServerFailure error){
//				//
//				Window.alert(error.getMessage());
//			}
//			@Override
//			public void onSuccess( AbsenceItemProxy response ) {
//				//
//				getView().setUpdatedAbsenceItem( response );
//			}
//		});
//	}
	

	/*
	 * CRITICAL - load student absences base on student id and date range
	 * */
	@Override
	public void filterDate(final String studentId, String dateFrom, String dateTo) {
		//
		// Keep the current selected values
		curStudentId = studentId;
		fromDate = dateFrom;
		toDate = dateTo;
		//
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		rc.listAllByStudentAndDate( studentId, dateFrom, dateTo ).fire(new Receiver<List<AbsenceItemProxy>>() {
			@Override
			public void onFailure(ServerFailure error){
				//
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( List<AbsenceItemProxy> response ) {
				//
				getView().setAbsenceItemTableData( response );
				//
				loadStudentsParents( studentId );
			}
		});
	}
	
	
	/*
	 * */
	private void loadStudentsParents( String studentId ) {
		//
		ParentRequestFactory rf = GWT.create(ParentRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ParentRequestContext rc = rf.parentRequestContext();
		rc.listAllByStudent( studentId ).fire(new Receiver<List<ParentProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<ParentProxy> response) {
				getView().setParentData(response);
			}
		});
	}

	
	/*
	 * */
	@Override
	public void showAbsenceInputPopup( String studentId, String studentName ) {
		//
		addToPopupSlot( popupAbsenceInput, false );
		popupAbsenceInput.onPopupStart( studentId, studentName );
	}


	
	/*
	 * */
	@ProxyEvent
	@Override
	public void onAbsenceAfterInput(AbsenceAfterInputEvent event) {
		// Fire the selection with the current selected value
		filterDate( curStudentId, fromDate, toDate );
	}


	
	/*
	 * */
	@Override
	public void removeAbsenceItem(AbsenceItemProxy aip) {
		//
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		rc.removeAbsenceItem(aip).fire(new Receiver<Void>() {
			@Override
			public void onFailure(ServerFailure error){
				//
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( Void response ) {
				//
				getView().removeDeletedAbsenceItem();
			}
		});
	}


	/*
	 * */
	@Override
	public void loadAbsentStudens(String dateFrom, String dateTo) {
		//
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		rc.loadAbsentStudents( dateFrom, dateTo ).fire(new Receiver<List<AbsenceItemProxy>>() {
			@Override
			public void onFailure(ServerFailure error){
				//
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( List<AbsenceItemProxy> response ) {
				//
				getView().setStudentTableData( response );
			}
		});
	}


	/*
	 * */
	@Override
	public void sendEmail(final String absenceItemID, String studentName, String parentName,
			String parentEmail, String message) {
		//
		String subject = "Notification de l'absence de " + studentName;
		//
		String from = "thuannn@gmail.com, Ecole Lemania";
		//
		String to = parentEmail + "," + parentName + "/";
		//
		String replyto = "info@lemania.ch, Ecole Lemania"  + "/";
		//
		String cc = "info@lemania.ch, INFO Lemania" + "/" + "thuan.nguyen@lemania.ch, Thuan Nguyen"  + "/";
//		cc = "/";
		//
		ContactRequestFactory rf = GWT.create(ContactRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ContactRequestContext rc = rf.contactRequest();
		rc.sendEmail( subject, from, to, replyto, cc, message ).fire(new Receiver<Void>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(Void response) {
				//
				saveNotificationDateEmail( absenceItemID );
			}
		});
	}


	/*
	 * */
	@Override
	public void sendSMS(final String absenceItemID, String number, String message) {
		//
		ContactRequestFactory rf = GWT.create(ContactRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ContactRequestContext rc = rf.contactRequest();
		rc.sendSMS( number, message ).fire(new Receiver<Void>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(Void response) {
				//
				saveNotificationDateSMS( absenceItemID );
			}
		});
	}
	
	
	/*
	 * */
	private void saveNotificationDateEmail(String absenceItemID) {
		//
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		rc.saveNotificationDateEmail( absenceItemID ).fire(new Receiver<AbsenceItemProxy>() {
			@Override
			public void onFailure(ServerFailure error){
				//
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( AbsenceItemProxy response ) {
				//
				getView().showNotificationDatesEmail( response );
				getView().setUpdatedAbsenceItem(response);
			}
		});
	}
	
	
	/*
	 * */
	private void saveNotificationDateSMS(String absenceItemID) {
		//
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		rc.saveNotificationDateSMS( absenceItemID ).fire(new Receiver<AbsenceItemProxy>() {
			@Override
			public void onFailure(ServerFailure error){
				//
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( AbsenceItemProxy response ) {
				//
				getView().showNotificationDatesSMS( response );
				getView().setUpdatedAbsenceItem(response);
			}
		});
	}
}
