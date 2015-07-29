package com.lemania.sis.client.presenter;

import java.util.List;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.lemania.sis.client.AdminGateKeeper;
import com.lemania.sis.client.CurrentUser;
import com.lemania.sis.client.event.CoursAddedEvent;
import com.lemania.sis.client.event.CoursAddedEvent.CoursAddedHandler;
import com.lemania.sis.client.event.LoginAuthenticatedEvent;
import com.lemania.sis.client.event.LoginAuthenticatedEvent.LoginAuthenticatedHandler;
import com.lemania.sis.client.form.mainpage.MainPagePresenter;
import com.lemania.sis.client.place.NameTokens;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.lemania.sis.client.uihandler.CoursListUiHandler;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.CoursProxy;
import com.lemania.sis.shared.EcoleProxy;
import com.lemania.sis.shared.service.CoursRequestFactory;
import com.lemania.sis.shared.service.EcoleRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.CoursRequestFactory.CoursRequestContext;
import com.lemania.sis.shared.service.EcoleRequestFactory.EcoleRequestContext;

public class CoursPresenter extends
		Presenter<CoursPresenter.MyView, CoursPresenter.MyProxy> 
		implements CoursListUiHandler, CoursAddedHandler, LoginAuthenticatedHandler {

	//
	private CurrentUser currentUser;
	
	public interface MyView extends View, HasUiHandlers<CoursListUiHandler> {
		void addCours(CoursProxy cours);
		void setData(List<CoursProxy> listCours);
		void refreshTable(CoursProxy updatedCours);
		void populateEcoleList(List<EcoleProxy> ecoles);
		void initializeTable();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.cours)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<CoursPresenter> {
	}

	@Inject
	public CoursPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
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
		
		// Structure the table
		getView().initializeTable();
	}
	
	
	@Override
	protected void onReset(){
		//
		super.onReset();
		// Thuan
		initialData();
	}
	
	
	// Thuan: Populate the list of school names
	private void initialData(){
		EcoleRequestFactory rf = GWT.create(EcoleRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		EcoleRequestContext rc = rf.ecoleRequest();
		rc.listAll().fire(new Receiver<List<EcoleProxy>>(){
			@Override
			public void onSuccess(List<EcoleProxy> response){
				getView().populateEcoleList(response);
			}
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
		});
	}

	
	@ProxyEvent
	@Override
	public void onCoursAdded(CoursAddedEvent event) {
		// TODO Auto-generated method stub
		History.newItem(NameTokens.cours, true);
	}

	
	@Override
	public void updateCoursStatus(CoursProxy cours, Boolean value) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		CoursRequestFactory rf = GWT.create(CoursRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		CoursRequestContext rc = rf.coursRequest();
		CoursProxy coursForUpdate = rc.edit(cours);
		coursForUpdate.setCoursActif(value);
		rc.saveAndReturn(coursForUpdate).fire(new Receiver<CoursProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(CoursProxy response) {
				getView().refreshTable(response);
			}
		});
	}

	@Override
	public void populateCoursList(String ecoleId) {
		if (ecoleId.isEmpty()){
			return;
		}
		
		CoursRequestFactory rf = GWT.create(CoursRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		CoursRequestContext rc = rf.coursRequest();
		rc.listAll(ecoleId).fire(new Receiver<List<CoursProxy>>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(List<CoursProxy> response) {
				getView().setData(response);
			}
		});
	}

	
	/**/
	@Override
	public void updateCoursName(CoursProxy cours, String name) {
		//
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		//
		if (name.equals(""))
			return;
		
		//
		CoursRequestFactory rf = GWT.create(CoursRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		CoursRequestContext rc = rf.coursRequest();
		CoursProxy coursForUpdate = rc.edit(cours);
		coursForUpdate.setCoursNom(name);
		rc.saveAndReturn(coursForUpdate).fire(new Receiver<CoursProxy>(){
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
			@Override
			public void onSuccess(CoursProxy response) {
				getView().refreshTable(response);
			}
		});		
	}

	
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		this.currentUser = event.getCurrentUser();
	}
}
