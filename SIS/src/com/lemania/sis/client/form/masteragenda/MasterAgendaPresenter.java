package com.lemania.sis.client.form.masteragenda;

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
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.event.MasterAgendaLoadEvent;
import com.lemania.sis.client.event.MasterAgendaLoadEvent.MasterAgendaLoadHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.ClasseProxy;
import com.lemania.sis.shared.ProfileProxy;
import com.lemania.sis.shared.classroom.ClassroomProxy;
import com.lemania.sis.shared.classroom.ClassroomRequestFactory;
import com.lemania.sis.shared.classroom.ClassroomRequestFactory.ClassroomRequestContext;
import com.lemania.sis.shared.masteragendaitem.MasterAgendaItemProxy;
import com.lemania.sis.shared.masteragendaitem.MasterAgendaItemRequestFactory;
import com.lemania.sis.shared.masteragendaitem.MasterAgendaItemRequestFactory.MasterAgendaItemRequestContext;
import com.lemania.sis.shared.period.PeriodProxy;
import com.lemania.sis.shared.period.PeriodRequestFactory;
import com.lemania.sis.shared.period.PeriodRequestFactory.PeriodRequestContext;
import com.lemania.sis.shared.profilesubject.ProfileSubjectProxy;
import com.lemania.sis.shared.profilesubject.ProfileSubjectRequestFactory;
import com.lemania.sis.shared.profilesubject.ProfileSubjectRequestFactory.ProfileSubjectRequestContext;
import com.lemania.sis.shared.service.ClasseRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.ProfileRequestFactory;
import com.lemania.sis.shared.service.ClasseRequestFactory.ClasseRequestContext;
import com.lemania.sis.shared.service.ProfileRequestFactory.ProfileRequestContext;

public class MasterAgendaPresenter extends
		Presenter<MasterAgendaPresenter.MyView, MasterAgendaPresenter.MyProxy>
		implements MasterAgendaUiHandlers, MasterAgendaLoadHandler {
	
	interface MyView extends View, HasUiHandlers<MasterAgendaUiHandlers> {
		//
		void initializeUI();
		void initializeUI(List<PeriodProxy> periods);
		void setEventHandlers();
		//
		void drawTable(List<PeriodProxy> periods);
		void clearSelectedMasterAgendaItem();
		//
		void showAddedMasterAgendaItem(MasterAgendaItemProxy mai);
		void showMasterAgendaItemData(List<MasterAgendaItemProxy> mais);
		//
		void setClassList(List<ClasseProxy> classes);
		void setProfileListData( List<ProfileProxy> profiles );
		void setSubjectListData( List<ProfileSubjectProxy> response );
		void setClassroomList( List<ClassroomProxy> response);
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_MasterAgenda = new Type<RevealContentHandler<?>>();

	@NameToken(NameTokens.masteragenda)
	@ProxyCodeSplit
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<MasterAgendaPresenter> {
	}

	@Inject
	public MasterAgendaPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, MainPagePresenter.TYPE_SetMainContent);

		getView().setUiHandlers(this);
	}

	/*
	 * */
	protected void onBind() {
		super.onBind();
		//
		getView().setEventHandlers();
	}

	/*
	 * */
	protected void onReset() {
		//
		super.onReset();
		//
		getView().initializeUI();
		//
		loadClassList();
		//
		loadClassroomList();
	}

	/*
	 * */
	private void loadClassroomList() {
		//
		ClassroomRequestContext rc = getRequestContext();
		rc.listAll().fire(new Receiver<List<ClassroomProxy>>() {
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess( List<ClassroomProxy> response) {
				getView().setClassroomList(response);
			}
		});
	}
	
	/*
	 * */
	private ClassroomRequestContext getRequestContext(){
		//
		ClassroomRequestFactory rf = GWT.create(ClassroomRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		return rf.classroomRequestContext();
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
	 * */
	private void loadPeriodParClass(String classId) {
		//
		PeriodRequestFactory rf = GWT.create(PeriodRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		PeriodRequestContext rc = rf.periodRequestContext();
		rc.listAllByClassAndStatus(classId, true).fire(new Receiver<List<PeriodProxy>>(){
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
	@Override
	public void onProfileChanged(final String profileId) {
		//
		if (profileId.isEmpty()) {
			Window.alert( NotificationValues.invalid_input + " - Profil");
			return;
		}
		//
		ProfileSubjectRequestFactory rf = GWT.create(ProfileSubjectRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		ProfileSubjectRequestContext rc = rf.profileSubjectRequest();
		rc.listAll( profileId ).fire(new Receiver<List<ProfileSubjectProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<ProfileSubjectProxy> response) {
				getView().setSubjectListData(response);
				//
				getEventBus().fireEvent( new MasterAgendaLoadEvent(profileId) );
			}
		});
	}

	/*
	 * */
	@Override
	public void addMasterAgendaItem(String jourCode, String periodId,
			String profileId, String profileSubjectId,
			String classroomId, int duration) {
		//
		MasterAgendaItemRequestFactory rf = GWT.create(MasterAgendaItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		MasterAgendaItemRequestContext rc = rf.masterAgendaItemRequestContext();
		rc.addMasterAgendaItem(jourCode, periodId, profileId, profileSubjectId, classroomId, duration).fire(new Receiver<MasterAgendaItemProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(MasterAgendaItemProxy response) {
				getView().showAddedMasterAgendaItem(response);
			}
		});		
	}

	/*
	 * */
	@ProxyEvent
	@Override
	public void onMasterAgendaLoad(MasterAgendaLoadEvent event) {
		//
		MasterAgendaItemRequestFactory rf = GWT.create(MasterAgendaItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		MasterAgendaItemRequestContext rc = rf.masterAgendaItemRequestContext();
		rc.listAllByProfile( event.getProfileId() ).fire(new Receiver<List<MasterAgendaItemProxy>>(){
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

	/*
	 * */
	@Override
	public void removeMasterAgendaItem(MasterAgendaItemProxy mai) {
		//
		MasterAgendaItemRequestFactory rf = GWT.create(MasterAgendaItemRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		MasterAgendaItemRequestContext rc = rf.masterAgendaItemRequestContext();
		rc.removeMasterAgendaItem(mai).fire(new Receiver<Void>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(Void response) {
				getView().clearSelectedMasterAgendaItem();
			}
		});		
	}

}
