package com.lemania.sis.client.form.studylogmgt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.LoggedInGatekeeper;
import com.lemania.sis.client.UI.FieldValidation;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.form.studylogmgt.StudyLogClassLoadEvent.StudyLogClassLoadHandler;
import com.lemania.sis.client.form.studylogmgt.StudyLogLoadLogsEvent.StudyLogLoadLogsHandler;
import com.lemania.sis.client.form.studylogmgt.StudyLogStudentLoadEvent.StudyLogStudentLoadHandler;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.client.values.DataValues;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.SubjectProxy;
import com.lemania.sis.shared.assignment.AssignmentRequestFactory;
import com.lemania.sis.shared.assignment.AssignmentRequestFactory.AssignmentRequestContext;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory.BulletinRequestContext;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ProfessorRequestFactory;
import com.lemania.sis.shared.service.ProfessorRequestFactory.ProfessorRequestContext;
import com.lemania.sis.shared.studylog.StudyLogProxy;
import com.lemania.sis.shared.studylog.StudyLogRequestFactory;
import com.lemania.sis.shared.studylog.StudyLogRequestFactory.StudyLogRequestContext;
public class StudyLogManagementPresenter extends Presenter<StudyLogManagementPresenter.MyView, StudyLogManagementPresenter.MyProxy> 
	implements StudyLogManagementUiHandlers, LoginAuthenticatedHandler,
				StudyLogClassLoadHandler, StudyLogStudentLoadHandler, StudyLogLoadLogsHandler {
    
	//
	interface MyView extends View , HasUiHandlers<StudyLogManagementUiHandlers> {
		//
		void resetForm();
		//
		void initializeUI();
		//
		void setProfListData(List<ProfessorProxy> profs);
		//
		void setSubjectsData(List<SubjectProxy> subjects);
		//
		void setClassListData(List<ClasseProxy> classes);
		//
		void setStudentListData(List<BulletinProxy> students);
		//
		void showAddedLog( StudyLogProxy studyLog );
		void showAddedLogs( List<StudyLogProxy> studyLogs );
		//
		void showLogs( List<StudyLogProxy> studyLogs );
		//
		void removeDeletedLog( String logId );
		//
		void showUpdatedLog( String editLogId, String logTitle, String logContent, String logFileName );
    }
	
    @ContentSlot
    public static final Type<RevealContentHandler<?>> SLOT_StudyLogManagement = new Type<RevealContentHandler<?>>();

    @NameToken(NameTokens.studylogmgt)
    @ProxyCodeSplit
    @UseGatekeeper( LoggedInGatekeeper.class )
    interface MyProxy extends ProxyPlace<StudyLogManagementPresenter> {
    }

    @Inject
    StudyLogManagementPresenter(
            EventBus eventBus,
            MyView view, 
            MyProxy proxy) {
        super(eventBus, view, proxy, MainPagePresenter.TYPE_SetMainContent);
        
        getView().setUiHandlers(this);
    }
    
    
    /*
     * */
    CurrentUser currentUser;
    List<ClasseProxy> classes = new ArrayList<ClasseProxy>();
    
    
    /*
     * */
    protected void onBind() {
        super.onBind();
		//
		getView().initializeUI();
    }
    
    protected void onReset() {
        super.onReset();
        //
        getView().resetForm();
        //
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
	

	/***************** UI Handlers **********************/
	
	
	/*
	 * When a professor is selected, load the subjects he/she takes care of
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
		rc.listAllSubjectByProfessor( profId ).fire(new Receiver<List<SubjectProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<SubjectProxy> response) {
				getView().setSubjectsData(response);
			}
		});
	}
	
	
	/*
	 * When user select a subject, load the class list, student list and logs using events
	 * */
	@Override
	public void onLstAssignmentsChange(String profId, String subjectId) {
		//
		if (profId.isEmpty() || subjectId.isEmpty()) {
			return;
		}
		// Load the class list
		getEventBus().fireEvent( new StudyLogClassLoadEvent("", profId, subjectId) );
	}
	
	/*
	 * When user change a class:
	 * - filter the student list and keep the updated list for log saving purpose
	 * - load the logs of this class
	 * selectedBsp : list of bulletins for log saving purpose
	 * */
	@Override
	public void onLstClassChange( String profId, String subjectId, String classId, String dateFrom, String dateTo) {
		//
		getEventBus().fireEvent( new StudyLogStudentLoadEvent("", profId, subjectId, classId) );
		getEventBus().fireEvent( new StudyLogLoadLogsEvent("", subjectId, classId, dateFrom, dateTo ));
	}
	
	
	/*
	 * If user choose the option of all classes, concatenate the class Ids to "|" string and save batch
	 * */
	@Override
	public void onStudyLogAdd ( String profId, String subjectId, String classeId, 
			final String logTitle, final String logContent, final String editLogId, final String logFileName, String logEntryDate ) {
		//
		if (logTitle.equals("") || logContent.equals("")) {
			Window.alert( NotificationValues.invalid_input + " - Titre & Contenu");
			return;
		}
		//
		StudyLogRequestFactory rf = GWT.create(StudyLogRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		StudyLogRequestContext rc = rf.studyLogRequestContext();
		if ( !classeId.equals( DataValues.optionAll) ) {
			//
			if ( editLogId.equals("") ) {
				rc.saveAndReturn(
						subjectId, classeId, logTitle, logContent, 
						logEntryDate,
						FieldValidation.getFileNameFormat( logEntryDate, logFileName ) )
					.fire(new Receiver<StudyLogProxy>(){
					@Override
					public void onFailure(ServerFailure error){
						Window.alert(error.getMessage());
					}
					@Override
					public void onSuccess(StudyLogProxy response) {
						getView().showAddedLog( response );
					}
				});
			} else {
				rc.updateLog(
						subjectId, classeId, logTitle, logContent, 
						logEntryDate,
						editLogId,
						FieldValidation.getFileNameFormat( logEntryDate, logFileName ) )
					.fire(new Receiver<StudyLogProxy>(){
					@Override
					public void onFailure(ServerFailure error){
						Window.alert(error.getMessage());
					}
					@Override
					public void onSuccess(StudyLogProxy response) {
						getView().showUpdatedLog( editLogId, logTitle, logContent, FieldValidation.getFileName(logFileName) );
					}
				});
			}
		} else {
			//
			String classeIdList = "";
			for (ClasseProxy classe : classes )
				classeIdList = classeIdList + "|" + classe.getId().toString();
			
			rc.saveAndReturnBatch(
					subjectId, 
					classeIdList, 
					logTitle, logContent, 
					logEntryDate,
					FieldValidation.getFileNameFormat( logEntryDate, logFileName ) )
				.fire(new Receiver<List<StudyLogProxy>>(){
				@Override
				public void onFailure(ServerFailure error){
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess(List<StudyLogProxy> response) {
					getView().showAddedLogs( response );
				}
			});
		}
	}
	
	
	/*
	 * */
	@Override
	public void deleteLog( final StudyLogProxy log ) {
		//
		StudyLogRequestFactory rf = GWT.create(StudyLogRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		StudyLogRequestContext rc = rf.studyLogRequestContext();
		rc.removeStudyLog(log).fire(new Receiver<Void>() {
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(Void response) {
				getView().removeDeletedLog( log.getId().toString() );
			}
		});
	}
	

	/***************** Event Handlers **********************/
	
	/*
	 * Load list of classes base on professor and subject
	 * */
	@ProxyEvent
	@Override
	public void onStudyLogClassLoad(StudyLogClassLoadEvent event) {
		//
		AssignmentRequestFactory rf = GWT.create(AssignmentRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AssignmentRequestContext rc = rf.assignmentRequest();
		rc.listAllClassByProfAndSubject( event.getProfId(), event.getSubjectId() ).fire(new Receiver<List<ClasseProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<ClasseProxy> response) {
				getView().setClassListData( response );
				classes.clear();
				classes.addAll( response );
			}
		});
	}
	
	
	/*
	 * Get the current user info when user is logged in 
	 * */
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		this.currentUser = event.getCurrentUser();
	}

	
	/*
	 * Load list of student base on professor, subject and class
	 * */
	@ProxyEvent
	@Override
	public void onStudyLogStudentLoad(StudyLogStudentLoadEvent event) {
		//
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		rc.listAllStudentByProfSubjectClass( event.getProfId(), event.getSubjectId(), event.getClassId() ).fire(new Receiver<List<BulletinProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<BulletinProxy> response) {
				//
				getView().setStudentListData( response );
			}
		});
	}

	
	/*
	 * Load list of logs base on subject and class
	 * */
	@ProxyEvent
	@Override
	public void onStudyLogLoadLogs(StudyLogLoadLogsEvent event) {
		//
		StudyLogRequestFactory rf = GWT.create(StudyLogRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		StudyLogRequestContext rc = rf.studyLogRequestContext();
		if ( !event.getClasseId().equals( DataValues.optionAll) ) {
			//
			rc.listAllBySubjectClass( event.getSubjectId(), event.getClasseId(), event.getDateFrom(), event.getDateTo() )
					.fire(new Receiver<List<StudyLogProxy>>(){
				@Override
				public void onFailure(ServerFailure error){
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess(List<StudyLogProxy> response) {
					getView().showLogs( response );
				}
			});
		} else {
			//
			rc.listAllBySubject( event.getSubjectId(), event.getDateFrom(), event.getDateTo() ).fire(new Receiver<List<StudyLogProxy>>(){
				@Override
				public void onFailure(ServerFailure error){
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess(List<StudyLogProxy> response) {
					getView().showLogs( response );
				}
			});
		}
	}
}

