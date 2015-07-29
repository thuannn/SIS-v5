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
import com.lemania.sis.client.uihandler.CoursAddUiHandler;
import com.lemania.sis.client.values.NotificationValues;
import com.lemania.sis.shared.CoursProxy;
import com.lemania.sis.shared.EcoleProxy;
import com.lemania.sis.shared.service.CoursRequestFactory;
import com.lemania.sis.shared.service.EcoleRequestFactory;
import com.lemania.sis.shared.service.EventSourceRequestTransport;
import com.lemania.sis.shared.service.CoursRequestFactory.CoursRequestContext;
import com.lemania.sis.shared.service.EcoleRequestFactory.EcoleRequestContext;

public class CoursAddPresenter 
		extends Presenter<CoursAddPresenter.MyView, CoursAddPresenter.MyProxy>
		implements CoursAddUiHandler, LoginAuthenticatedHandler {

	public interface MyView extends View, HasUiHandlers<CoursAddUiHandler> {
		void populateEcoleList(List<EcoleProxy> ecoles);
	}
	
	// Thuan
	private CoursProxy cours;
	private CurrentUser currentUser;

	@ProxyCodeSplit
	@NameToken(NameTokens.coursadd)
	@UseGatekeeper(AdminGateKeeper.class)
	public interface MyProxy extends ProxyPlace<CoursAddPresenter> {
	}

	@Inject
	public CoursAddPresenter(final EventBus eventBus, final MyView view,
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
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		// Thuan
		initialData();
	}
	
	
	/**/
	private void initialData(){
		//
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
	
	
	/**/
	@Override
	public void coursAdd(String coursNom, String ecoleId, Boolean coursActif){
		// Check Read-only
		if (this.currentUser.isReadOnly()){
			Window.alert(NotificationValues.readOnly);
			return;
		}
		
		// Validate data		
		if (coursNom.isEmpty()){
			Window.alert("Veuillez saissir le nom du cours.");
			return;
		}
		if (ecoleId.isEmpty()){
			Window.alert("Veuillez choisir le nom de l'�cole.");
			return;
		}
		
		// Save data
		CoursRequestFactory rf = GWT.create(CoursRequestFactory.class);
		rf.initialize(this.getEventBus(), new EventSourceRequestTransport(this.getEventBus()));
		CoursRequestContext rc = rf.coursRequest();
		cours = rc.create(CoursProxy.class);
		cours.setCoursNom(coursNom);
		cours.setCoursActif(coursActif);
		rc.save(cours, ecoleId).fire(new Receiver<Void>(){
			@Override
			public void onSuccess(Void response){
				returnToCoursListSuccess();
			}
			@Override
			public void onFailure(ServerFailure error){
				Window.alert(error.getMessage());
			}
		});
	}

	@Override
	public void coursAddCancel() {
		// Return to the list of the cours name
		History.newItem(NameTokens.cours);
	}
	
	private void returnToCoursListSuccess() {
		//
		getEventBus().fireEvent(new CoursAddedEvent(cours));
	}

	
	@ProxyEvent
	@Override
	public void onLoginAuthenticated(LoginAuthenticatedEvent event) {
		//
		this.currentUser = event.getCurrentUser();
	}
}
