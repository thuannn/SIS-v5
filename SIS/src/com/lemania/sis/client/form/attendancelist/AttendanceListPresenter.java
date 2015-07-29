package com.lemania.sis.client.form.attendancelist;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.LoggedInGatekeeper;
import com.lemania.sis.client.ProfessorGateKeeper;
import com.lemania.sis.client.event.AttendanceListAssignmentAfterSelectedEvent;
import com.lemania.sis.client.event.AttendanceListAssignmentAfterSelectedEvent.AttendanceListAssignmentAfterSelectedHandler;
import com.lemania.sis.client.event.AttendanceListAssignmentSelectedEvent;
import com.lemania.sis.client.event.AttendanceListAssignmentSelectedEvent.AttendanceListAssignmentSelectedHandler;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.absenceitem.AbsenceItemProxy;
import com.lemania.sis.shared.absenceitem.AbsenceItemRequestFactory;
import com.lemania.sis.shared.absenceitem.AbsenceItemRequestFactory.AbsenceItemRequestContext;
import com.lemania.sis.shared.assignment.AssignmentProxy;
import com.lemania.sis.shared.assignment.AssignmentRequestFactory;
import com.lemania.sis.shared.assignment.AssignmentRequestFactory.AssignmentRequestContext;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectRequestFactory;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectRequestFactory.BulletinSubjectRequestContext;
import com.lemania.sis.shared.period.PeriodProxy;
import com.lemania.sis.shared.period.PeriodRequestFactory;
import com.lemania.sis.shared.period.PeriodRequestFactory.PeriodRequestContext;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ProfessorRequestFactory;
import com.lemania.sis.shared.service.ProfessorRequestFactory.ProfessorRequestContext;

public class AttendanceListPresenter
		extends
		Presenter<AttendanceListPresenter.MyView, AttendanceListPresenter.MyProxy>
		implements 
			AttendanceListUiHandlers,
			LoginAuthenticatedHandler,
			AttendanceListAssignmentSelectedHandler,
			AttendanceListAssignmentAfterSelectedHandler {
	
	// Thuan
	private CurrentUser currentUser;
	private String strAbsenceDate;
	
	interface MyView extends View, HasUiHandlers<AttendanceListUiHandlers> {
		//
		void setProfListData(List<ProfessorProxy> profs);
		//
		void setAssignmentTableData(List<AssignmentProxy> assignments);
		//
		void setStudentListData(List<BulletinSubjectProxy> bulletins);
		//
		void setPeriodListData(List<PeriodProxy> periods);
		//
		void initializeUI();
		//
		void resetUI( CurrentUser curUser );
		//
		void setAddedAbsenceItem( AbsenceItemProxy aip );
		//
		void removeDeletedAbsenceItemId();
		//
		void showAbsenceItems( List<AbsenceItemProxy> aip );
		//
		void setUpdatedAbsenceItem( AbsenceItemProxy ai );
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_AttendanceList = new Type<RevealContentHandler<?>>();

	@NameToken(NameTokens.attendancelist)
	@ProxyCodeSplit
	@UseGatekeeper( ProfessorGateKeeper.class )
	interface MyProxy extends ProxyPlace<AttendanceListPresenter> {
	}

	@Inject
	AttendanceListPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, MainPagePresenter.TYPE_SetMainContent);

		getView().setUiHandlers(this);
	}

	protected void onBind() {
		//
		super.onBind();
		//
		getView().initializeUI();
	}

	protected void onReset() {
		//
		super.onReset();
		//
		getView().resetUI( currentUser );
		// Thuan
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
	 * */
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		this.currentUser = event.getCurrentUser();
	}

	
	/*
	 * When a professor is selected, load all the assignments of him/her
	 * */
	@Override
	public void onProfessorSelected(String profId) {
		//
		if (profId.isEmpty()){
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
	 * When an assignment is selected, list all the student of this professor using the information contained in the assignment
	 * */
	@Override
	public void onAssignmentSelected( final AssignmentProxy a, String strAbsenceDate ) {
		//
		this.strAbsenceDate = strAbsenceDate;
		//
		BulletinSubjectRequestFactory rf = GWT.create(BulletinSubjectRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinSubjectRequestContext rc = rf.bulletinSubjectRequest();
		rc.listAllByAssignment( a.getId().toString() ).fire(new Receiver<List<BulletinSubjectProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				//
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<BulletinSubjectProxy> response) {				
				//
				getView().setStudentListData(response);		
				getEventBus().fireEvent( new AttendanceListAssignmentSelectedEvent(a) );
			}
		});
	}
	

	/*
	 * Load the period list
	 * */
	@ProxyEvent
	@Override
	public void onAttendanceListAssignmentSelected(final AttendanceListAssignmentSelectedEvent event) {
		//
		PeriodRequestFactory rf = GWT.create(PeriodRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		PeriodRequestContext rc = rf.periodRequestContext();
		rc.listAllByClass( event.getAssignment().getClassId() ).fire(new Receiver<List<PeriodProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<PeriodProxy> response) {
				//
				getView().setPeriodListData(response);
				//
				getEventBus().fireEvent( new AttendanceListAssignmentAfterSelectedEvent( event.getAssignment().getId().toString() ));
			}
		});
		
	}

	
	/*
	 * */
	@Override
	public void saveAbsenceItem(String strAbsenceDate, String studentId, String periodId,
			String profId, String classId, String subjectId, String motifId,
			String codeAbsence, String profComment, int lateMinute,
			boolean justified, boolean parentNotified) {
		// 
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		rc.saveAbsenceItem(strAbsenceDate, studentId, periodId, profId, classId, subjectId, motifId, codeAbsence, profComment, lateMinute, justified, parentNotified)
			.fire(new Receiver<AbsenceItemProxy>(){
				@Override
				public void onFailure(ServerFailure error){
					//
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess( AbsenceItemProxy response ) {
					//
					getView().setAddedAbsenceItem( response );
				}
			});
	}

	
	/*
	 * */
	@Override
	public void removeAbsenceItem(String aiID) {
		//
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		rc.removeAbsenceItem( aiID ).fire(new Receiver<Void>(){
				@Override
				public void onFailure(ServerFailure error){
					//
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess( Void response ) {
					//
					getView().removeDeletedAbsenceItemId();
				}
			});
	}

	
	/*
	 * Load the Absence Item and show on the table
	 * */
	@ProxyEvent
	@Override
	public void onAttendanceListAssignmentAfterSelected(AttendanceListAssignmentAfterSelectedEvent event) {
		//
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		rc.listAllByAssignment( event.getAssignmentId(), this.strAbsenceDate ).fire(new Receiver<List<AbsenceItemProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				//
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( List<AbsenceItemProxy> response ) {
				//
				getView().showAbsenceItems( response );
			}
		});
	}

	
	/*
	 * */
	@Override
	public void updateAbsenceLateItem(String aiID, String minutes) {
		//
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		rc.updateAbsenceLateItem( aiID, Integer.parseInt(minutes) ).fire(new Receiver<AbsenceItemProxy>(){
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
	public void updateRemarque(String absenceItemID, String strRemarque) {
		//
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		rc.updateRemarque( absenceItemID, strRemarque ).fire(new Receiver<AbsenceItemProxy>(){
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
}
