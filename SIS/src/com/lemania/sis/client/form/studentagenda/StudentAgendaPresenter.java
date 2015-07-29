package com.lemania.sis.client.form.studentagenda;

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
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.ProfileProxy;
import com.lemania.sis.shared.bulletin.BulletinProxy;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory;
import com.lemania.sis.shared.bulletin.BulletinRequestFactory.BulletinRequestContext;
import com.lemania.sis.shared.masteragendaitem.MasterAgendaItemProxy;
import com.lemania.sis.shared.masteragendaitem.MasterAgendaItemRequestFactory;
import com.lemania.sis.shared.masteragendaitem.MasterAgendaItemRequestFactory.MasterAgendaItemRequestContext;
import com.lemania.sis.shared.period.PeriodProxy;
import com.lemania.sis.shared.period.PeriodRequestFactory;
import com.lemania.sis.shared.period.PeriodRequestFactory.PeriodRequestContext;
import com.lemania.sis.shared.service.ClasseRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ProfileRequestFactory;
import com.lemania.sis.shared.service.ClasseRequestFactory.ClasseRequestContext;
import com.lemania.sis.shared.service.ProfileRequestFactory.ProfileRequestContext;

public class StudentAgendaPresenter
		extends
		Presenter<StudentAgendaPresenter.MyView, StudentAgendaPresenter.MyProxy>
		implements StudentAgendaUiHandlers {
	
	interface MyView extends View, HasUiHandlers<StudentAgendaUiHandlers> {
		//
		void initializeUI();
		void initializeUI(List<PeriodProxy> periods);
		//
		void drawTable(List<PeriodProxy> periods);
		//
		void setClassList(List<ClasseProxy> classes);
		void setProfileListData( List<ProfileProxy> profiles );
		void setStudentListData(List<BulletinProxy> students);
		//
		void showMasterAgendaItemData(List<MasterAgendaItemProxy> mais);
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_StudentAgenda = new Type<RevealContentHandler<?>>();

	@NameToken(NameTokens.studentagenda)
	@ProxyCodeSplit
	@UseGatekeeper( AdminGateKeeper.class )
	public interface MyProxy extends ProxyPlace<StudentAgendaPresenter> {
	}

	@Inject
	public StudentAgendaPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, MainPagePresenter.TYPE_SetMainContent);

		getView().setUiHandlers(this);
	}

	
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
		getView().initializeUI();
		//
		loadClassList();
	}
	
	
	/*
	 * */
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
				getView().setClassList(response);
			}
		});
	}
	
	
	/*
	 * */
	@Override
	public void onClassChanged(String classId) {
		//
		if (classId.isEmpty())			
			return;
		//
		loadProfileList(classId);
		//
		loadPeriodParClass(classId);
	}
	
	
	/*
	 * Load profile list when form is opened
	 * */
	private void loadProfileList(String classId) {
		//
		ProfileRequestFactory rf = GWT.create(ProfileRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		
		ProfileRequestContext rc = rf.profileRequest();
		rc.listAllActiveByClass( classId ).fire(new Receiver<List<ProfileProxy>>(){
			@Override
			public void onSuccess(List<ProfileProxy> response){
				getView().setProfileListData( response );
			}
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
		});
		//
	}
	
	
	/*
	 * */
	private void loadPeriodParClass(String classId) {
		//
		PeriodRequestFactory rf = GWT.create(PeriodRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		PeriodRequestContext rc = rf.periodRequestContext();
		rc.listAllByClass(classId).fire(new Receiver<List<PeriodProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<PeriodProxy> response) {
				getView().initializeUI(response);
			}
		});
	}
	
	
	/*
	 * */
	@Override
	public void onProfileChanged(String classId, String profileId) {
		//
		if (profileId.isEmpty()) {
			Window.alert( NotificationValues.invalid_input + " : Profil");
			return;
		}
		// 
		BulletinRequestFactory rf = GWT.create(BulletinRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		BulletinRequestContext rc = rf.bulletinRequest();
		rc.listAllActiveByClassProfile( classId, profileId ).fire(new Receiver<List<BulletinProxy>>(){
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
	public void onStudentChange(String classId, String profileId, String bulletinId) {
		//
		MasterAgendaItemRequestFactory rf = GWT.create(MasterAgendaItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		MasterAgendaItemRequestContext rc = rf.masterAgendaItemRequestContext();
		rc.listAllByClassProfileStudent( classId, profileId, bulletinId ).fire(new Receiver<List<MasterAgendaItemProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<MasterAgendaItemProxy> response) {
				getView().showMasterAgendaItemData(response);
			}
		});			
	}

}
