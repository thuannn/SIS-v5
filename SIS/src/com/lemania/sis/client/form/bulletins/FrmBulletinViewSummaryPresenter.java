package com.lemania.sis.client.form.bulletins;

import java.util.Date;
import java.util.List;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.lemania.sis.client.event.DrawSchoolInterfaceEvent;
import com.lemania.sis.client.event.DrawSchoolInterfaceEvent.DrawSchoolInterfaceHandler;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.CurrentUser;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory.BulletinRequestContext;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectProxy;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectRequestFactory;
import com.lemania.sis.shared.bulletinsubject.BulletinSubjectRequestFactory.BulletinSubjectRequestContext;
import com.lemania.sis.shared.service.ClasseRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ClasseRequestFactory.ClasseRequestContext;

public class FrmBulletinViewSummaryPresenter
		extends
		Presenter<FrmBulletinViewSummaryPresenter.MyView, FrmBulletinViewSummaryPresenter.MyProxy>
		implements FrmBulletinViewSummaryUiHandler, DrawSchoolInterfaceHandler, LoginAuthenticatedHandler {

	public interface MyView extends View, HasUiHandlers<FrmBulletinViewSummaryUiHandler> {
		//
		void resetForm();
		//
		void setClasseList(List<ClasseProxy> classes);
		//
		void setStudentListData(List<BulletinProxy> bulletins);
		//
		void drawBulletinSubjectList( List<BulletinSubjectProxy> subjects );
		//
		void saveRemarqueDirection( BulletinProxy bp );
		//
		void drawPierreViretInterface();
		//
		void drawDate(String date);
	}
	
	
	// Thuan
	CurrentUser currentUser;
		

	@ProxyCodeSplit
	@NameToken(NameTokens.bulletin)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<FrmBulletinViewSummaryPresenter> {
	}

	@Inject
	public FrmBulletinViewSummaryPresenter(final EventBus eventBus,
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
	protected void onReset(){
		super.onReset();
		// Thuan
		getView().resetForm();
		loadClassList();
	}

	
	/**/
	private void loadClassList() {
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

	
	/**/
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

	
	/**/
	@Override
	public void onBulletinChange(String bulletinId) {
		//
		BulletinSubjectRequestFactory rf = GWT.create(BulletinSubjectRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinSubjectRequestContext rc = rf.bulletinSubjectRequest();
		rc.listAllPositiveCoef( bulletinId ).fire(new Receiver<List<BulletinSubjectProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<BulletinSubjectProxy> response) {
				getView().drawBulletinSubjectList(response);
			}
		});
	}

	
	/*
	 * */
	@Override
	public void saveBulletinRemarques(String bulletinId, final String remarqueDirection) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		rc.saveBulletinRemarqueDirection( bulletinId, remarqueDirection ).fire(new Receiver<BulletinProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(BulletinProxy response) {
				getView().saveRemarqueDirection( response );
			}
		});
	}

	@ProxyEvent
	@Override
	public void onDrawSchoolInterface(DrawSchoolInterfaceEvent event) {
		//
		if (event.getSchoolCode() == NotificationValues.pierreViret)
			getView().drawPierreViretInterface();
	}

	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		this.currentUser = event.getCurrentUser();
		//
		getView().drawDate( DateTimeFormat.getFormat("dd.MM.yyyy").format( new Date() ));
		
//		getView().drawDate(
//					this.currentUser.getCurrentDay()+ "." 
//					+ this.currentUser.getCurrentMonth() + "." 
//					+ this.currentUser.getCurrentYear());
	}
}
