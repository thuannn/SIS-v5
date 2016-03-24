package com.lemania.sis.client.form.individualCourse.individualcoursesubscription;

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
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionProxy;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionRequestFactory;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionRequestFactory.CourseSubscriptionRequestContext;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ProfessorRequestFactory;
import com.lemania.sis.shared.service.ProfessorRequestFactory.ProfessorRequestContext;
import com.lemania.sis.shared.student.StudentProxy;
import com.lemania.sis.shared.student.StudentRequestFactory;
import com.lemania.sis.shared.student.StudentRequestFactory.StudentRequestContext;
public class IndividualCourseSubscriptionPresenter extends Presenter<IndividualCourseSubscriptionPresenter.MyView, IndividualCourseSubscriptionPresenter.MyProxy> 
	implements IndividualCourseSubscriptionUiHandlers, LoginAuthenticatedHandler {
    
	//
	private CurrentUser currentUser;
	
	//
	interface MyView extends View , HasUiHandlers<IndividualCourseSubscriptionUiHandlers> {
		//
		void initializeUI();
		//
		public void setTableData( List<StudentProxy> studentList );
		//
		public void setAppliedStudentsTableData( List<CourseSubscriptionProxy> list );
		//
		void setProfListData(List<ProfessorProxy> profs);
    }
	
	//
    @ContentSlot
    public static final Type<RevealContentHandler<?>> SLOT_IndividualCourseSubscription = new Type<RevealContentHandler<?>>();

    //
    @NameToken(NameTokens.individualCourseSubscription)
    @ProxyCodeSplit
    interface MyProxy extends ProxyPlace<IndividualCourseSubscriptionPresenter> {
    }

    //
    @Inject
    IndividualCourseSubscriptionPresenter(
            EventBus eventBus,
            MyView view, 
            MyProxy proxy) {
        super(eventBus, view, proxy, MainPagePresenter.TYPE_SetMainContent);
        
        getView().setUiHandlers(this);
    }
    
    //
    protected void onBind() {
    	//
        super.onBind();
        //
		getView().initializeUI();
    }
    
    //
    protected void onReset() {
    	//
        super.onReset();
        //
        loadStudentList();
        //
        loadProfessorList();
    }
    
    
    /* 
     * Load student list when form is opened 
     * */
	private void loadStudentList() {
		//
		StudentRequestContext rc = getStudentRequestContext();
		rc.listAll().fire(new Receiver<List<StudentProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<StudentProxy> response) {
				getView().setTableData(response);
			}
		});
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
	 * Get the request context for StudenProxy.
	 * Used in every function which call to Request Factory 
	 * */
	public StudentRequestContext getStudentRequestContext() {
		StudentRequestFactory rf = GWT.create(StudentRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		return rf.studentRequest();
	}

	
	/*
	 * */
	@Override
	public void loadAppliedStudentsByDate(String date) {
		//
		CourseSubscriptionRequestFactory rf = GWT.create(CourseSubscriptionRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		CourseSubscriptionRequestContext rc = rf.courseSubscriptionRequestContext();
		rc.listAllByDate( date ).fire(new Receiver<List<CourseSubscriptionProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<CourseSubscriptionProxy> response) {
				getView().setAppliedStudentsTableData(response);
			}
		});
	}
	
	
	/*
	 * Get the current user info when user is logged in */
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		this.currentUser = event.getCurrentUser();
	}

	
	/*
	 * */
	@Override
	public void addCourseSubscription(String studentID, String profID, final String date) {
		//
		CourseSubscriptionRequestFactory rf = GWT.create(CourseSubscriptionRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		CourseSubscriptionRequestContext rc = rf.courseSubscriptionRequestContext();
		rc.saveAndReturn( studentID, profID, date ).fire(new Receiver<CourseSubscriptionProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( CourseSubscriptionProxy response) {
				//
				loadAppliedStudentsByDate( date );
			}
		});
	}

	
	/*
	 * */
	@Override
	public void removeCourseSubscription(CourseSubscriptionProxy subscription, final String date) {
		//
		CourseSubscriptionRequestFactory rf = GWT.create(CourseSubscriptionRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		CourseSubscriptionRequestContext rc = rf.courseSubscriptionRequestContext();
		rc.removeCourseSubscription(subscription).fire(new Receiver<Void>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(Void response ) {
				//
				loadAppliedStudentsByDate( date );
			}
		});
	}
}