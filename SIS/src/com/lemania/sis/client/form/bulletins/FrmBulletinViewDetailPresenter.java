package com.lemania.sis.client.form.bulletins;

import java.util.ArrayList;
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
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.LoggedInGatekeeper;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory.BulletinRequestContext;
import com.lemania.sis.shared.bulletinbranche.BulletinBrancheProxy;
import com.lemania.sis.shared.bulletinbranche.BulletinBrancheRequestFactory;
import com.lemania.sis.shared.bulletinbranche.BulletinBrancheRequestFactory.BulletinBrancheRequestContext;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectRequestFactory;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectRequestFactory.BulletinSubjectRequestContext;
import com.lemania.sis.shared.service.ClasseRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ClasseRequestFactory.ClasseRequestContext;

public class FrmBulletinViewDetailPresenter
		extends
		Presenter<FrmBulletinViewDetailPresenter.MyView, FrmBulletinViewDetailPresenter.MyProxy> 
		implements 
		FrmBulletinViewDetailUiHandler, LoginAuthenticatedHandler {
	
	// Thuan
	private CurrentUser currentUser;
	private List<BulletinSubjectProxy> subjects = new ArrayList<BulletinSubjectProxy>();
	

	public interface MyView extends View, HasUiHandlers<FrmBulletinViewDetailUiHandler> {
		//
		void resetForm();
		void showAdminPanel(Boolean show);
		//
		void setStudentListData(List<BulletinProxy> bulletins);
		//
		void setClasseList(List<ClasseProxy> classes);
		//
		void drawGradeTableMatu(List<BulletinSubjectProxy> subjects, List<BulletinBrancheProxy> branches, Boolean isStudent);
		void drawGradeTablePrematurite(List<BulletinSubjectProxy> subjects, List<BulletinBrancheProxy> branches, Boolean isStudent);
		void drawGradeTableNormal(List<BulletinSubjectProxy> subjects, List<BulletinBrancheProxy> branches, Boolean isStudent);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.bulletindetail)
	@UseGatekeeper(LoggedInGatekeeper.class)
	public interface MyProxy extends ProxyPlace<FrmBulletinViewDetailPresenter> {
	}

	@Inject
	public FrmBulletinViewDetailPresenter(final EventBus eventBus,
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
		
		// Thuan
		getView().setUiHandlers(this);
	}
	
	
	@Override
	protected void onReset() {
		//
		super.onReset();
		// Thuan
		getView().resetForm();
		//
		if (currentUser.isAdmin())
			loadClassList();
		if (currentUser.isProf())
			loadClassListByProf();
		if (currentUser.isStudent())
			loadStudentList();
		if ( currentUser.isParent() )
			loadStudentListByParent();
	}
	
	
	/*
	 * */
	private void loadStudentListByParent() {
		//
		// Hide the class list
		getView().showAdminPanel(false);
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
				getView().setStudentListData(response);
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

	
	//
	private void loadStudentList() {
		//
		getView().showAdminPanel(false);
		//
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		//
		if ( currentUser.isAdmin() || currentUser.isProf() ) {
			rc.listAllByEmail( currentUser.getUserEmail() ).fire(new Receiver<List<BulletinProxy>>(){
				@Override
				public void onFailure(ServerFailure error){
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess(List<BulletinProxy> response) {
					getView().setStudentListData(response);
				}
			});
		} else {
			rc.listAllByEmailForPublic( currentUser.getUserEmail() ).fire(new Receiver<List<BulletinProxy>>(){
				@Override
				public void onFailure(ServerFailure error){
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess(List<BulletinProxy> response) {
					getView().setStudentListData(response);
				}
			});
		}
	}

	
	/**/
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		this.currentUser = event.getCurrentUser();
	}

	
	/**/
	@Override
	public void onStudentSelected() {
		//
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
				getView().setStudentListData(response);
			}
		});
	}
	

	/*
	 * */
	@Override
	public void onBulletinChange(final BulletinProxy bulletin) {
		//
		BulletinSubjectRequestFactory rf = GWT.create(BulletinSubjectRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinSubjectRequestContext rc = rf.bulletinSubjectRequest();
		if ( currentUser.isAdmin() || currentUser.isProf() ) {
			rc.listAll( bulletin.getId().toString() ).fire(new Receiver<List<BulletinSubjectProxy>>(){
				@Override
				public void onFailure(ServerFailure error){
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess(List<BulletinSubjectProxy> response) {
					subjects.clear();
					subjects.addAll(response);
					getBranches( bulletin );
				}
			});
		} else {
			rc.listAllForPublic( bulletin.getId().toString() ).fire(new Receiver<List<BulletinSubjectProxy>>(){
				@Override
				public void onFailure(ServerFailure error){
					Window.alert(error.getMessage());
				}
				@Override
				public void onSuccess(List<BulletinSubjectProxy> response) {
					subjects.clear();
					subjects.addAll(response);
					getBranches( bulletin );
				}
			});
		}
	}

	
	/*
	 * */
	protected void getBranches(final BulletinProxy bulletin) {
		//
		BulletinBrancheRequestFactory rf = GWT.create(BulletinBrancheRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinBrancheRequestContext rc = rf.bulletinBrancheRequest();
		rc.listAllByBulletin( bulletin.getId().toString() ).fire(new Receiver<List<BulletinBrancheProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<BulletinBrancheProxy> response) {
				drawBulletin( bulletin, subjects, response);
			}
		});
	}
	
	
	/*
	 * */
	protected void drawBulletin(BulletinProxy bulletin, final List<BulletinSubjectProxy> subjects, final List<BulletinBrancheProxy> branches){
		//
//		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
//		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
//		BulletinRequestContext rc = rf.bulletinRequest();
//		rc.getBulletin( bulletinId ).fire(new Receiver<BulletinProxy>(){
//			@Override
//			public void onFailure(ServerFailure error){
//				Window.alert(error.getMessage());
//			}
//			@Override
//			public void onSuccess(BulletinProxy response) {
				if (bulletin.getProgrammeName().toLowerCase().contains("matu")){ 
					if (bulletin.getClasseName().toLowerCase().contains("prématurité"))
						getView().drawGradeTablePrematurite(subjects, branches, (currentUser.isStudent() || currentUser.isParent()) ? true:false );
					else
						getView().drawGradeTableMatu(subjects, branches, (currentUser.isStudent() || currentUser.isParent()) ? true:false );
				}
				else
					getView().drawGradeTableNormal(subjects, branches, (currentUser.isStudent() || currentUser.isParent()) ? true:false );
//			}
//		});
	}
}
