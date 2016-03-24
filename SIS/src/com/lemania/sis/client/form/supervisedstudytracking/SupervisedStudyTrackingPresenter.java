package com.lemania.sis.client.form.supervisedstudytracking;

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
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.LoggedInGatekeeper;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.form.supervisedstudysubscriptionevents.OnSupervisedStudySubscriptionUpdateEvent;
import com.lemania.sis.client.form.supervisedstudysubscriptionevents.OnSupervisedStudySubscriptionUpdateEvent.OnSupervisedStudySubscriptionUpdateHandler;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.shared.ProfessorProxy;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionProxy;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionRequestFactory;
import com.lemania.sis.shared.coursesubscription.CourseSubscriptionRequestFactory.CourseSubscriptionRequestContext;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ProfessorRequestFactory;
import com.lemania.sis.shared.service.ProfessorRequestFactory.ProfessorRequestContext;
public class SupervisedStudyTrackingPresenter extends Presenter<SupervisedStudyTrackingPresenter.MyView, SupervisedStudyTrackingPresenter.MyProxy> 
		implements SupervisedStudyTrackingUiHandlers, LoginAuthenticatedHandler, OnSupervisedStudySubscriptionUpdateHandler {
    
	
	//
	private CurrentUser currentUser;
	
	//
	interface MyView extends View , HasUiHandlers<SupervisedStudyTrackingUiHandlers> {
		//
		void initializeUI();
		//
		void setProfListData(List<ProfessorProxy> profs, boolean autoSelect);
		//
		public void setAppliedStudentsTableData( List<CourseSubscriptionProxy> list );
		public void setStudentSubscriptionsTableData( List<CourseSubscriptionProxy> list );
		//
		public void resetForm();
    }
    
	//
	@ContentSlot
    public static final Type<RevealContentHandler<?>> SLOT_SupervisedStudyTracking = new Type<RevealContentHandler<?>>();

	//
    @NameToken(NameTokens.supervisedstudytracking)
    @ProxyCodeSplit
    @UseGatekeeper(LoggedInGatekeeper.class)
    interface MyProxy extends ProxyPlace<SupervisedStudyTrackingPresenter> {
    }

    //
    @Inject
    SupervisedStudyTrackingPresenter(
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
        loadProfessorList();
        //
        getView().resetForm();
    }
    
    /*-------------------------------*/
    
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
					getView().setProfListData(response, true);
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
					getView().setProfListData(response, false);
				}
			});
		}
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
				//
				getView().setAppliedStudentsTableData(response);
			}
		});
	}
	
	
	/*
	 * */
	@Override
	public void loadStudentSubscriptions(CourseSubscriptionProxy subscription) {
		//
		CourseSubscriptionRequestFactory rf = GWT.create(CourseSubscriptionRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		CourseSubscriptionRequestContext rc = rf.courseSubscriptionRequestContext();
		rc.listAllByStudent( subscription ).fire(new Receiver<List<CourseSubscriptionProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<CourseSubscriptionProxy> response) {
				//
				getView().setStudentSubscriptionsTableData(response);
			}
		});
	}
	
	
	/*
	 * */
	@Override
	public void saveSubscriptionNote(final CourseSubscriptionProxy subscription,
			String note) {
		//
		CourseSubscriptionRequestFactory rf = GWT.create(CourseSubscriptionRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		CourseSubscriptionRequestContext rc = rf.courseSubscriptionRequestContext();
		CourseSubscriptionProxy cs = rc.edit( subscription );
		cs.setNote(note);
		rc.saveAndReturn(cs).fire(new Receiver<CourseSubscriptionProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(CourseSubscriptionProxy response) {
				//
				getEventBus().fireEvent( new OnSupervisedStudySubscriptionUpdateEvent( subscription ) );
			}
		});
	}
    
    
    /*-------------------------------*/
    
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
	@ProxyEvent
	@Override
	public void onOnSupervisedStudySubscriptionUpdate(
			OnSupervisedStudySubscriptionUpdateEvent event) {
		//
		loadStudentSubscriptions( event.getSubscription() );
	}

}