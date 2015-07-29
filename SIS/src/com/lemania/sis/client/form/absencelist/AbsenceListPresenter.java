package com.lemania.sis.client.form.absencelist;

import java.util.ArrayList;
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
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.absenceitem.AbsenceItemProxy;
import com.lemania.sis.shared.absenceitem.AbsenceItemRequestFactory;
import com.lemania.sis.shared.absenceitem.AbsenceItemRequestFactory.AbsenceItemRequestContext;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory.BulletinRequestContext;
import com.lemania.sis.shared.service.ClasseRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ClasseRequestFactory.ClasseRequestContext;
public class AbsenceListPresenter extends Presenter<AbsenceListPresenter.MyView, AbsenceListPresenter.MyProxy> 
	implements AbsenceListUiHandlers, LoginAuthenticatedHandler {
	
	//
    interface MyView extends View , HasUiHandlers<AbsenceListUiHandlers> {
    	//
    	void setStudentListData( List<BulletinProxy> students, boolean isUnique );
    	//
    	void setStudentAbsenceListData( List<AbsenceItemProxy> absences );
    	//
    	void resetUI();
    	//
    	void showAdminPanel(Boolean show);
    	//
		void setClasseList(List<ClasseProxy> classes);
    }
    
    //
    @ContentSlot
    public static final Type<RevealContentHandler<?>> SLOT_AbsenceList = new Type<RevealContentHandler<?>>();

    @NameToken(NameTokens.absenceslist)
    @ProxyCodeSplit
    @UseGatekeeper( LoggedInGatekeeper.class )
    interface MyProxy extends ProxyPlace<AbsenceListPresenter> {
    }

    @Inject
    AbsenceListPresenter(
            EventBus eventBus,
            MyView view, 
            MyProxy proxy) {
        super(eventBus, view, proxy, MainPagePresenter.TYPE_SetMainContent);
        
        getView().setUiHandlers(this);
    }
    
    
    // Thuan
 	private CurrentUser currentUser;
 	List<BulletinProxy> bulletins = new ArrayList<BulletinProxy>();
    
    
 	
 	/*
 	 * */
 	protected void onBind() {
        super.onBind();
    }
 	
    
    /*
     * */
    protected void onReset() {
        super.onReset();
        //
        getView().resetUI();
        //
        if (currentUser.isStudent())
			loadStudentList();
        if (currentUser.isParent())
        	loadStudentListByParent();
        if (currentUser.isProf())
        	loadClassListByProf();
    }
    
    
    /*
	 * */
	private void loadStudentList() {
		//
		getView().showAdminPanel(false);
		//
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(),
				new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		//
		if (currentUser.isAdmin() || currentUser.isProf()) {
			rc.listAllByEmail(currentUser.getUserEmail()).fire(
					new Receiver<List<BulletinProxy>>() {
						@Override
						public void onFailure(ServerFailure error) {
							Window.alert(error.getMessage());
						}

						@Override
						public void onSuccess(List<BulletinProxy> response) {
							//
							getView().setStudentListData(response, true);
							//
							bulletins.clear();
							bulletins.addAll(response);
						}
					});
		} else {
			rc.listAllByEmailForPublic(currentUser.getUserEmail()).fire(
					new Receiver<List<BulletinProxy>>() {
						@Override
						public void onFailure(ServerFailure error) {
							Window.alert(error.getMessage());
						}

						@Override
						public void onSuccess(List<BulletinProxy> response) {
							//
							getView().setStudentListData(response, true);
							//
							bulletins.clear();
							bulletins.addAll(response);
						}
					});
		}
	}
    
    
    /*
	 * */
	private void loadClassList() {
		//
		getView().showAdminPanel(true);
		//
		ClasseRequestFactory rf = GWT.create(ClasseRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ClasseRequestContext rc = rf.classeRequest();
		rc.listAllActive().fire(new Receiver<List<ClasseProxy>>(){
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
	private void loadClassListByProf() {
		//
		loadClassList();
	}

    /*
     * */
    private void loadStudentListByParent() {
    	//
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		rc.listAllByParentUserId( currentUser.getUserId().toString() ).fire(new Receiver<List<BulletinProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<BulletinProxy> response) {
				getView().setStudentListData(response, false);
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
     * Load list of absences using student Id
     * */
	@Override
	public void loadAbsenceList(String studentId) {
		//
		AbsenceItemRequestFactory rf = GWT.create(AbsenceItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		AbsenceItemRequestContext rc = rf.absenceItemRequestContext();
		rc.listAllByStudent(studentId).fire(new Receiver<List<AbsenceItemProxy>>() {
			@Override
			public void onFailure(ServerFailure error){
				//
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( List<AbsenceItemProxy> response ) {
				//
				getView().setStudentAbsenceListData( response );
			}
		});
	}
	
	
	/*
	 * */
	@Override
	public void onClassChange(String classId) {
		// 
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		rc.listAllActiveByClass( classId ).fire(new Receiver<List<BulletinProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<BulletinProxy> response) {
				getView().setStudentListData(response, false);
			}
		});
	}
}